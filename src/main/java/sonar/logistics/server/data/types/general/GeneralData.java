package sonar.logistics.server.data.types.general;

import sonar.logistics.server.data.api.IData;

public abstract class GeneralData<D> implements IData {

    protected D data;
    private boolean hasUpdated = false;

    public GeneralData(D data){
        this.data = data;
    }

    public void setData(D newData){
        if(hasUpdated(newData, data)) {
            this.data = newData;
            this.hasUpdated = true;
        }
    }

    public void preUpdate(){
        hasUpdated = false;
    }

    public void postUpdate(){}

    public boolean hasUpdated(){
        return hasUpdated;
    }

    public boolean hasUpdated(D newData, D currentData){
        return newData != currentData;
    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }
}