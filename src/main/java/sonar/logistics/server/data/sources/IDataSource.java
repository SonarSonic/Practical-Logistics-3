package sonar.logistics.server.data.sources;

import sonar.logistics.server.data.api.IEnvironment;

//implemented on node connections etc.
public interface IDataSource {

    default IEnvironment getEnvironment(){
        return null;
    }

}
