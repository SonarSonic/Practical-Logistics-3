package sonar.logistics.networking.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.utils.network.EnumSyncType;
import sonar.logistics.blocks.base.SyncableTile;

public class TileSyncPacket extends TileAbstractPacket {

    public CompoundNBT syncTag;
    public EnumSyncType type;

    public TileSyncPacket(PacketBuffer b) {
        super(b);
        syncTag = b.readCompoundTag();
        type = EnumSyncType.values()[b.readInt()];
    }

    public TileSyncPacket(SyncableTile tile, EnumSyncType type) {
        super(tile.getPos());
        this.syncTag = tile.write(new CompoundNBT(), type);
        this.type = type;
    }

    public void encode(PacketBuffer b){
        super.encode(b);
        b.writeCompoundTag(syncTag);
        b.writeInt(type.ordinal());
    }

    @Override
    public void handle(TileEntity tile, NetworkEvent.Context ctx) {
        if(tile instanceof SyncableTile){
            ((SyncableTile) tile).read(syncTag, type);
        }
    }
}
