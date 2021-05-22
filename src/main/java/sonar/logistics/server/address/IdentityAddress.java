package sonar.logistics.server.address;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.server.ServerDataCache;
import sonar.logistics.util.network.EnumSyncType;

import java.util.Objects;

public class IdentityAddress extends Address {
    public int identity;

    public IdentityAddress() {}

    public IdentityAddress(int identity) {
        this.identity = identity;
    }

    public int getIdentity() {
        return identity;
    }

    @Override
    public void read(PacketBuffer buffer) {
        identity = buffer.readInt();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(identity);
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        identity = nbt.getInt("identity");
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        nbt.putLong("identity", identity);
        return nbt;
    }

    @Override
    public int getType() {
        return IDENTITY_ADDRESS;
    }

    @Override
    public boolean updateEnvironment(Environment environment) {
        environment.reset();
        environment.networkedTile = ServerDataCache.INSTANCE.getNetworkedTile(this);
        return environment.networkedTile != null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IdentityAddress){
            return ((IdentityAddress) obj).identity == identity;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(IDENTITY_ADDRESS, identity);
    }

    @Override
    public String toString() {
        return String.format("Identity: (%s)", identity);
    }
}
