package sonar.logistics.util.network;

import net.minecraft.nbt.CompoundNBT;

public interface INBTSyncable {

    CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType);

    CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType);

}
