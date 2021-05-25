package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import sonar.logistics.networking.PL3PacketHandler;

import java.util.function.Supplier;

public class GeneralPacket {

    public GeneralPackets.Types type;
    public Object[] args;

    public PacketBuffer buffer;

    public GeneralPacket(PacketBuffer buffer) {
        decode(buffer);
    }

    public GeneralPacket(GeneralPackets.Types type, Object ...args) {
        this.type = type;
        this.args = args;
    }

    public void encode(PacketBuffer b) {
        b.writeByte(type.ordinal());
        type.encode.apply(b, args);
    }

    public void decode(PacketBuffer b) {
        type = GeneralPackets.Types.values()[b.readByte()];
        buffer = b;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Object reply = type.decode.apply(buffer);
            if(reply != null){
                switch (ctx.get().getDirection().getOriginationSide()){
                    case CLIENT:
                        PL3PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), reply);
                        break;
                    case SERVER:
                        PL3PacketHandler.INSTANCE.sendToServer(reply);
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
