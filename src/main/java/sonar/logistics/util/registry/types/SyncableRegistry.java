package sonar.logistics.util.registry.types;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.IByteBufSaveable;
import sonar.logistics.util.network.INBTSyncable;
import sonar.logistics.util.registry.IRegistryObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncableRegistry<O extends IRegistryObject> extends AbstractRegistry<O> {

    public Map<String, Class<O>> registry = new HashMap<>();

    public void register(String id, Class<? extends IRegistryObject> syncable){
        if(registry.containsKey(id)){
            throw new NullPointerException("Duplicate Registry Name: " + id);
        }
        registry.put(id, (Class<O>)syncable);
    }

    @Override
    public O read(PacketBuffer buffer){
        String registryName = buffer.readString();
        O newObject = instance(registry.get(registryName));
        assert newObject instanceof IByteBufSaveable;
        ((IByteBufSaveable) newObject).read(buffer);
        return newObject;
    }

    @Override
    public void write(IRegistryObject object, PacketBuffer buffer){
        buffer.writeString(object.getRegistryName());
        assert object instanceof IByteBufSaveable;
        ((IByteBufSaveable) object).write(buffer);
    }

    @Override
    public void readList(CompoundNBT nbt, EnumSyncType syncType, String key, List list){
        list.clear();
        ListNBT listNBT = nbt.getList(key, Constants.NBT.TAG_COMPOUND);
        for(INBT nbtValue : listNBT) {
            CompoundNBT objectNBT = (CompoundNBT) nbtValue;
            Object object = read(objectNBT, null, syncType);
            if(object != null){
                list.add(object);
            }
        }
    }

    @Override
    public void writeList(CompoundNBT nbt, EnumSyncType syncType, String key, List list){
        ListNBT listNBT = new ListNBT();
        for(Object object : list){
            CompoundNBT objectNBT = new CompoundNBT();
            write((IRegistryObject) object, objectNBT, null, syncType);
            listNBT.add(objectNBT);
        }
        nbt.put(key, listNBT);
    }

    @Nullable
    public O read(CompoundNBT nbt, @Nullable String key, EnumSyncType syncType){
        CompoundNBT objectNBT = key == null ? nbt : nbt.getCompound(key);

        String registryName = objectNBT.getString("id");
        O newObject = instance(registry.get(registryName));
        if(newObject != null){
            assert newObject instanceof INBTSyncable;
            ((INBTSyncable) newObject).read(objectNBT, syncType);
            return newObject;
        }
        return null;
    }

    @Override
    public void write(IRegistryObject object, CompoundNBT nbt, @Nullable String key, EnumSyncType syncType){
        CompoundNBT objectNBT = key == null ? nbt : new CompoundNBT();

        objectNBT.putString("id", object.getRegistryName());
        assert object instanceof INBTSyncable;
        ((INBTSyncable) object).write(objectNBT, syncType);

        if(key != null)
            nbt.put(key, objectNBT);
    }

    public void clear() {
        registry.clear();
    }
}
