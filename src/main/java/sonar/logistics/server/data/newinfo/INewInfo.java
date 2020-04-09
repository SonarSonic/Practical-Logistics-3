package sonar.logistics.server.data.newinfo;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.server.data.api.DataUUID;

public interface INewInfo {

    DataUUID getUUID();

    void save(CompoundNBT tag);

    void read(CompoundNBT tag);
}
