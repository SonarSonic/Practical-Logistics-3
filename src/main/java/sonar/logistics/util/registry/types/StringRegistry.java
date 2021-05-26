package sonar.logistics.util.registry.types;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.registry.IRegistryObject;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringRegistry<O extends IRegistryObject> extends AbstractRegistry<O> {

    public Map<String, O> registry = new HashMap<>();

    public void register(IRegistryObject registryObject){
        if(registry.containsKey(registryObject.getRegistryName())){
            throw new NullPointerException("Duplicate String: " + registryObject.getRegistryName());
        }
        registry.put(registryObject.getRegistryName(), (O)registryObject);
    }

    public O read(PacketBuffer buffer){
        String registryName = buffer.readString();
        return registry.get(registryName);
    }

    public void write(IRegistryObject object, PacketBuffer buffer){
        buffer.writeString(object.getRegistryName());
    }

    @Override
    public void readList(CompoundNBT nbt, EnumSyncType syncType, String key, List list) {
        list.clear();
        ListNBT listNBT = nbt.getList(key, Constants.NBT.TAG_STRING);
        for(int i = 0; i < listNBT.size(); i++){
            String registryName = listNBT.getString(i);
            O object = registry.get(registryName);
            if(object != null){
                list.add(object);
            }
        }
    }

    @Override
    public void writeList(CompoundNBT nbt, EnumSyncType syncType, String key, List list) {
        ListNBT listNBT = new ListNBT();
        for(Object object : list){
            StringNBT stringNBT = StringNBT.valueOf(((IRegistryObject) object).getRegistryName());
            listNBT.add(stringNBT);
        }
        nbt.put(key, listNBT);
    }

    @Override
    public O read(CompoundNBT nbt, @Nonnull String key, EnumSyncType syncType) {
        String registryName = nbt.getString(key);
        return registry.get(registryName);
    }

    @Override
    public void write(IRegistryObject object, CompoundNBT nbt, @Nonnull String key, EnumSyncType syncType) {
        nbt.putString(key, object.getRegistryName());
    }

    public void clear() {
        registry.clear();
    }

}
