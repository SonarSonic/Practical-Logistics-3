package sonar.logistics.server.data.types.sources;

import net.minecraft.item.ItemStack;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataSource;
import sonar.logistics.server.data.source.Address;

public class SourceData implements IData {

    public Address address;
    public ItemStack stack;

    public SourceData(){
        super();
    }

    public SourceData(Address address, ItemStack stack){
        super();
        this.address = address;
        this.stack = stack;
    }

    public void copyFrom(SourceData dataSource){
        address = dataSource.address;
        stack = dataSource.stack;
    }

    public static SourceData fromDataSource(IDataSource source){
        ItemStack stack = ItemStack.EMPTY;
        if(source.state() != null){
            stack = new ItemStack(source.state().getBlock());
        }
        return new SourceData(source.address(), stack);
    }

}
