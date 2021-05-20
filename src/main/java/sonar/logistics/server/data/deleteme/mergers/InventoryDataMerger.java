package sonar.logistics.server.data.deleteme.mergers;
/*
import sonar.logistics.server.data.deleteme.holders.DataHolder;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.data.methods.VanillaMethods;
import sonar.logistics.server.data.types.inventory.InventoryData;
import sonar.logistics.server.data.types.items.ItemHandlerData;

import java.util.List;

public class InventoryDataMerger implements IDataMerger<InventoryData> {

    public InventoryDataMerger(){}

    @Override
    public Class<InventoryData> getDataType() {
        return InventoryData.class;
    }

    @Override
    public Method getDataMethod() {
        return VanillaMethods.ITEM_CAPABILITY;
    }

    public void generateData(InventoryData inventoryData, List<DataHolder> validHolders){
        inventoryData.preUpdate();
        for (DataHolder holder : validHolders) {
            ItemHandlerData data = (ItemHandlerData) holder.data;
            data.stackList.forEach(stack -> inventoryData.addData(stack, stack.getCount()));
            inventoryData.addStorage(data.inventory_count, data.inventory_max);
        }
        inventoryData.postUpdate();
    }

    public boolean isValidHolder(DataHolder holder){
        return holder.data instanceof ItemHandlerData;
    }
}
*/