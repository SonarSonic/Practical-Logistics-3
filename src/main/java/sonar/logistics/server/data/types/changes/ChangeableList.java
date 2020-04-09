package sonar.logistics.server.data.types.changes;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChangeableList<V> {

    public Map<V, ChangeableNumber> data = new HashMap<>();
    public boolean hasUpdated = false;

    public void preUpdate(){
        Iterator<ChangeableNumber> iterator = data.values().iterator();
        while(iterator.hasNext()){
            ChangeableNumber count = iterator.next();
            if(count.getChange().shouldDelete()){
                iterator.remove();
            }
            count.preUpdate();
        }
        hasUpdated = false;
    }

    public void postUpdate(){
        Iterator<ChangeableNumber> iterator = data.values().iterator();
        while(iterator.hasNext()){
            ChangeableNumber count = iterator.next();
            count.postUpdate();
            if(count.getChange().shouldUpdate()){
                hasUpdated = true;
            }
        }
    }


    @Nullable
    public ChangeableNumber findData(V value){
        if(!isValid(value)){
            return null;
        }
        for(Map.Entry<V, ChangeableNumber> entry : data.entrySet()) {
            if (match(entry.getKey(), value)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void addData(V value, long count){
        if(!isValid(value) || count == 0){
            return;
        }
        ChangeableNumber countData = findData(value);
        if(countData != null){
            countData.count+=count;
            return;
        }
        data.put(value, new ChangeableNumber(count));
    }

    public void removeData(V value, long count){
        if(!isValid(value) || count == 0){
            return;
        }
        ChangeableNumber countData = findData(value);
        if(countData != null){
            countData.count-=count;
        }
    }

    public void setData(V value, long count){
        if(!isValid(value) || count == 0){
            return;
        }
        ChangeableNumber countData = findData(value);
        if(countData != null){
            countData.count = count;
            return;
        }
        data.put(value, new ChangeableNumber(count));
    }

    public boolean isValid(V value){
        return value != null;
    }

    public boolean match(V mapValue, V newValue){
        return mapValue.hashCode() == newValue.hashCode() && mapValue.equals(newValue);
    }

    public void onUpdated(){
        hasUpdated = false;
    }

    public boolean hasUpdated(){
        return hasUpdated;
    }

    public String toString(){
        return data.toString();
    }



}
