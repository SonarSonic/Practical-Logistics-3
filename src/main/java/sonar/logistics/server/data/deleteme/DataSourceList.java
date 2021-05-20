package sonar.logistics.server.data.deleteme;

/*
import sonar.logistics.server.data.DataManagerV2;
import sonar.logistics.server.data.api.Address;
import sonar.logistics.server.data.types.changes.ChangeableList;

/**used for combining multiple data sources into one - which is commonly references e.g.
 * Logistics Networks will have a Data Source List of all attached sources
public class DataSourceList extends ChangeableList<Address> {

    public int identity;

    public DataSourceList(){
        this.identity = DataManagerV2.instance().getNextIdentity();
    }

    public Iterable<Address> getDataSources() {
        return data.keySet();
    }

    public int getIdentity() {
        return identity;
    }

    public void addDataSource(Address dataSource){
        addData(dataSource, 1);
    }

    public void removeDataSource(Address dataSource){
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
}*/