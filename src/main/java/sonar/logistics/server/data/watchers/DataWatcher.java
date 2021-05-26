package sonar.logistics.server.data.watchers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.util.ListHelper;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.INBTSyncable;
import sonar.logistics.util.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class DataWatcher implements INBTSyncable {

    public Consumer<DataWatcher> callback;

    public List<DataAddress> watchingData = new ArrayList<>();
    public List<DataAddress> changedData = new ArrayList<>();

    public DataWatcher(Consumer<DataWatcher> callback){
        this.callback = callback;
    }

    public abstract Address getAddress();

    public abstract boolean isWatcherActive();

    public void tick(){}

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        Registries.getAddressRegistry().readList(nbt, EnumSyncType.SAVE, "watching", watchingData);
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        Registries.getAddressRegistry().writeList(nbt, EnumSyncType.SAVE, "watching", watchingData);
        return nbt;
    }

    public void addDataAddress(DataAddress address) {
        if(!watchingData.contains(address)){
            watchingData.add(address);
            DataManager.INSTANCE.startWatching(address, this);
            callback.accept(this);
        }
    }

    public void removeDataAddress(DataAddress address) {
        if(watchingData.contains(address)) {
            watchingData.remove(address);
            DataManager.INSTANCE.stopWatching(address, this);
            callback.accept(this);
        }
    }

    public void preDataUpdate() {
        changedData.clear();
    }

    public void onDataUpdate(DataAddress address, IData data) {
        if(data.hasUpdated()){
            ListHelper.addWithCheck(changedData, address);
        }
    }

    public void postDataUpdate(){

    }

    public List<DataAddress> getChangedData() {
        return changedData;
    }

    public List<DataAddress> getWatchingAddressList() {
        return watchingData;
    }
}
