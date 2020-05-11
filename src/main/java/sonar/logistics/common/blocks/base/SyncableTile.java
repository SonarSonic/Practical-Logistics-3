package sonar.logistics.common.blocks.base;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.network.PacketDistributor;
import sonar.logistics.networking.PL3PacketHandler;
import sonar.logistics.networking.packets.TileSyncPacket;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.ISyncable;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public class SyncableTile extends TileEntity implements ITickableTileEntity, ISyncable {

    private boolean queueMarkDirty = true;
    private boolean queueSyncPacket = true;

    public SyncableTile(TileEntityType<?> type) {
        super(type);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void tick() {
        flushQueues();
    }

    public void flushQueues(){
        if(world.isRemote){
            return;
        }
        if(queueMarkDirty){
            queueMarkDirty = false;
            markDirty();
        }
        if(queueSyncPacket){
            queueSyncPacket = false;
            syncPacket(EnumSyncType.SYNC);
        }
    }

    public void queueMarkDirty(){
        queueMarkDirty = true;
    }

    public void queueSyncPacket(){
        queueSyncPacket = true;
    }

    public void syncPacket(EnumSyncType type){
        PL3PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(getPos())), new TileSyncPacket(this, type));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
        return tag;
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
        return tag;
    }

    @Override
    public final CompoundNBT write(CompoundNBT tag) {
        write(tag, EnumSyncType.SAVE);
        return super.write(tag);
    }

    @Override
    public final void read(CompoundNBT tag) {
        read(tag, EnumSyncType.SAVE);
        super.read(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 0, write(new CompoundNBT(), EnumSyncType.UPDATE_PACKET));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound(), EnumSyncType.UPDATE_PACKET);
    }
}
