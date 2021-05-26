package sonar.logistics.server.data.types.sources;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.address.Address;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.registry.Registries;

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

        Registries.getAddressRegistry().write(data.address, nbt, ADDRESS_KEY, EnumSyncType.SAVE);
        nbt.put(STACK_KEY, data.stack.write(new CompoundNBT()));

        tag.put(key, nbt);
    }

    @Override
    public void read(SourceData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = tag.getCompound(key);
        data.address = Registries.getAddressRegistry().read(nbt, ADDRESS_KEY, EnumSyncType.SAVE);
        data.stack = ItemStack.read(nbt.getCompound(STACK_KEY));
    }

    @Override
    public void saveUpdate(SourceData data, PacketBuffer buf) {
        Registries.getAddressRegistry().write(data.address, buf);
        buf.writeItemStack(data.stack);
    }

    @Override
    public void readUpdate(SourceData data, PacketBuffer buf) {
        data.address = Registries.getAddressRegistry().read(buf);
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

    @Override
    public SourceData createTest() {
        SourceData data = create();
        data.address = Address.createBlockAddress(new BlockPos(14, 1231, 60), DimensionType.OVERWORLD, Direction.DOWN);
        data.stack = new ItemStack(Blocks.FURNACE, 10);
        return data;
    }
}
