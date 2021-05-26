package sonar.logistics.util.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.FloatNBT;
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

    public static double[] readDoubleArray(CompoundNBT tag, String tagName) {
        ListNBT list = tag.getList(tagName, Constants.NBT.TAG_DOUBLE);
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.getDouble(i);
        }
        return array;
    }

    public static CompoundNBT writeFloatArray(CompoundNBT tag, float[] array, String tagName) {
        ListNBT list = new ListNBT();
        for (float d : array) {
            list.add(FloatNBT.valueOf(d));
        }
        tag.put(tagName, list);
        return tag;
    }

    public static float[] readFloatArray(CompoundNBT tag, String tagName) {
        ListNBT list = tag.getList(tagName, Constants.NBT.TAG_FLOAT);
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.getFloat(i);
        }
        return array;
    }


}
