package sonar.logistics.client.gsi.style.properties;

public enum Unit {

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
