package sonar.logistics.server.data.types.inventory;

import net.minecraft.item.ItemStack;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.types.changes.ChangeableNumber;

public class StoredItemData implements IData {

    public ItemStack stack;
    public ChangeableNumber countData;
    public boolean hasUpdated = false;

    public StoredItemData(){
        this(ItemStack.EMPTY, 0);
    }

    public StoredItemData(ItemStack stack, long count){
        this.stack = stack;
        this.countData = new ChangeableNumber(count);
    }

    @Override
    public void preUpdate(){
        countData.preUpdate();
        hasUpdated = false;
    }

    @Override
    public void postUpdate(){
        countData.postUpdate();
        if(countData.getChange().shouldUpdate()){
            hasUpdated = true;
        }
    }

    @Override
    public void onUpdated(){
        hasUpdated = false;
    }

    @Override
    public boolean hasUpdated(){
        return hasUpdated;
    }

    public boolean isItemType(ItemStack testing){
        return !stack.isEmpty() && stack.isItemEqual(testing) && ItemStack.areItemStackTagsEqual(stack, testing);
    }

    public void addStack(ItemStack stack, long count){
        if(isItemType(stack)){
            countData.count+=count;
        }
    }

    public void removeStack(ItemStack stack, long count){
        if(isItemType(stack)){
            countData.count-=count;
        }
    }

}
