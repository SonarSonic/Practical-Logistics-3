package sonar.logistics.server.data.deleteme.holders;
/*
import sonar.logistics.server.data.DataRegistry;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.data.OLD.mergers.IDataMerger;
import sonar.logistics.server.data.api.IDataWatcher;

import java.util.ArrayList;
import java.util.List;

public class DataMergerHolder<D extends IData> extends DataHolder<D> implements IDataWatcher {

    IDataSourceList sourceList;
    final IDataMerger<D> merger;
    final IDataFactory<D> factory;
    //FIXME TO ADD MULTI-SOURCE which sends updates etc..

    List<DataHolder> sub_holders = new ArrayList<>();

    public boolean updateAllData = true;

    public DataMergerHolder(IDataMerger<D> merger, int tickRate){
        super(tickRate);
        this.merger = merger;
        this.factory = (IDataFactory<D>) DataRegistry.INSTANCE.getDataType(merger.getDataType()).factory;
    }

    @Override
    public boolean isWatcherActive() {
        return true;///dataHolder.hasWatchers; // FIXME
    }

    @Override
    public List<DataHolder> getDataHolders() {
        return sub_holders;
    }

    public void doTick(){
        if(updateAllData) {

            boolean isNew = false;
            if(data == null){
                data = factory.create();
                isNew = true;
            }

            merger.generateData(data, sub_holders);
            ///FIXME MAKE UPDATEDATA VERSION WHEN ONLY CERTAIN SOURCES CHANGE ETC.
            updateAllData=false;

            if(isNew){
                data.onUpdated();
            }
            else if(data.hasUpdated()){
                System.out.println(data);
                data.onUpdated();
            }
        }
    }

    @Override
    public void onDataChanged(DataHolder holder){
        updateAllData = true;
    }

    @Override
    public void addDataHolder(DataHolder holder){
        sub_holders.add(holder);
        updateAllData = true;
    }

    @Override
    public void removeDataHolder(DataHolder holder){
        sub_holders.remove(holder);
        updateAllData = true;

    }

}
*/