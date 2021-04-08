package sonar.logistics.client.gsi.style.properties;

/**
 * Defines a length which is calculated with a {@link Unit}
 */
public class LengthProperty {

    ///defaults / commonly used lengths
    public static final LengthProperty AUTO = new LengthProperty(Unit.AUTO, 0);
    public static final LengthProperty MIN_PIXEL = new LengthProperty(Unit.PIXEL, 0);
    public static final LengthProperty MAX_PIXEL = new LengthProperty(Unit.PIXEL, Float.MAX_VALUE);
    public static final LengthProperty MIN_PERCENT = new LengthProperty(Unit.PERCENT, 0);
    public static final LengthProperty MAX_PERCENT = new LengthProperty(Unit.PERCENT, 100);

    public Unit unitType;
    public double value;

    public LengthProperty(Unit unitType, double value){
        this.unitType = unitType;
        this.value = value;
    }

    public double getValue(double maxValue){
        switch (unitType){
            case AUTO:
                return Float.MAX_VALUE;
            case PERCENT:
                return maxValue * value;
            default:
                return value;
        }
    }

    public static double getLengthSafe(LengthProperty length, double maxValue) {
        return length == null ? 0 : length.getValue(maxValue);
    }

    public static float getLengthSafeF(LengthProperty length, double maxValue) {
        return (float)getLengthSafe(length, maxValue);
    }
}
