package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.caches.network.PL3NetworkManager;

import java.util.function.Supplier;

public abstract class NetworkAbstractPacket {

    public int networkID;

    public NetworkAbstractPacket(PacketBuffer buffer){
        decode(buffer);
    }

    public NetworkAbstractPacket(int networkID){
        this.networkID = networkID;
    }

    public void encode(PacketBuffer b){
        b.writeInt(networkID);
    }

    public void decode(PacketBuffer b){
        networkID = b.readInt();
    }

    public PL3Network getPL3Network(){
        return PL3NetworkManager.INSTANCE.getCachedData(networkID);
    }

    public abstract void handle(Supplier<NetworkEvent.Context> ctx);

}
