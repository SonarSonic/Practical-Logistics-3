package sonar.logistics.common.multiparts.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sonar.logistics.common.blocks.host.MultipartHostTile;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.INBTSyncable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultipartTile implements ICapabilityProvider, INBTSyncable {

    protected final MultipartEntry entry;

    public MultipartTile(MultipartEntry entry) {
        super();
        this.entry = entry;
    }



    //// KEY METHODS \\\\

    public void tick(){}

    public final MultipartEntry getEntry() {
        return entry;
    }

    public final World getHostWorld() {
        return entry.getHost().getWorld();
    }

    public final BlockPos getHostPos() {
        return entry.getHost().getPos();
    }

    public final void queueTileUpdate(){
        queueMarkDirty();;
        queueSyncPacket();
    }

    public final void queueMarkDirty() {
        entry.getHost().queueMarkDirty();
    }

    public final void queueSyncPacket() {
        entry.getHost().queueSyncPacket();
    }


    //// NBT METHODS \\\\

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
        return tag;
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
        return tag;
    }


    //// MULTIPART EVENTS \\\\

    public void onPlaced(World world, BlockState state, BlockPos pos){}

    public void onDestroyed(World world, BlockState state, BlockPos pos){}

    public void onMultipartAdded(MultipartHostTile host, MultipartEntry entry, MultipartEntry added){}

    public void onMultipartRemoved(MultipartHostTile host, MultipartEntry entry, MultipartEntry removed){}


    //// LOADING METHODS \\\\

    protected boolean validated, hadLoadTick;

    public void onLoadTick(){}

    public void onLoad(){
        if(!hadLoadTick){
            onLoadTick();
        }
    }

    public void onUnload(){
        if(hadLoadTick){
            hadLoadTick = false;
        }
    }

    public boolean isValid(){
        return validated;
    }

    public void validate() {
        this.validated = true;
    }

    public void invalidate() {
        this.validated = false;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }
}
