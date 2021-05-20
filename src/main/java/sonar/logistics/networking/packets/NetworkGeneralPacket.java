package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NetworkGeneralPacket extends NetworkAbstractPacket {

    public NetworkPackets.Types type;
    public PacketBuffer buffer;

    public NetworkGeneralPacket(PacketBuffer buffer) {
        super(buffer);
    }

    public NetworkGeneralPacket(int networkID, NetworkPackets.Types type) {
        super(networkID);
        this.type = type;
    }

    @Override
    public void encode(PacketBuffer b) {
        super.encode(b);
        b.writeByte(type.ordinal());
        type.encode.apply(b, networkID);
    }

    @Override
    public void decode(PacketBuffer b) {
        super.decode(b);
        type = NetworkPackets.Types.values()[b.readByte()];
        buffer = b;
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> type.decode.apply(buffer, networkID));
        ctx.get().setPacketHandled(true);
    }
}
