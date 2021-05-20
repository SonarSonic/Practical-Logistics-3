package sonar.logistics.server.data.source;

import com.google.common.base.Objects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.util.network.EnumSyncType;

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
    public int hashCode() {
        return Objects.hashCode(IDENTITY_ADDRESS, identity);
    }

    @Override
    public String toString() {
        return String.format("Identity: (%s)", identity);
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
        //TODO;
        return false;
    }
}
