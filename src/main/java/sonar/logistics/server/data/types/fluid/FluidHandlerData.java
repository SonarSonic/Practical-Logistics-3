package sonar.logistics.server.data.types.fluid;

import net.minecraftforge.fluids.FluidStack;
import sonar.logistics.server.data.api.IData;

import javax.annotation.Nullable;
import java.util.HashMap;

public class FluidHandlerData implements IData {

    public HashMap<Integer, SimpleFluidTank> tankMap = new HashMap<>();
    public boolean hasUpdated = false;

    public void preUpdate(){
        hasUpdated = false;
    }

    public boolean hasUpdated(){
        return hasUpdated;
    }


    public static class SimpleFluidTank{

        @Nullable
        FluidStack contents;
        long capacity;

        public SimpleFluidTank(FluidStack contents, long capacity){
            this.contents = contents;
            this.capacity = capacity;
        }

        @Override
        public String toString(){
            return String.valueOf(contents);
        }

    }

    @Override
    public String toString() {
        return tankMap.values().toString();
    }

}
