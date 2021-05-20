package sonar.logistics.client;

import sonar.logistics.networking.PL3PacketHandler;
import sonar.logistics.networking.packets.NetworkPackets;
import sonar.logistics.networking.packets.NetworkRequestPacket;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.source.DataAddress;
import sonar.logistics.server.data.types.sources.SourceData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDataCache {

    public static final ClientDataCache INSTANCE = new ClientDataCache();

    public Map<DataAddress, IData> dataMap = new HashMap<>();

    public int requestedSources = -1;
    public Map<Integer, List<SourceData>> networkSources = new HashMap<>();

    public void dataSyncPacket(Map<DataAddress, IData> data){
        for(Map.Entry<DataAddress, IData> sync : data.entrySet()){
            dataMap.put(sync.getKey(), sync.getValue());
        }
    }

    public void clearUICaches(){
        requestedSources = -1;
        networkSources.clear();
    }

    public void clear(){
        dataMap.clear();
        networkSources.clear();
    }

    public List<SourceData> getOrRequestNetworkSources(int networkID, boolean refresh){
        if(requestedSources != networkID || refresh){
            requestedSources = networkID;
            PL3PacketHandler.INSTANCE.sendToServer(new NetworkRequestPacket(networkID, NetworkPackets.Types.SOURCES));
        }
        return networkSources.getOrDefault(networkID, new ArrayList<>());
    }

    public static ClientDataCache instance(){
        return INSTANCE;
    }

}