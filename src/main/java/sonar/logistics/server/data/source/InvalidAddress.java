package sonar.logistics.server.data.source;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.util.network.EnumSyncType;

public class InvalidAddress extends Address{

    public static InvalidAddress INSTANCE = new InvalidAddress();

    @Override
    public int getType() {
        return INVALID_ADDRESS;
    }

    @Override
    public boolean updateEnvironment(Environment environment) {
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
}
