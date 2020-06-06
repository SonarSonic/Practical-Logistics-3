package sonar.logistics.client.gsi.style.properties;

public class FlexLength extends Length {

    public UnitLength minLength;
    public UnitLength maxLength;

    public FlexLength(UnitLength minLength, UnitLength maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public double getValue(double maxValue) {
        double min = minLength.getValue(maxValue);
        double max = maxLength.getValue(maxValue);
        return Math.max(min, Math.min(max, maxValue));
    }
}

