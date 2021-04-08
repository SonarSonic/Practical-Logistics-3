package sonar.logistics.client.gsi.style.properties;


public class UnitLength extends Length {

    public static final UnitLength AUTO = new UnitLength(UnitType.AUTO, 0);
    public static final UnitLength MIN_PIXEL = new UnitLength(UnitType.PIXEL, 0);
    public static final UnitLength MAX_PIXEL = new UnitLength(UnitType.PIXEL, Float.MAX_VALUE);
    public static final UnitLength MIN_PERCENT = new UnitLength(UnitType.PERCENT, 0);
    public static final UnitLength MAX_PERCENT = new UnitLength(UnitType.PERCENT, 100);

    public UnitType unitType;
    public double value;

    public UnitLength(UnitType unitType, double value){
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

}
