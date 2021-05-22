package sonar.logistics.server.address;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.server.caches.network.PL3NetworkManager;
import sonar.logistics.util.network.EnumSyncType;

import java.util.Objects;

public class NetworkAddress extends Address {
    public int networkID;

    public NetworkAddress() {}

    public NetworkAddress(int networkID) {
        this.networkID = networkID;
    }

    public int getNetworkID() {
        return networkID;
    }

    @Override
    public void read(PacketBuffer buffer) {
        networkID = buffer.readInt();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(networkID);
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        networkID = nbt.getInt("networkID");
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        nbt.putLong("networkID", networkID);
        return nbt;
    }

    @Override
    public int getType() {
        return NETWORK_ADDRESS;
    }

    @Override
    public boolean updateEnvironment(Environment environment) {
        environment.reset();
        environment.network = PL3NetworkManager.INSTANCE.getCachedData(networkID);
        return environment.network != null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NetworkAddress){
            return ((NetworkAddress) obj).networkID == networkID;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NETWORK_ADDRESS, networkID);
    }

    @Override
    public String toString() {
        return String.format("Network ID: (%s)", networkID);
    }
}
