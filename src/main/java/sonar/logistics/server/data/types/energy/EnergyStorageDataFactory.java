package sonar.logistics.server.data.types.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.energy.IEnergyStorage;
import sonar.logistics.server.data.api.IDataFactory;

public class EnergyStorageDataFactory implements IDataFactory<EnergyStorageData> {

    public static final String ENERGY_TYPE = "type";
    public static final String ENERGY = "energy";
    public static final String CAPACITY = "capacity";

    @Override
    public EnergyStorageData create() {
        return new EnergyStorageData();
    }

    @Override
    public void save(EnergyStorageData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(ENERGY_TYPE, data.type.ordinal());
        nbt.putLong(ENERGY, data.energy);
        nbt.putLong(CAPACITY, data.energy);
        tag.put(key, nbt);
    }

    @Override
    public void read(EnergyStorageData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = tag.getCompound(key);
        data.type = EnumEnergyType.values()[nbt.getInt(ENERGY_TYPE)];
        data.energy = nbt.getLong(ENERGY);
        data.capacity = nbt.getLong(CAPACITY);
    }

    @Override
    public void saveUpdate(EnergyStorageData data, PacketBuffer buf) {
        buf.writeLong(data.energy);
        buf.writeLong(data.capacity);
    }

    @Override
    public void readUpdate(EnergyStorageData data, PacketBuffer buf) {
        data.energy = buf.readLong();
        data.capacity = buf.readLong();
    }

    @Override
    public boolean canConvert(Class returnType){
        return returnType == IEnergyStorage.class;
    }

    @Override
    public void convert(EnergyStorageData data, Object obj){
        if(obj instanceof IEnergyStorage) {
            IEnergyStorage storage = (IEnergyStorage)obj;
            data.type = EnumEnergyType.FE;

            if (data.energy != storage.getEnergyStored()) {
                data.energy = storage.getEnergyStored();
                data.hasUpdated = true;
            }

            if (data.capacity != storage.getMaxEnergyStored()) {
                data.capacity = storage.getMaxEnergyStored();
                data.hasUpdated = true;
            }
        }
    }

    @Override
    public EnergyStorageData createTest() {
        EnergyStorageData testData = create();
        testData.energy = 1000;
        testData.capacity = 50000;
        testData.type = EnumEnergyType.FE;
        return testData;
    }
}
