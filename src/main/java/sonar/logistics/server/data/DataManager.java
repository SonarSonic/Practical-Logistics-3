package sonar.logistics.server.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import sonar.logistics.networking.PL3PacketHandler;
import sonar.logistics.networking.packets.DataSyncPacket;
import sonar.logistics.networking.packets.DataUpdatePacket;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataSource;
import sonar.logistics.server.data.api.IDataWatcher;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.data.source.Address;
import sonar.logistics.server.data.source.DataAddress;
import sonar.logistics.server.data.source.Environment;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    public static final DataManager INSTANCE = new DataManager();

    public List<IDataWatcher> watchers = new ArrayList<>();
    public Map<Address, DataSource> dataSources = new HashMap<>();

    public Map<ServerPlayerEntity, List<DataAddress>> updatePackets = new HashMap<>();
    public Map<ServerPlayerEntity, List<DataAddress>> syncPackets = new HashMap<>();

    private int identity;

    public int getNextIdentity() {
        return identity++;
    }

    public void clear() {
        watchers.clear();
        dataSources.clear();
        syncPackets.clear();
        updatePackets.clear();
    }

    public void update(){
        watchers.forEach(IDataWatcher::preDataUpdate);
        dataSources.values().forEach(DataSource::tick);
        watchers.forEach(IDataWatcher::postDataUpdate);

        ///send sync packets
        for(Map.Entry<ServerPlayerEntity, List<DataAddress>> entry : syncPackets.entrySet()){
            Map<DataAddress, IData> dataMap = getDataMapFromAddressList(entry.getValue());
            PL3PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(entry::getKey), new DataSyncPacket(dataMap));
        }

        //send update packets
        for(Map.Entry<ServerPlayerEntity, List<DataAddress>> entry : updatePackets.entrySet()){
            Map<DataAddress, IData> dataMap = getDataMapFromAddressList(entry.getValue());
            PL3PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(entry::getKey), new DataUpdatePacket(dataMap));
        }
    }

    public void addDataToSyncPacket(ServerPlayerEntity entity, List<DataAddress> addresses){
        syncPackets.putIfAbsent(entity, new ArrayList<>());
        syncPackets.get(entity).addAll(addresses);
    }

    public void addDataToUpdatePacket(ServerPlayerEntity entity, List<DataAddress> addresses){
        updatePackets.putIfAbsent(entity, new ArrayList<>());
        updatePackets.get(entity).addAll(addresses);
    }

    public void addMethod(DataAddress address, IDataWatcher watcher){
        dataSources.computeIfAbsent(address.source, DataSource::new);
        dataSources.get(address.source).addMethod(address.method, watcher);
    }

    public void removeMethod(DataAddress address, IDataWatcher watcher){
        DataSource source = dataSources.get(address.source);
        if(source != null){
            source.removeMethod(address.method, watcher);
        }
    }

    ///// Ad hoc Method Calling \\\\\

    @Nullable
    public static Environment getEnvironment(Address address){
        Environment environment = new Environment(address);
        boolean valid = address.updateEnvironment(environment);
        return valid ? environment : null;
    }

    public static List<IData> invokeMethods(Address source, List<Method> methods){
        List<IData> dataList = new ArrayList<>();
        Environment environment = getEnvironment(source);
        for(Method method : methods){
            IData data = invokeMethod(method, environment);
            if(data != null){
                dataList.add(data);
            }
        }
        return dataList;
    }

    public static IData invokeMethod(DataAddress address){
        return invokeMethod(address.method, getEnvironment(address.source));
    }

    public static IData invokeMethod(Method method, @Nullable IDataSource dataSource){
        if(dataSource != null && method.canInvoke(dataSource)){
            IData data = method.getDataFactory().create();
            Object returned = method.invoke(dataSource);
            method.getDataFactory().convert(data, returned);
            return data;
        }
        return null;
    }

    @Nullable
    public static IData getData(DataAddress address){
        DataSource source = INSTANCE.dataSources.get(address.source);
        if(source != null){
            return source.getData(address);
        }
        return null;
    }

    public static Map<DataAddress, IData> getDataMapFromAddressList(List<DataAddress> addressList){
        Map<DataAddress, IData> dataMap = new HashMap<>();
        for(DataAddress address : addressList){
            IData data = getData(address);
            if(data != null){
                dataMap.put(address, data);
            }
        }
        return dataMap;
    }

    /// General Data Saving

    public static ListNBT writeDataList(List<IData> data){
        ListNBT list = new ListNBT();
        for(IData d : data){
            list.add(writeData(d));
        }
        return list;
    }

    public static List<IData> readDataList(ListNBT listNBT){
        List<IData> dataList = new ArrayList<>();
        for(INBT tag : listNBT){
            CompoundNBT nbt = (CompoundNBT) tag;
            dataList.add(readData(nbt));
        }
        return dataList;
    }

    public static void writeDataList(List<IData> dataList, PacketBuffer buffer){
        buffer.writeInt(dataList.size());
        for(IData data : dataList){
            buffer.writeCompoundTag(writeData(data));
        }
    }

    public static void readDataList(List<IData> dataList, PacketBuffer buffer){
        int size = buffer.readInt();
        for(int i = 0; i < size; i++){
            CompoundNBT nbt = buffer.readCompoundTag();
            if(nbt != null){
                dataList.add(readData(nbt));
            }
        }
    }

    public static CompoundNBT writeData(IData data){
        CompoundNBT nbt = new CompoundNBT();
        DataRegistry.DataType dataType = DataRegistry.INSTANCE.getDataType(data.getClass());
        nbt.putInt("type", dataType.id);
        dataType.factory.save(data, "data", nbt);
        return nbt;
    }

    public static IData readData(CompoundNBT nbt){
        int type = nbt.getInt("type");
        DataRegistry.DataType dataType = DataRegistry.INSTANCE.getDataType(type);
        IData data = dataType.factory.create();
        dataType.factory.read(data, "data", nbt);
        return data;
    }

    public static void writeDataUpdate(IData data, PacketBuffer buffer){
        DataRegistry.DataType dataType = DataRegistry.INSTANCE.getDataType(data.getClass());
        buffer.writeInt(dataType.id);
        dataType.factory.saveUpdate(data, buffer);
    }

    public static void readDataUpdate(IData data, PacketBuffer buffer){
        int type = buffer.readInt();
        DataRegistry.DataType dataType = DataRegistry.INSTANCE.getDataType(type);
        dataType.factory.readUpdate(data, buffer);
    }

    public static DataManager instance(){
        return INSTANCE;
    }
}
