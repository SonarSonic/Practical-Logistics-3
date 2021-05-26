package sonar.logistics.client.gsi.style.properties;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.INBTSyncable;

/**
 * Defines a length which is calculated with a {@link Unit}
 */
public class LengthProperty {

    ///defaults / commonly used lengths
    public static final LengthProperty AUTO = new LengthProperty(Unit.AUTO, 0);
    public static final LengthProperty MIN_PIXEL = new LengthProperty(Unit.PIXEL, 0);
    public static final LengthProperty MAX_PIXEL = new LengthProperty(Unit.PIXEL, Float.MAX_VALUE);
    public static final LengthProperty MIN_PERCENT = new LengthProperty(Unit.PERCENT, 0);
    public static final LengthProperty MAX_PERCENT = new LengthProperty(Unit.PERCENT, 1);

    public Unit unitType;
    public float value;

    public LengthProperty(Unit unitType, float value){
        this.unitType = unitType;
        this.value = value;
    }

    public float getValue(float maxValue){
        switch (unitType){
            case AUTO:
                return Float.MAX_VALUE;
            case PERCENT:
                return maxValue * value;
            default:
                return value;
        }
    }

    public static float getLengthSafe(LengthProperty length, float maxValue) {
        return length == null ? 0 : length.getValue(maxValue);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LengthProperty){
            LengthProperty property = (LengthProperty) obj;
            return property.unitType == this.unitType && property.value == value;
        }
        return super.equals(obj);
    }
}
