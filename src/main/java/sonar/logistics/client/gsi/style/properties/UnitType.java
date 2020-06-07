package sonar.logistics.client.gsi.style.properties;

public enum UnitType {

    AUTO,
    PIXEL,
    PERCENT;

    public boolean isAuto(){
        return this == AUTO;
    }

    public boolean isDefinite(){
        return this != AUTO;
    }

}
