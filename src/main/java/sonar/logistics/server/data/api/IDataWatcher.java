package sonar.logistics.server.data.api;

import sonar.logistics.server.data.source.DataAddress;

import java.util.List;

/**refers to any object which watches the changes of a piece of IData*/
public interface IDataWatcher {

    /**if the data watcher is currently activated by a viewer (generally a player)*/
    boolean isWatcherActive();

    /**the list of addresses the data */
    List<DataAddress> getAddresses();

    default void preDataUpdate(){

    }

    default void onDataUpdate(DataAddress address, IData data){}

    default void postDataUpdate(){

    }
}
