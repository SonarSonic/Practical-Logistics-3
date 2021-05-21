package sonar.logistics.server.data.types.items;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import sonar.logistics.server.data.api.IDataFactory;

public class ItemHandlerDataFactory implements IDataFactory<ItemHandlerData> {

    public static final String INVENTORY_KEY = "inv";
    public static final String INVENTORY_SIZE = "inv_size";
    public static final String INV_COUNT_KEY = "inv_count";
    public static final String INV_MAX_KEY = "inv_max";

    @Override
    public ItemHandlerData create() {
        return new ItemHandlerData();
    }

    @Override
    public void save(ItemHandlerData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt(INVENTORY_SIZE, data.stackList.size());
        ItemStackHelper.saveAllItems(nbt, data.stackList, true);

        nbt.putLong(INV_COUNT_KEY, data.inventory_count);
        nbt.putLong(INV_MAX_KEY, data.inventory_max);

        tag.put(key, nbt);

    }

    @Override
    public void read(ItemHandlerData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = tag.getCompound(key);
        int size = nbt.getInt(INVENTORY_SIZE);

        if(data.stackList.size() != size){
            data.stackList = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
        }else {
            data.stackList.clear();
        }
        ItemStackHelper.loadAllItems(nbt, data.stackList);

        data.inventory_count = nbt.getLong(INV_COUNT_KEY);
        data.inventory_max = nbt.getLong(INV_MAX_KEY);

    }

    @Override
    public void saveUpdate(ItemHandlerData data, PacketBuffer buf) {
        buf.writeInt(data.changedStacks.size());
        if(!data.changedStacks.isEmpty()){
            for(Integer slot : data.changedStacks.keySet()){
                buf.writeInt(slot);
                buf.writeItemStack(data.stackList.get(slot));
            }
        }
        buf.writeBoolean(data.hasCountChanged);
        if(data.hasCountChanged){
            buf.writeLong(data.inventory_count);
            buf.writeLong(data.inventory_max);
        }
    }

    @Override
    public void readUpdate(ItemHandlerData data, PacketBuffer buf) {
        int changed = buf.readInt();
        int current_id = 0;
        while(current_id < changed){
            data.stackList.set(buf.readInt(), buf.readItemStack());
            current_id++;
        }
        if(buf.readBoolean()){
            data.inventory_count = buf.readLong();
            data.inventory_max = buf.readLong();
        }
    }

    @Override
    public boolean canConvert(Class returnType){
        return returnType == IItemHandler.class;
    }

    @Override
    public void convert(ItemHandlerData data, Object obj){

        if(obj instanceof IItemHandler){
            IItemHandler handler = (IItemHandler)obj;

            if(data.stackList.size() != handler.getSlots()){
                data.stackList = NonNullList.<ItemStack>withSize(handler.getSlots(), ItemStack.EMPTY);
            }

            for(int i = 0; i < handler.getSlots() ; i++){
                ItemStack stack = handler.getStackInSlot(i);
                int slotLimit = handler.getSlotLimit(i);
                data.inventory_count += stack.getCount();
                data.inventory_max += slotLimit;

                ItemStack previous = data.stackList.size() > i ? data.stackList.get(i) : null;
                if(previous == null || !ItemStack.areItemStacksEqual(previous, stack)){
                    data.stackList.set(i, stack.copy());
                    data.changedStacks.put(i, previous == null ? ItemStack.EMPTY : previous);
                }
            }
        }
    }

    @Override
    public ItemHandlerData createTest() {
        ItemHandlerData data = create();
        data.stackList.set(0, new ItemStack(Items.FURNACE, 64));
        data.inventory_count = 64;
        data.inventory_max = 10000;
        data.hasCountChanged = true;
        return data;
    }
}
