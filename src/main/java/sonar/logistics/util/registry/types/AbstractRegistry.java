package sonar.logistics.util.registry.types;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.registry.IRegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractRegistry<O extends IRegistryObject> {

    public O instance(Class<O> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e ) {
            throw new NullPointerException("No instance method for: " + clazz);
        }
    }

    public void readList(PacketBuffer buffer, List<O> list){
        int size = buffer.readInt();
        for(int i = 0; i < size; i++){
            list.add(read(buffer));
        }
    }

    public void writeList(PacketBuffer buffer, List<IRegistryObject> list){
        buffer.writeInt(list.size());
        for(IRegistryObject object : list){
            write(object, buffer);
        }
    }

    public abstract O read(PacketBuffer buffer);

    public abstract void write(IRegistryObject object, PacketBuffer buffer);

    public abstract void readList(CompoundNBT nbt, EnumSyncType syncType, String key, List list);

    public abstract void writeList(CompoundNBT nbt, EnumSyncType syncType, String key, List list);

    public abstract O read(CompoundNBT nbt, String key, EnumSyncType syncType);

    public abstract void write(IRegistryObject object, CompoundNBT nbt, String key, EnumSyncType syncType);
}
