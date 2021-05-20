package sonar.logistics.server.data.types.sources;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.data.source.Address;

public class SourceDataFactory implements IDataFactory<SourceData> {

    public static final String ADDRESS_KEY = "address";
    public static final String STACK_KEY = "item";

    @Override
    public SourceData create() {
        return new SourceData();
    }

    @Override
    public void save(SourceData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = new CompoundNBT();

        CompoundNBT addressNBT = new CompoundNBT();
        Address.toNBT(data.address, addressNBT);
        nbt.put(ADDRESS_KEY, addressNBT);

        CompoundNBT stackNBT = new CompoundNBT();
        data.stack.write(stackNBT);
        nbt.put(STACK_KEY, stackNBT);

        tag.put(key, nbt);
    }

    @Override
    public void read(SourceData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = tag.getCompound(key);
        data.address = Address.fromNBT(nbt.getCompound(ADDRESS_KEY));
        data.stack = ItemStack.read(nbt.getCompound(STACK_KEY));
    }

    @Override
    public void saveUpdate(SourceData data, PacketBuffer buf) {
        Address.toPacketBuffer(data.address, buf);
        buf.writeItemStack(data.stack);
    }

    @Override
    public void readUpdate(SourceData data, PacketBuffer buf) {
        data.address = Address.fromPacketBuffer(buf);
        data.stack = buf.readItemStack();
    }

    @Override
    public boolean canConvert(Class returnType) {
        return returnType == SourceData.class;
    }

    @Override
    public void convert(SourceData data, Object obj) {
        if(obj instanceof SourceData){
            SourceData addressData = (SourceData) obj;
            data.copyFrom(addressData);
        }
    }
}
