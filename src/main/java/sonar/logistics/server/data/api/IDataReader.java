package sonar.logistics.server.data.api;

import sonar.logistics.server.data.newinfo.INewInfo;

/**takes data and turns it into IInfo form for the Server & Client*/
public interface IDataReader extends IDataWatcher {

    DataUUID getUUID();

    INewInfo update(INewInfo current);

}
