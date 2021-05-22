package sonar.logistics.networking.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.client.ClientDataCache;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.DataAddress;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DataSyncPacket {

    public Map<DataAddress, IData> dataMap = new HashMap<>();

    public DataSyncPacket(PacketBuffer buffer){
        decode(buffer);
    }

    public DataSyncPacket(Map<DataAddress, IData> dataMap){
        this.dataMap = dataMap;
    }

    public void encode(PacketBuffer b){
        b.writeInt(dataMap.size());

        CompoundNBT mainNBT = new CompoundNBT();
        int count = 0;
        for(Map.Entry<DataAddress, IData> entry : dataMap.entrySet()){
            entry.getKey().method.getDataFactory().save(entry.getValue(), String.valueOf(count), mainNBT);
            count ++;
        }
        b.writeCompoundTag(mainNBT);

        for(Map.Entry<DataAddress, IData> entry : dataMap.entrySet()){
            Address.toPacketBuffer(entry.getKey(), b);
        }
    }

    public void decode(PacketBuffer b){
        int size = b.readInt();
        CompoundNBT mainNBT = b.readCompoundTag();

        for(int i = 0; i < size; i++){
            Address address = Address.fromPacketBuffer(b);
            assert address instanceof DataAddress;
            DataAddress dataAddress = (DataAddress) address;
            IData data = dataAddress.method.getDataFactory().create();
            dataAddress.method.getDataFactory().read(data, String.valueOf(i), mainNBT);
            dataMap.put(dataAddress, data);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> ClientDataCache.INSTANCE.dataSyncPacket(dataMap));
        ctx.get().setPacketHandled(true);
    }

}
