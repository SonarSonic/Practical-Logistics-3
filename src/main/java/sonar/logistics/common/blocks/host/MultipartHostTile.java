package sonar.logistics.common.blocks.host;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import sonar.logistics.PL3;
import sonar.logistics.common.blocks.PL3Blocks;
import sonar.logistics.common.blocks.base.SyncableTile;
import sonar.logistics.common.multiparts.PL3Multiparts;
import sonar.logistics.common.multiparts.base.IMultipartBlock;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.common.multiparts.utils.EnumMultipartSlot;
import sonar.logistics.util.network.EnumSyncType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MultipartHostTile extends SyncableTile {

    public List<MultipartEntry> MULTIPARTS = new ArrayList<>();

    public MultipartHostTile() {
        super(PL3Blocks.MULTIPART_HOST_TILE);
    }

    private boolean checkIntegrity = true;
    public boolean isNBTLoading = false; //is being loaded to nbt, to prevent unnecessary updates
    public boolean setRenderBlockState = true;

    public void tick(){
        forEachTile(MultipartTile::tick);
        super.tick(); //so render updates + dirty updates happen this tick

        if(!world.isRemote && checkIntegrity){
            checkIntegrity = false;
            if(MULTIPARTS.isEmpty()){
                world.setBlockState(this.getPos(), Blocks.AIR.getDefaultState());
            }else if(MULTIPARTS.size() == 1 && !MULTIPARTS.get(0).multipart.requiresMultipartHost()){
                ///TODO CURRENTLY ONLY FOR USE WITH CABLES!
                MultipartEntry entry = MULTIPARTS.get(0);
                if(!entry.multipart.onHostRemoved(entry)) {
                    world.setBlockState(this.getPos(), entry.getBlockState());
                }
            }
        }
        if(world.isRemote && setRenderBlockState){
            MULTIPARTS.forEach(MultipartEntry::updateRenderState);
            setRenderBlockState = false;
        }
    }

    public void queueIntegrityCheck(){
        if(!world.isRemote){
            checkIntegrity = true;
        }
    }

    //// HELPER METHODS \\\\\

    public boolean isSlotTaken(EnumMultipartSlot slot){
        return getMultipart(slot) != null;
    }

    public MultipartEntry getMultipart(EnumMultipartSlot slot){
        for(MultipartEntry entry : MULTIPARTS){
            if(entry.slot == slot){
                return entry;
            }
        }
        return null;
    }

    //// ADD MULTIPARTS \\\\

    public boolean canAddMultipart(MultipartEntry entry){
        return true;
    }

    public boolean doAddMultipart(MultipartEntry entry){
        if(canAddMultipart(entry)){
            MULTIPARTS.add(entry);
            setRenderBlockState = true;
            if(!isNBTLoading){
                queueMarkDirty();
                queueSyncPacket();

                entry.onPlaced();
                MULTIPARTS.forEach(M -> M.onMultipartAdded(entry));

                if(entry.hasMultipartTile()) {
                    validateMultipartTile(entry.getMultipartTile());
                }
            }
            return true;
        }
        return false;
    }

    //// REMOVE MULTIPARTS \\\\

    public boolean canRemoveMultipart(MultipartEntry entry){
        return true;
    }

    public boolean doRemoveMultipart(MultipartEntry entry){
        if(canRemoveMultipart(entry)){
            MULTIPARTS.remove(entry);
            queueIntegrityCheck();

            setRenderBlockState = true;
            if(!isNBTLoading) {
                queueMarkDirty();
                queueSyncPacket();

                entry.onDestroyed();
                MULTIPARTS.forEach(M -> M.onMultipartRemoved(entry));

                if(entry.hasMultipartTile()) {
                    invalidateMultipartTile(entry.getMultipartTile());
                }
            }
            return true;
        }
        return false;
    }

    //// NBT METHODS \\\\

    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
        if(syncType == EnumSyncType.SAVE || syncType == EnumSyncType.SYNC || syncType == EnumSyncType.CHUNK_DATA){
            ListNBT nbtList = new ListNBT();
            MULTIPARTS.forEach(ENTRY -> nbtList.add(writeMultipart(ENTRY, syncType, syncType == EnumSyncType.CHUNK_DATA)));
            tag.put("parts", nbtList);
        }
        return tag;
    }

    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
        isNBTLoading = syncType == EnumSyncType.SAVE;
        if(syncType == EnumSyncType.SAVE || syncType == EnumSyncType.SYNC || syncType == EnumSyncType.CHUNK_DATA){
            ListNBT nbtList = tag.getList("parts", Constants.NBT.TAG_COMPOUND);
            MULTIPARTS.forEach(ENTRY -> ENTRY.setWasNBTLoaded(false));
            nbtList.forEach(NBT -> readMultipart((CompoundNBT) NBT, syncType, syncType == EnumSyncType.CHUNK_DATA));
            MULTIPARTS.stream().filter(ENTRY -> !ENTRY.wasNBTLoaded()).forEach(this::doRemoveMultipart);

            setRenderBlockState = true; //update client rendering
        }
        isNBTLoading = false;
        return tag;
    }

    //// MULTIPART LOADING \\\\\

    public CompoundNBT writeMultipart(MultipartEntry entry, EnumSyncType type, boolean ignoreTileEntity){
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("slot", entry.slot.ordinal());
        entry.getBlockState().cacheState();
        tag.putString("path", entry.multipart.getMultipartRegistryName().getPath());
        if(!ignoreTileEntity && entry.hasMultipartTile()){
            tag.put("tile", entry.getMultipartTile().write(new CompoundNBT(), type));
        }
        return tag;
    }

    @Nullable
    public MultipartEntry readMultipart(CompoundNBT tag, EnumSyncType type, boolean ignoreTileEntity){
        EnumMultipartSlot slot = EnumMultipartSlot.values()[tag.getInt("slot")];
        String registryName = tag.getString("path");

        MultipartEntry entry = getMultipart(slot);
        if(entry == null || !entry.multipart.getMultipartRegistryName().getPath().equals(registryName)){
            IMultipartBlock multipart = PL3Multiparts.getMultipart(new ResourceLocation(PL3.MODID, registryName));
            if(multipart != null) {
                MultipartEntry newEntry = new MultipartEntry(this, multipart, slot);
                doAddMultipart(newEntry);
                entry = newEntry;
            }else{
                doRemoveMultipart(entry);
                return null;
            }
        }
        if(!ignoreTileEntity && entry != null && tag.contains("tile") && entry.hasMultipartTile()){
            entry.getMultipartTile().read(tag.getCompound("tile"), type);
        }
        entry.setWasNBTLoaded(true);
        return entry;
    }


    //// TILE ENTITY NBT \\\\

    public CompoundNBT writeMultipartTileTags(CompoundNBT tag, EnumSyncType type){
        ListNBT nbtList = new ListNBT();
        MULTIPARTS.stream().filter(MultipartEntry::hasMultipartTile).forEach(ENTRY -> {
            CompoundNBT tileTag = ENTRY.getMultipartTile().write(tag, type);
            tileTag.putInt("slot", ENTRY.slot.ordinal());
            nbtList.add(tileTag);
        });
        tag.put("tiles", nbtList);
        return tag;
    }

    public CompoundNBT readMultipartTileTags(CompoundNBT tag, EnumSyncType type){
        ListNBT nbtList = tag.getList("tiles", Constants.NBT.TAG_COMPOUND);
        nbtList.forEach(NBT -> {
            CompoundNBT tileTag =  ((CompoundNBT)NBT);
            EnumMultipartSlot slot = EnumMultipartSlot.values()[tileTag.getInt("slot")];
            MultipartEntry entry = getMultipart(slot);
            if(entry != null && entry.hasMultipartTile()){ //TODO REVIEW - WE SHOULDN'T HAVE TO CHECK NULL.
                getMultipart(slot).getMultipartTile().read(tag, type);
            }
        });
        return tag;
    }


    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        write(tag, EnumSyncType.CHUNK_DATA);
        writeMultipartTileTags(tag, EnumSyncType.CHUNK_DATA);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        super.handleUpdateTag(tag);
        read(tag, EnumSyncType.CHUNK_DATA);
        readMultipartTileTags(tag, EnumSyncType.CHUNK_DATA);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        write(tag, EnumSyncType.UPDATE_PACKET);
        writeMultipartTileTags(tag, EnumSyncType.UPDATE_PACKET);
        return new SUpdateTileEntityPacket(getPos(), 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound(), EnumSyncType.UPDATE_PACKET);
        readMultipartTileTags(pkt.getNbtCompound(), EnumSyncType.UPDATE_PACKET);
    }

    //// MULTIPART TILE METHODS \\\\

    public void validateMultipartTile(MultipartTile tile){
        if(!isRemoved() && !tile.isValid()) {
            tile.validate();
            tile.onLoad();
        }
    }

    public void invalidateMultipartTile(MultipartTile tile){
        if(tile.isValid()) {
            tile.invalidate();
        }
    }

    @Override
    public void onLoad(){
        super.onLoad();
        forEachTile(MultipartTile::onLoad);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        forEachTile(MultipartTile::onUnload);
    }

    @Override
    public void remove() {
        super.remove();
        forEachTile(this::invalidateMultipartTile);
    }

    @Override
    public void validate() {
       super.validate();
       forEachTile(this::validateMultipartTile);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side){
        MultipartEntry entry = getMultipart(EnumMultipartSlot.fromDirection(side));
        if(entry != null && entry.hasMultipartTile()) {
            return entry.getMultipartTile().getCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }

    public final void forEachTile(Consumer<MultipartTile> forEach){
        MULTIPARTS.stream().filter(MultipartEntry::hasMultipartTile).forEach(entry -> forEach.accept(entry.getMultipartTile()));
    }

    public final void forEachNetworkedTile(Consumer<INetworkedTile> forEach){
        MULTIPARTS.stream().filter(MultipartEntry::hasMultipartTile).forEach(entry -> {
            if(entry.getMultipartTile() instanceof INetworkedTile) {
                forEach.accept((INetworkedTile)entry.getMultipartTile());
            }
        });
    }

}
