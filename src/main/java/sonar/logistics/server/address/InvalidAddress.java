package sonar.logistics.server.address;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.util.network.EnumSyncType;

public class InvalidAddress extends Address{

    public static InvalidAddress INSTANCE = new InvalidAddress();

    @Override
    public String getRegistryName() {
        return "invalid";
    }

    @Override
    public boolean updateEnvironment(Environment environment) {
        environment.reset();
        return false;
    }

    @Override
    public void read(PacketBuffer buffer) {}

    @Override
    public void write(PacketBuffer buffer) {}

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        return nbt;
    }

    @Override
    public String toString() {
        return "Invalid Address";
    }

    @Override
    public int hashCode() {
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InvalidAddress;
    }
}
