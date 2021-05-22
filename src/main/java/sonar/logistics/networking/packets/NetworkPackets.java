package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import sonar.logistics.client.ClientDataCache;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.caches.network.PL3NetworkManager;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.methods.VanillaMethods;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.Environment;
import sonar.logistics.server.data.types.sources.SourceData;

import java.util.ArrayList;
import java.util.List;

public class NetworkPackets {

    public enum Types{
        SOURCES(NetworkPackets::encodeSourcesPacket, NetworkPackets::decodeSourcesPacket);

        public IEncode encode;
        public IDecode decode;

        Types(IEncode encode, IDecode decode){
            this.encode = encode;
            this.decode = decode;
        }

        public interface IEncode{
            void apply(PacketBuffer b, int networkID);
        }

        public interface IDecode{
            void apply(PacketBuffer b, int networkID);
        }
    }

    public static void encodeSourcesPacket(PacketBuffer b, int networkID){
        PL3Network network = PL3NetworkManager.INSTANCE.getCachedData(networkID);
        if(network != null){
            b.writeBoolean(true);
            List<IData> sourceData = new ArrayList<>();

            for(Address sourceAddress : network.addressList){
                Environment environment = DataManager.getEnvironment(sourceAddress);
                IData data = DataManager.invokeMethod(VanillaMethods.ADDRESS_DATA, environment);
                sourceData.add((SourceData) data);
            }
            DataManager.writeDataList(sourceData, b);

        }else{
            b.writeBoolean(false);
        }
    }

    public static void decodeSourcesPacket(PacketBuffer b, int networkID){
        if(b.readBoolean()){
            List<IData> data = new ArrayList<>();
            DataManager.readDataList(data, b);

            List<SourceData> sourceData = new ArrayList<>();
            data.stream().filter(d -> d instanceof SourceData).forEach(d -> sourceData.add((SourceData) d));
            ClientDataCache.INSTANCE.networkSources.put(networkID, sourceData);
        }
    }

}
