package sonar.logistics.common.multiparts.networking;

import sonar.logistics.server.data.sources.IDataSource;

import java.util.List;

public interface IDataSourceNode extends INetworkedTile {

    void addDataSources(List<IDataSource> sources);

}
