package sonar.logistics.utils.network;

import net.minecraft.nbt.CompoundNBT;

public interface ISyncable {

    CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType);

    CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType);

}
