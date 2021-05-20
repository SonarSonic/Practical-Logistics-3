package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import sonar.logistics.networking.PL3PacketHandler;

import java.util.function.Supplier;

public class NetworkRequestPacket extends NetworkAbstractPacket {

    public NetworkPackets.Types requestType;

    public NetworkRequestPacket(PacketBuffer buffer) {
        super(buffer);
    }

    public NetworkRequestPacket(int networkID, NetworkPackets.Types requestType) {
        super(networkID);
        this.requestType = requestType;
    }

    @Override
    public void encode(PacketBuffer b) {
        super.encode(b);
        b.writeInt(requestType.ordinal());
    }

    @Override
    public void decode(PacketBuffer b) {
        super.decode(b);
        requestType = NetworkPackets.Types.values()[b.readInt()];
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> PL3PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() ->ctx.get().getSender()), new NetworkGeneralPacket(networkID, requestType)));
        ctx.get().setPacketHandled(true);
    }
}
