package sonar.logistics.util.registry.types;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.registry.IRegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashRegistry<O extends IRegistryObject> extends AbstractRegistry<O> {

    public Map<Integer, O> registry = new HashMap<>();

    public void register(IRegistryObject registryObject){
        if(registry.containsKey(registryObject.getRegistryName().hashCode())){
            throw new NullPointerException("Duplicate Hash: " + registryObject.getRegistryName());
        }
        registry.put(registryObject.getRegistryName().hashCode(), (O)registryObject);
    }

    public O read(PacketBuffer buffer){
        int hashCode = buffer.readInt();
        return registry.get(hashCode);
    }

    public void write(IRegistryObject object, PacketBuffer buffer){
        buffer.writeInt(object.getRegistryName().hashCode());
    }

    @Override
    public void readList(CompoundNBT nbt, EnumSyncType syncType, String key, List list) {
        list.clear();
        ListNBT listNBT = nbt.getList(key, Constants.NBT.TAG_STRING);
        for(int i = 0; i < listNBT.size(); i++){
            int hashCode = listNBT.getInt(i);
            O object = registry.get(hashCode);
            if(object != null){
                list.add(object);
            }
        }
    }

    @Override
    public void writeList(CompoundNBT nbt, EnumSyncType syncType, String key, List list) {
        ListNBT listNBT = new ListNBT();
        for(Object object : list){
            IntNBT stringNBT = IntNBT.valueOf(((IRegistryObject) object).getRegistryName().hashCode());
            listNBT.add(stringNBT);
        }
        nbt.put(key, listNBT);
    }

    public O read(CompoundNBT nbt, String key, EnumSyncType syncType) {
        int hashCode = nbt.getInt(key);
        return registry.get(hashCode);
    }

    public void write(IRegistryObject object, CompoundNBT nbt, String key, EnumSyncType syncType) {
        nbt.putInt(key, object.getRegistryName().hashCode());
    }

    public void clear() {
        registry.clear();
    }

}
