package sonar.logistics.networking.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.client.ClientDataCache;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.caches.network.PL3NetworkManager;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.server.data.watchers.DataWatcher;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.data.methods.MethodCategory;
import sonar.logistics.server.data.methods.MethodRegistry;
import sonar.logistics.server.data.methods.VanillaMethods;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.Environment;
import sonar.logistics.server.data.types.sources.SourceData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralPackets {

    public enum Types{
        REQUEST_SOURCES((b, args) -> GeneralPackets.encodeRequestSources(b, (int)args[0]), GeneralPackets::decodeRequestSources),
        SOURCES((b, args) -> GeneralPackets.encodeSources(b, (int)args[0]), GeneralPackets::decodeSources),
        REQUEST_METHOD_LIST((b, args) -> GeneralPackets.encodeRequestMethodList(b, (int)args[0], (Address) args[1]), GeneralPackets::decodeRequestMethodList),
        METHOD_LIST((b, args) -> GeneralPackets.encodeMethodList(b, (int)args[0], (Address) args[1]), GeneralPackets::decodeMethodList),
        DATA_ADDRESS_SELECTION((b, args) -> GeneralPackets.encodeDataAddressSelection(b, (Address) args[0], (boolean) args[1], (List<DataAddress>) args[2]), GeneralPackets::decodeDataAddressSelection);

        public IEncode encode;
        public IDecode decode;

        Types(IEncode encode, IDecode decode){
            this.encode = encode;
            this.decode = decode;
        }

        public interface IEncode{
            void apply(PacketBuffer b, Object ...args);
        }

        public interface IDecode{
            Object apply(PacketBuffer b);
        }
    }

    public static void encodeRequestMethodList(PacketBuffer b, int networkID, Address address){
        b.writeInt(networkID);
        Address.toPacketBuffer(address, b);
    }

    public static Object decodeRequestMethodList(PacketBuffer b){
        int networkID = b.readInt();
        Address address = Address.fromPacketBuffer(b);
        return new GeneralPacket(Types.METHOD_LIST, networkID, address);
    }

    public static void encodeMethodList(PacketBuffer b, int networkID, Address address) {
        b.writeInt(networkID);
        Address.toPacketBuffer(address, b);

        PL3Network network = PL3NetworkManager.INSTANCE.getCachedData(networkID);
        if(network != null && network.externalAddressList.contains(address)){ //TODO GLOBAL / LOCAL TOO?
            b.writeBoolean(true);
            Environment environment = DataManager.getEnvironment(address);
            List<Method> methods = DataManager.getAvailableMethods(MethodRegistry.methods.values(), environment);
            List<IData> dataList = DataManager.invokeMethods(methods, environment);

            CompoundNBT mainNBT = new CompoundNBT();
            int count = 0;
            for(IData data : dataList){
                Method method = methods.get(count);
                method.getDataFactory().save(data, String.valueOf(count), mainNBT);
                count ++;
            }
            b.writeCompoundTag(mainNBT);
            b.writeInt(methods.size());
            for(Method method : methods){
                b.writeString(method.getIdentifier());
            }
            return;
        }
        b.writeBoolean(false);
    }

    public static Object decodeMethodList(PacketBuffer b) {
        int networkID = b.readInt();
        Address address = Address.fromPacketBuffer(b);
        if(b.readBoolean()){
            CompoundNBT mainNBT = b.readCompoundTag();
            int size = b.readInt();
            Map<MethodCategory, Map<Method, IData>> methods = new HashMap<>();
            for(int i = 0; i < size; i++){
                String identifier = b.readString();
                Method method = MethodRegistry.getMethodFromIdentifier(identifier);
                if(method != null){
                    IData data = method.getDataFactory().create();
                    method.getDataFactory().read(data, String.valueOf(i), mainNBT);
                    if(data != null){
                        methods.putIfAbsent(method.getCategory(), new HashMap<>());
                        methods.get(method.getCategory()).put(method, data);
                    }
                }
            }
            if(ClientDataCache.INSTANCE.requestedAddress != null && ClientDataCache.INSTANCE.requestedAddress.equals(address)){
                ClientDataCache.INSTANCE.methodMap = methods;
            }
        }
        return null;
    }

    public static void encodeRequestSources(PacketBuffer b, int networkID){
        b.writeInt(networkID);
    }

    public static Object decodeRequestSources(PacketBuffer b){
        int networkID = b.readInt();
        return new GeneralPacket(Types.SOURCES, networkID);
    }

    //TODO for things like Connected Display or "Global Network Connections" this should not be based on network, but based on identity...
    public static void encodeSources(PacketBuffer b, int networkID){
        b.writeInt(networkID);
        PL3Network network = PL3NetworkManager.INSTANCE.getCachedData(networkID);
        if(network != null){
            b.writeBoolean(true);
            List<IData> sourceData = new ArrayList<>();

            for(Address sourceAddress : network.externalAddressList){
                Environment environment = DataManager.getEnvironment(sourceAddress);
                IData data = DataManager.invokeMethod(VanillaMethods.ADDRESS_DATA, environment);
                sourceData.add((SourceData) data);
            }
            DataManager.writeDataList(sourceData, b);

        }else{
            b.writeBoolean(false);
        }
    }

    public static Object decodeSources(PacketBuffer b){
        int networkID = b.readInt();
        if(b.readBoolean()){
            List<IData> data = new ArrayList<>();
            DataManager.readDataList(data, b);

            List<SourceData> sourceData = new ArrayList<>();
            data.stream().filter(d -> d instanceof SourceData).forEach(d -> sourceData.add((SourceData) d));
            ClientDataCache.INSTANCE.externalNetworkAddressMap.put(networkID, sourceData);
        }
        return null;
    }

    public static void encodeDataAddressSelection(PacketBuffer b, Address address, boolean add, List<DataAddress> dataAddresses){
        Address.toPacketBuffer(address, b);
        b.writeBoolean(add);
        b.writeInt(dataAddresses.size());
        for(DataAddress dataAddress : dataAddresses){
            Address.toPacketBuffer(dataAddress, b);
        }
    }

    public static Object decodeDataAddressSelection(PacketBuffer b){
        Address address = Address.fromPacketBuffer(b);
        boolean add = b.readBoolean();
        int size = b.readInt();
        List<DataAddress> addressList = new ArrayList<>();
        for(int i = 0; i < size; i++){
            Address dataAddress = Address.fromPacketBuffer(b);
            if(dataAddress instanceof DataAddress){
                addressList.add((DataAddress)dataAddress);
            }
        }
        DataWatcher watcher = DataManager.INSTANCE.getDataWatcher(address);
        if(watcher != null){
            if(add)
                addressList.forEach(watcher::addDataAddress);
            else
                addressList.forEach(watcher::removeDataAddress);
        }
        return null;
    }

}