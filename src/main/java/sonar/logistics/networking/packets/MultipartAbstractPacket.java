package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.blocks.host.MultipartHostTile;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.utils.network.NetworkUtils;

import java.util.function.Supplier;

public abstract class MultipartAbstractPacket {

    public BlockPos pos;
    public EnumMultipartSlot slot;

    public MultipartAbstractPacket(BlockPos pos, EnumMultipartSlot slot){
        this.pos = pos;
        this.slot = slot;
    }

    public MultipartAbstractPacket(PacketBuffer b){
        this.pos = b.readBlockPos();
        this.slot = EnumMultipartSlot.values()[b.readInt()];
    }

    public void encode(PacketBuffer b){
        b.writeBlockPos(pos);
        b.writeInt(slot.ordinal());
    }

    public final void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(()-> {
            TileEntity tile = NetworkUtils.getSafeTileEntity(pos, ctx.get());
            if(tile instanceof MultipartHostTile){
                MultipartHostTile host = (MultipartHostTile) tile;
                MultipartEntry entry = host.getMultipart(slot);
                if(entry != null) {
                    handle(host, entry, ctx.get());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public abstract void handle(MultipartHostTile host, MultipartEntry entry, NetworkEvent.Context ctx);

}
