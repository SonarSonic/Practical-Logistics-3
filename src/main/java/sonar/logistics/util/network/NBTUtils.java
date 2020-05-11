package sonar.logistics.util.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class NBTUtils {

    public static CompoundNBT writeDoubleArray(CompoundNBT tag, double[] array, String tagName) {
        ListNBT list = new ListNBT();
        for (double d : array) {
            list.add(DoubleNBT.valueOf(d));
        }
        tag.put(tagName, list);
        return tag;
    }

    public static double[] readDoubleArray(CompoundNBT tag, String tagName, int size) {
        ListNBT list = tag.getList(tagName, Constants.NBT.TAG_DOUBLE);
        double[] array = new double[size];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.getDouble(i);
        }
        return array;
    }


}
