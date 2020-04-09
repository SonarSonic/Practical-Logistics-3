package sonar.logistics.server.data.types.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.server.data.api.IDataFactory;

public class StoredItemDataFactory implements IDataFactory<StoredItemData> {

    public static final String COUNT_KEY = "pl_count";

    @Override
    public StoredItemData create() {
        return new StoredItemData();
    }

    @Override
    public void save(StoredItemData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = new CompoundNBT();
        data.stack.write(nbt);
        nbt.putLong(COUNT_KEY, data.countData.count);
        tag.put(key, nbt);
    }

    @Override
    public void read(StoredItemData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = tag.getCompound(key);
        data.stack = ItemStack.read(nbt);
        data.countData.count = tag.getLong(COUNT_KEY);

    }

    @Override
    public void saveUpdate(StoredItemData data, PacketBuffer buf) {
        buf.writeItemStack(data.stack);
        buf.writeLong(data.countData.count);
    }

    @Override
    public void readUpdate(StoredItemData data, PacketBuffer buf) {
        data.stack = buf.readItemStack();
        data.countData.count = buf.readLong();
    }
}
