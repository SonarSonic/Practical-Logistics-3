package sonar.logistics.server.data.newinfo;

import sonar.logistics.server.data.api.DataUUID;

public abstract class BaseNewInfo implements INewInfo {

    public final DataUUID uuid;

    public BaseNewInfo(DataUUID uuid){
        this.uuid = uuid;
    }

    @Override
    public DataUUID getUUID() {
        return uuid;
    }

}
