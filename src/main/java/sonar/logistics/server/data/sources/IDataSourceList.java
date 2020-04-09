package sonar.logistics.server.data.sources;

/**should only be used with combinable info, otherwise the first source will be the only one which is used*/
public interface IDataSourceList extends IDataSource {

    Iterable<IDataSource> getDataSources();

    void addDataSource(IDataSource dataSource);

    void removeDataSource(IDataSource dataSource);

    int getIdentity();

}
