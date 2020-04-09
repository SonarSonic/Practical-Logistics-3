package sonar.logistics.server.data.types.inventory;

import net.minecraft.item.ItemStack;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.types.changes.ChangeableList;

public class InventoryData extends ChangeableList<ItemStack>  implements IData {

    public long inventory_count = 0, inventory_max = 0;

    @Override
    public void preUpdate(){
        super.preUpdate();
        inventory_count = 0;
        inventory_max = 0;
    }

    @Override
    public boolean isValid(ItemStack value){
        return value != null && !value.isEmpty();
    }

    @Override
    public boolean match(ItemStack mapValue, ItemStack newValue){
        return mapValue.isItemEqual(newValue) && ItemStack.areItemStackTagsEqual(mapValue, newValue);
    }

    public void addStorage(long count, long max){
        inventory_count += count;
        inventory_max += max;
    }

}