package sonar.logistics.server.data.sources;

import sonar.logistics.server.data.DataManager;
import sonar.logistics.server.data.types.changes.ChangeableList;

/**used for combining multiple data sources into one - which is commonly references e.g.
 * Logistics Networks will have a Data Source List of all attached sources*/
public class DataSourceList extends ChangeableList<IDataSource> implements IDataSourceList {

    public int identity;

    public DataSourceList(){
        this.identity = DataManager.instance().getNextIdentity();
    }

    @Override
    public Iterable<IDataSource> getDataSources() {
        return data.keySet();
    }

    @Override
    public int getIdentity() {
        return identity;
    }

    public void addDataSource(IDataSource dataSource){
        addData(dataSource, 1);
    }

    public void removeDataSource(IDataSource dataSource){
        removeData(dataSource, 1);
    }

    @Override
    public int hashCode(){
        return getIdentity();
    }

    @Override
    public boolean equals(Object object){
        return object instanceof DataSourceList && ((DataSourceList) object).identity == identity;
    }
}
