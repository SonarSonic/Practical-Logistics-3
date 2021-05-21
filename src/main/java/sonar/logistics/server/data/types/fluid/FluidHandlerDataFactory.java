package sonar.logistics.server.data.types.fluid;

import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import sonar.logistics.server.data.api.IDataFactory;

import java.util.Map;

public class FluidHandlerDataFactory implements IDataFactory<FluidHandlerData> {

    public static final String CAPACITY_KEY = "capacity";
    public static final String TANK_ID_KEY = "tankID";
    public static final String TANKS_KEY = "tanks";
    public static final String EMPTY_KEY = "Empty";

    @Override
    public FluidHandlerData create() {
        return new FluidHandlerData();
    }

    @Override
    public void save(FluidHandlerData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        data.tankMap.forEach((ID, T) -> list.add(saveTank(ID, T)));
        nbt.put(TANKS_KEY, list);
        tag.put(key, nbt);
    }

    @Override
    public void read(FluidHandlerData data, String key, CompoundNBT tag) {
        CompoundNBT nbt = tag.getCompound(key);
        data.tankMap.clear();
        ListNBT list = nbt.getList(TANKS_KEY, Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase -> readTank(data, (CompoundNBT) nbtBase));
    }

    @Override
    public void saveUpdate(FluidHandlerData data, PacketBuffer buf) {
        buf.writeInt(data.tankMap.size());
        for(Map.Entry<Integer, FluidHandlerData.SimpleFluidTank> tank : data.tankMap.entrySet()){
            buf.writeCompoundTag(saveTank(tank.getKey(), tank.getValue()));
        }
    }

    @Override
    public void readUpdate(FluidHandlerData data, PacketBuffer buf) {
        int size = buf.readInt();
        int current_id = 0;
        while(current_id < size){
            readTank(data, buf.readCompoundTag());
            current_id++;
        }
    }

    private CompoundNBT saveTank(Integer tankID, FluidHandlerData.SimpleFluidTank tank){
        CompoundNBT tag = new CompoundNBT();

        tag.putInt(TANK_ID_KEY, tankID);
        tag.putLong(CAPACITY_KEY, tank.capacity);

        if (tank.contents != null){
            tank.contents.writeToNBT(tag);
        }else{
            tag.putString(EMPTY_KEY, "");
        }

        return tag;
    }

    public FluidHandlerData.SimpleFluidTank readTank(FluidHandlerData data, CompoundNBT tag){

        int tankID = tag.getInt(TANK_ID_KEY);
        long capacity = tag.getLong(CAPACITY_KEY);

        FluidStack contents;
        if (!tag.contains(EMPTY_KEY)){
            contents = FluidStack.loadFluidStackFromNBT(tag);
        }else{
            contents = null;
        }

        FluidHandlerData.SimpleFluidTank tank = new FluidHandlerData.SimpleFluidTank(contents, capacity);
        data.tankMap.put(tankID, tank);
        return tank;
    }

    @Override
    public boolean canConvert(Class returnType){
        return returnType == IFluidHandler.class;
    }

    @Override
    public void convert(FluidHandlerData data, Object obj){
        if(obj instanceof IFluidHandler) {
            IFluidHandler handler = ((IFluidHandler) obj);
            for(int i = 0; i < handler.getTanks(); i ++){
                FluidHandlerData.SimpleFluidTank simpleTank = data.tankMap.get(i);
                FluidStack contents = handler.getFluidInTank(i);
                int capacity = handler.getTankCapacity(i);
                if(simpleTank != null){
                    if(!equalNullableFluidStacks(contents, simpleTank.contents)){
                        simpleTank.contents = contents;
                        simpleTank.capacity = capacity;
                        data.hasUpdated = true;
                    }
                }else{
                    data.tankMap.put(i, new FluidHandlerData.SimpleFluidTank(contents, capacity));
                    data.hasUpdated = true;
                }
            }
        }
    }



    private boolean equalNullableFluidStacks(FluidStack fluid1, FluidStack fluid2){
        if(fluid1 == null && fluid2 == null){
            return true;
        }
        if(fluid1 == null || fluid2 == null){
            return false;
        }
        return fluid1.isFluidStackIdentical(fluid2);
    }

    @Override
    public FluidHandlerData createTest() {
        FluidHandlerData data = create();
        data.tankMap.put(0, new FluidHandlerData.SimpleFluidTank(new FluidStack(Fluids.LAVA, 500), 1000));
        data.tankMap.put(1, new FluidHandlerData.SimpleFluidTank(new FluidStack(Fluids.WATER, 1000), 1000));
        return data;
    }
}
