package sonar.logistics.client;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.networking.PL3PacketHandler;
import sonar.logistics.networking.packets.GeneralPacket;
import sonar.logistics.networking.packets.GeneralPackets;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.data.methods.MethodCategory;
import sonar.logistics.server.data.types.sources.SourceData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDataCache {

    public static final ClientDataCache INSTANCE = new ClientDataCache();

    public Map<DataAddress, IData> dataMap = new HashMap<>();


    ///data creation wizard ui
    public int requestedSources = -1;
    public Map<Integer, List<SourceData>> externalNetworkAddressMap = new HashMap<>();

    public Address requestedAddress = null;
    public Map<MethodCategory, Map<Method, IData>> methodMap = new HashMap<>();

    public void dataSyncPacket(Map<DataAddress, IData> data){
        for(Map.Entry<DataAddress, IData> sync : data.entrySet()){
            dataMap.put(sync.getKey(), sync.getValue());
        }
    }

    public void clearUICaches(){
        requestedAddress = null;
        requestedSources = -1;
        externalNetworkAddressMap.clear();
        methodMap.clear();
    }

    public void clear(){
        dataMap.clear();
        externalNetworkAddressMap.clear();
        methodMap.clear();
    }

    public List<SourceData> getOrRequestNetworkSources(int networkID, boolean refresh){
        if(requestedSources != networkID || refresh){
            requestedSources = networkID;
            PL3PacketHandler.INSTANCE.sendToServer(new GeneralPacket(GeneralPackets.Types.REQUEST_SOURCES, networkID));
        }
        return externalNetworkAddressMap.getOrDefault(networkID, new ArrayList<>());
    }

    public Map<MethodCategory, Map<Method, IData>> getOrRequestAvailableMethods(int networkID, Address address, boolean refresh){
        if(requestedAddress == null || !requestedAddress.equals(address) || refresh){
            requestedAddress = address;
            PL3PacketHandler.INSTANCE.sendToServer(new GeneralPacket(GeneralPackets.Types.REQUEST_METHOD_LIST, networkID, address));
            methodMap.clear();
        }
        return methodMap;
    }
}