package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.util.network.NetworkUtils;

import java.util.function.Supplier;

public abstract class TileAbstractPacket {

    public BlockPos pos;

    public TileAbstractPacket(PacketBuffer b){
        pos = b.readBlockPos();
    }

    public TileAbstractPacket(BlockPos pos){
        this.pos = pos;
    }

    public void encode(PacketBuffer b){
        b.writeBlockPos(pos);
    }

    public final void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(()-> {
            TileEntity tile = NetworkUtils.getSafeTileEntity(pos, ctx.get());
            if(tile != null){
                handle(tile, ctx.get());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public abstract void handle(TileEntity tile, NetworkEvent.Context ctx);

}
