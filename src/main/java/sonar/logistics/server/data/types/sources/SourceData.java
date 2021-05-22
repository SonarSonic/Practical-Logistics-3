package sonar.logistics.server.data.types.sources;

import net.minecraft.item.ItemStack;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.Environment;
import sonar.logistics.server.address.InvalidAddress;

public class SourceData implements IData {

    public Address address = InvalidAddress.INSTANCE;
    public ItemStack stack = ItemStack.EMPTY;

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

    public static SourceData fromEnvironment(Environment environment){
        ItemStack stack = ItemStack.EMPTY;
        if(environment.state() != null){
            stack = new ItemStack(environment.state().getBlock());
        }
        return new SourceData(environment.address(), stack);
    }

}
