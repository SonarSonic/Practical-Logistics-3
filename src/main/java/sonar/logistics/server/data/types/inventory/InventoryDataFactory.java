package sonar.logistics.server.data.types.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.data.types.changes.ChangeableNumber;

import java.util.HashMap;
import java.util.Map;

public class InventoryDataFactory implements IDataFactory<InventoryData> {

    public static final String INVENTORY_KEY = "inv";
    public static final String COUNT_KEY = "pl_count";
    public static final String INV_COUNT_KEY = "inv_count";
    public static final String INV_MAX_KEY = "inv_max";

    @Override
    public InventoryData create() {
        return new InventoryData();
    }

    @Override
    public void save(InventoryData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        data.data.forEach((S,L) -> list.add(saveStack(S,L)));
        nbt.put(INVENTORY_KEY, list);

        nbt.putLong(INV_COUNT_KEY, data.inventory_count);
        nbt.putLong(INV_MAX_KEY, data.inventory_max);
        tag.put(key, nbt);
    }

    @Override
    public void read(InventoryData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = tag.getCompound(key);
        data.data.clear();
        ListNBT list = nbt.getList(INVENTORY_KEY, Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase -> readStack(data, (CompoundNBT) nbtBase));

        data.inventory_count = nbt.getLong(INV_COUNT_KEY);
        data.inventory_max = nbt.getLong(INV_MAX_KEY);
    }

    @Override
    public void saveUpdate(InventoryData data, PacketBuffer buf) {
        Map<ItemStack, ChangeableNumber> changed = new HashMap<>();
        data.data.forEach((S,L) -> {if(L.getChange().shouldUpdate()){changed.put(S,L);}});
        buf.writeInt(changed.size());
        if(!changed.isEmpty()){
            for(Map.Entry<ItemStack, ChangeableNumber> entry : changed.entrySet()){
                buf.writeItemStack(entry.getKey());
                buf.writeLong(entry.getValue().count);
            }
        }
    }

    @Override
    public void readUpdate(InventoryData data, PacketBuffer buf) {
        int changed = buf.readInt();
        int current_id = 0;
        while(current_id < changed){
            ItemStack stack = buf.readItemStack();
            long count = buf.readLong();
            data.setData(stack, count);
            current_id++;
        }
    }

    private CompoundNBT saveStack(ItemStack stack, ChangeableNumber count){
        CompoundNBT tag = new CompoundNBT();
        stack.write(tag);
        tag.putLong(COUNT_KEY, count.count);
        return tag;
    }

    public void readStack(InventoryData data, CompoundNBT tag){
        ItemStack stack = ItemStack.read(tag);
        long count = tag.getLong(COUNT_KEY);
        data.data.put(stack, new ChangeableNumber(count));
    }
}
