package sonar.logistics.server.data.deleteme;
/*
import sonar.logistics.server.data.deleteme.DataSourceList;
import sonar.logistics.server.data.deleteme.mergers.IDataMerger;
import sonar.logistics.server.data.api.IDataWatcher;
import sonar.logistics.server.data.deleteme.holders.DataHolder;
import sonar.logistics.server.data.deleteme.holders.DataMergerHolder;
import sonar.logistics.server.data.deleteme.holders.SourceHolder;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.data.api.IDataSource;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Manages the syncing / updating


public class OLDDataManager {

    public static final OLDDataManager INSTANCE = new OLDDataManager();

    private final List<IDataWatcher> addedWatchers = new ArrayList<>();
    private final List<IDataWatcher> removedWatchers = new ArrayList<>();
    //private Map<InfoUUID, IDataWatcher> LOADED_WATCHERS = new HashMap<>();
    //private Map<IDataSource, DataGeneratorHolder> DATA_MERGERS = new HashMap<>();

    private final List<DataSourceList> DATA_SOURCE_LISTS = new ArrayList<>();
    private final Map<IDataSource, SourceHolder> DATA_SOURCES = new HashMap<>();
    private final List<DataMergerHolder> DATA_MERGERS = new ArrayList<>();

    public static OLDDataManager instance(){
        return INSTANCE;
    }

    private int identity;

    public int getNextIdentity(){
        return identity++;
    }

    ////CREATION

    @Nonnull
    public SourceHolder getOrCreateSourceHolder(IDataSource source){
        SourceHolder sourceHolder = DATA_SOURCES.get(source);

        if(sourceHolder == null){
            sourceHolder = new SourceHolder(source);
            DATA_SOURCES.putIfAbsent(source, sourceHolder);
            /*
            MethodRegistry.tileEntityFunction.stream().filter(f -> f.canInvoke(env)).forEach(f -> dataHolder.holders.put(f, new DataHolder(20)));
            MethodRegistry.blockFunction.stream().filter(f -> f.canInvoke(env)).forEach(f -> dataHolder.holders.put(f, new DataHolder(20)));
            MethodRegistry.worldFunction.stream().filter(f -> f.canInvoke(env)).forEach(f -> dataHolder.holders.put(f, new DataHolder(20)));

        }
        return sourceHolder;
    }

    public DataHolder getOrCreateDataHolder(IDataSource source, Method method, int tickRate){
        SourceHolder holder = getOrCreateSourceHolder(source);
        DataHolder dataHolder = holder.holders.get(method);

        if(dataHolder == null){
            dataHolder = new DataHolder(tickRate);
            dataHolder.sourceIncompatible = !method.canInvoke(source);
            dataHolder.data = method.getDataType().factory.create();
            dataHolder.forceUpdate = true;
            holder.holders.put(method, dataHolder);
        }
        return dataHolder;
    }

    public DataMergerHolder createDataMerger(DataSourceList sourceList, IDataMerger merger, int tickRate){
        DataMergerHolder generatorHolder = new DataMergerHolder(merger, tickRate);
        Method method = merger.getDataMethod();

        for(IDataSource source : sourceList.getDataSources()){
            DataHolder dataHolder = getOrCreateDataHolder(source, method, tickRate);
            if (dataHolder != null) {
                generatorHolder.addDataHolder(dataHolder);
                dataHolder.addWatcher(generatorHolder);
            }
        }

        DATA_MERGERS.add(generatorHolder);
        return generatorHolder;
    }




    ////CHANGES

    //TODO
    public void onSourceChanged(IDataSource source){
        SourceHolder methodHolder = DATA_SOURCES.get(source);
        if(methodHolder != null){
            for(Map.Entry<Method, DataHolder> entry : methodHolder.holders.entrySet()){
                if(!entry.getKey().canInvoke(source)){
                    entry.getValue().sourceIncompatible = true;

                    entry.getValue().data.preUpdate();
                    entry.getValue().data = entry.getKey().getDataType().factory.create();
                    entry.getValue().data.postUpdate();

                    entry.getValue().onDataChanged();
                    entry.getValue().data.onUpdated();
                }else{
                    if(entry.getValue().sourceIncompatible) {
                        entry.getValue().sourceIncompatible = false;
                        entry.getValue().forceUpdate = true;
                    }
                }
            }
        }
    }

    public void onSourceAdded(IDataSource source){

    }

    public void onSourceRemoved(IDataSource source){

    }


    ////REMOVALS



    //// UPDATING

    public void flushWatchers(){
        addedWatchers.forEach(watcher -> watcher.getAddresses().stream().filter(Objects::nonNull).forEach(h -> h.addWatcher(watcher)));
        removedWatchers.forEach(watcher -> watcher.getAddresses().stream().filter(Objects::nonNull).forEach(h -> h.removeWatcher(watcher)));

        addedWatchers.clear();
        removedWatchers.clear();
    }

    public void flushUpdates(){
        DATA_SOURCES.values().forEach(SourceHolder::updateData);
        DATA_MERGERS.forEach(DataMergerHolder::doTick);
    }

    public void constructingPhase(){
        OLDDataManager.instance().flushWatchers();
        OLDDataManager.instance().flushUpdates();
    }

    public void updatingPhase(){
        //TODO ?
    }

    public void notifyingPhase(){
        ////FIXME SEND DATA CHANGES
    }


    ////REGISTERING


    public void clear(){
        addedWatchers.clear();
        removedWatchers.clear();
        //LOADED_WATCHERS.clear();
        DATA_SOURCES.clear();
        DATA_MERGERS.clear();
    }


    /*
    @Nonnull
    public <D extends IData> DataHolder<D> getOrCreateDataHolder(Class<D> dataType, IDataSource source, int tickRate){
        DataHolder holder = getDataHolder(dataType, source);
        if(holder != null){
            return holder;
        }

        if(source instanceof IDataMultiSource) {
            IDataMultiSource multiSource = (IDataMultiSource) source;
            DataHolderMultiSource newMultiHolder = new DataHolderMultiSource(multiSource, getFactory(dataType), tickRate);
            multiSource.getDataSources().forEach(s -> {
                DataHolder dataHolder = getOrCreateDataHolder(dataType, s, tickRate);
                newMultiHolder.addDataHolder(dataHolder);
                dataHolder.addWatcher(newMultiHolder);
            });
            HOLDER_MULTI_SOURCE_MAP.computeIfAbsent(multiSource, FunctionHelper.ARRAY).add(newMultiHolder);
            return newMultiHolder;
        }else{
            DataHolder newHolder = new DataHolder(getValidGenerator(source, dataType), source, getFactory(dataType).create(), tickRate);
            HOLDER_SOURCE_MAP.computeIfAbsent(source, FunctionHelper.ARRAY).add(newHolder);
            return newHolder;
        }
    }


    @Nullable
    public <D extends IData> DataHolder getDataHolder(Class<D> dataType, IDataSource source){
        List<? extends DataHolder> holders = (source instanceof IDataMultiSource? HOLDER_MULTI_SOURCE_MAP : HOLDER_SOURCE_MAP).get(source);
        if(holders != null && !holders.isEmpty()){
            for(DataHolder holder : holders){
                if(dataType == holder.data.getClass()){
                    return holder;
                }
            }
        }
        return null;
    }

    public void removeDataHolder(DataHolder holder){
        if(holder instanceof DataHolderMultiSource){
            DataHolderMultiSource multiHolder = (DataHolderMultiSource) holder;
            List<DataHolderMultiSource> holders = HOLDER_MULTI_SOURCE_MAP.get(multiHolder.source);
            holders.remove(holder);
            if(holders.size() == 0){
                HOLDER_SOURCE_MAP.remove(holder.source);
            }
            multiHolder.subDataHolders.forEach(h -> ((DataHolder)h).removeWatcher(multiHolder));
            holder.onHolderDestroyed();
        }else{
            List<DataHolder> holders = HOLDER_SOURCE_MAP.get(holder.source);
            holders.remove(holder);
            if(holders.size() == 0){
                HOLDER_SOURCE_MAP.remove(holder.source);
            }
            holder.onHolderDestroyed();
        }
    }


    public void removeDataSource(IDataSource source){
        List<DataHolder> holders = HOLDER_SOURCE_MAP.get(source);
        if(holders != null && !holders.isEmpty()){
            holders.forEach(h -> h.onHolderDestroyed());
            holders.clear();
        }
    }


    public Map<InfoUUID, IDataWatcher> getDataWatchers(){
        return LOADED_WATCHERS;
    }

    public void addWatcher(IDataWatcher watcher){
        if(watcher != null) {
            addedWatchers.add(watcher);
        }
    }

    public void removeWatcher(IDataWatcher watcher){
        if(watcher != null) {
            removedWatchers.add(watcher);
        }
    }

    public void onWatcherChanged(IDataWatcher watcher){
        watcher.getDataHolders().forEach(holder -> holder.onWatchersChanged());
    }

    public void onMultiSourceChanged(IDataMultiSource multiSource){
        List<DataHolderMultiSource> holders = HOLDER_MULTI_SOURCE_MAP.get(multiSource);
        if(holders !=null && !holders.isEmpty()) {
            //holders.forEach(mHolder -> mHolder.getDataHolders().forEach(holder -> ((IDataHolder)holder).removeWatcher(mHolder)));
            for(DataHolderMultiSource mHolder : holders) {
                List<DataHolder> oldHolders = Lists.newArrayList(mHolder.subDataHolders);
                List<DataHolder> newHolders = new ArrayList<>();
                multiSource.getDataSources().forEach(s -> {
                    newHolders.add(getOrCreateDataHolder(mHolder.data.getClass(), s, mHolder.tickRate));
                });
                List<DataHolder> removed = new ArrayList<>();
                for (DataHolder ref : oldHolders) {
                    if (!newHolders.contains(ref)) {
                        removed.add(ref);
                        continue;
                    }
                    newHolders.remove(ref);
                }

                if (!newHolders.isEmpty() || !removed.isEmpty()) {
                    newHolders.forEach(holder -> mHolder.addDataHolder(holder));
                    removed.forEach(holder -> mHolder.removeDataHolder(holder));
                }
            }
        }

    }

    public void sendInfoPackets(){
        for(Map.Entry<InfoUUID, IDataWatcher> entry : LOADED_WATCHERS.entrySet()){
            if(entry.getValue().isWatcherActive()){
                IInfo oldInfo = ServerInfoHandler.instance().getInfoMap().get(entry.getKey());
               // IInfo newInfo = entry.createValue().updateData(oldInfo);
            }
        }
    }
}
    */
