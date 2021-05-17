package sonar.logistics.server.data.mergers;

import net.minecraft.item.ItemStack;
import sonar.logistics.server.data.api.IDataMerger;
import sonar.logistics.server.data.api.methods.IMethod;
import sonar.logistics.server.data.holders.DataHolder;
import sonar.logistics.server.data.methods.VanillaMethods;
import sonar.logistics.server.data.types.inventory.StoredItemData;
import sonar.logistics.server.data.types.items.ItemHandlerData;

import java.util.List;

public class StoredItemDataMerger implements IDataMerger<StoredItemData> {

    public ItemStack stack;

    public StoredItemDataMerger(ItemStack stack){
        this.stack = stack;
    }

    @Override
    public Class<StoredItemData> getDataType() {
        return StoredItemData.class;
    }

    @Override
    public IMethod getDataMethod() {
        return VanillaMethods.ITEM_CAPABILITY;
    }

    @Override
    public void generateData(StoredItemData itemData, List<DataHolder> validHolders) {
        itemData.preUpdate();
        for (DataHolder holder : validHolders) {
            ItemHandlerData data = (ItemHandlerData) holder.data;
            data.stackList.stream().filter(itemData::isItemType).forEach(S -> itemData.addStack(S, S.getCount()));
        }
        itemData.postUpdate();
    }

    @Override
    public boolean isValidHolder(DataHolder holder) {
        return holder.data instanceof ItemHandlerData;
    }
}
