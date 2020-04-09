package sonar.logistics.server.data.types.changes;

public class ChangeableNumber {

    public long lastCount;
    public long count;
    public EnumChangeable enumChange = EnumChangeable.NEW_VALUE;

    public ChangeableNumber(long count){
        this.count = count;
    }

    public void preUpdate(){
        this.lastCount = count;
        this.count = 0;
    }

    public void postUpdate(){
        this.enumChange = EnumChangeable.getChange(count, lastCount);
    }

    public EnumChangeable getChange(){
        return enumChange;
    }

    public String toString(){
        return "Count: " + count + "Change: " + enumChange.name();
    }

}