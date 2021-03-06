package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.client.ClientDataCache;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.util.registry.Registries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DataUpdatePacket {

    public Map<DataAddress, IData> dataMap = new HashMap<>();

    public DataUpdatePacket(PacketBuffer buffer){
        decode(buffer);
    }

    public DataUpdatePacket(Map<DataAddress, IData> dataMap){
        this.dataMap = dataMap;
    }

    public void encode(PacketBuffer b){
        b.writeInt(dataMap.size());
        for(Map.Entry<DataAddress, IData> entry : dataMap.entrySet()){
            Registries.getAddressRegistry().write(entry.getKey(), b);
            entry.getKey().method.getDataFactory().saveUpdate(entry.getValue(), b);
        }
    }

    public void decode(PacketBuffer b){
        int size = b.readInt();
        for(int i = 0; i < size; i++){
            Address address = Registries.getAddressRegistry().read(b);
            assert address instanceof DataAddress;
            DataAddress dataAddress = (DataAddress) address;
            IData data = ClientDataCache.INSTANCE.dataMap.get(address);
            if(data == null){
                data = dataAddress.method.getDataFactory().create();
                ClientDataCache.INSTANCE.dataMap.put(dataAddress, data);
            }
            dataAddress.method.getDataFactory().readUpdate(data, b);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> ClientDataCache.INSTANCE.dataSyncPacket(dataMap));
        ctx.get().setPacketHandled(true);
    }

}
