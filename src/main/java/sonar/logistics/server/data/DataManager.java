package sonar.logistics.server.data;

import net.minecraft.item.ItemStack;
import sonar.logistics.PL3;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.data.api.IDataMerger;
import sonar.logistics.server.data.api.IDataWatcher;
import sonar.logistics.server.data.api.methods.IMethod;
import sonar.logistics.server.data.generators.InventoryDataMerger;
import sonar.logistics.server.data.generators.StoredItemDataMerger;
import sonar.logistics.server.data.holders.DataHolder;
import sonar.logistics.server.data.holders.DataMergerHolder;
import sonar.logistics.server.data.holders.SourceHolder;
import sonar.logistics.server.data.sources.IDataSource;
import sonar.logistics.server.data.sources.IDataSourceList;
import sonar.logistics.server.data.types.energy.EnergyStorageData;
import sonar.logistics.server.data.types.energy.EnergyStorageDataFactory;
import sonar.logistics.server.data.types.fluid.FluidHandlerData;
import sonar.logistics.server.data.types.fluid.FluidHandlerDataFactory;
import sonar.logistics.server.data.types.general.PrimitiveDataTypes;
import sonar.logistics.server.data.types.general.SpecialDataTypes;
import sonar.logistics.server.data.types.inventory.InventoryData;
import sonar.logistics.server.data.types.inventory.InventoryDataFactory;
import sonar.logistics.server.data.types.inventory.StoredItemData;
import sonar.logistics.server.data.types.inventory.StoredItemDataFactory;
import sonar.logistics.server.data.types.items.ItemHandlerData;
import sonar.logistics.server.data.types.items.ItemHandlerDataFactory;

import javax.annotation.Nonnull;
import java.util.*;

public class DataManager {

    private Map<Class, Integer> DATA_TYPES = new HashMap<>();
    private Map<Class, List<IDataMerger>> MERGERS = new HashMap<>();
    private Map<Class, IDataFactory> FACTORIES = new HashMap<>();


    private List<IDataWatcher> addedWatchers = new ArrayList<>();
    private List<IDataWatcher> removedWatchers = new ArrayList<>();
    //private Map<InfoUUID, IDataWatcher> LOADED_WATCHERS = new HashMap<>();
    //private Map<IDataSource, DataGeneratorHolder> DATA_MERGERS = new HashMap<>();

    private List<IDataSourceList> DATA_SOURCE_LISTS = new ArrayList<>();
    private Map<IDataSource, SourceHolder> DATA_SOURCES = new HashMap<>();
    private List<DataMergerHolder> DATA_MERGERS = new ArrayList<>();

    public static DataManager instance(){
        return PL3.proxy.getDataManager();
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
            sourceHolder = new SourceHolder(source, source.getEnvironment());
            DATA_SOURCES.putIfAbsent(source, sourceHolder);
            /*
            MethodRegistry.tileEntityFunction.stream().filter(f -> f.canInvoke(env)).forEach(f -> dataHolder.holders.put(f, new DataHolder(20)));
            MethodRegistry.blockFunction.stream().filter(f -> f.canInvoke(env)).forEach(f -> dataHolder.holders.put(f, new DataHolder(20)));
            MethodRegistry.worldFunction.stream().filter(f -> f.canInvoke(env)).forEach(f -> dataHolder.holders.put(f, new DataHolder(20)));
            */
        }
        return sourceHolder;
    }

    public DataHolder getOrCreateDataHolder(IDataSource source, IMethod method, int tickRate){
        SourceHolder holder = getOrCreateSourceHolder(source);
        DataHolder dataHolder = holder.holders.get(method);

        if(dataHolder == null){
            dataHolder = new DataHolder(tickRate);
            dataHolder.sourceIncompatible = !method.canInvoke(source.getEnvironment());
            dataHolder.data = method.getDataFactory().create();
            dataHolder.forceUpdate = true;
            holder.holders.put(method, dataHolder);
        }
        return dataHolder;
    }

    public DataMergerHolder createDataMerger(IDataSourceList sourceList, IDataMerger merger, int tickRate){
        DataMergerHolder generatorHolder = new DataMergerHolder(merger, tickRate);
        IMethod method = merger.getDataMethod();

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

    public void onSourceChanged(IDataSource source){
        SourceHolder methodHolder = DATA_SOURCES.get(source);
        if(methodHolder != null){
            for(Map.Entry<IMethod, DataHolder> entry : methodHolder.holders.entrySet()){
                if(!entry.getKey().canInvoke(source.getEnvironment())){
                    entry.getValue().sourceIncompatible = true;

                    entry.getValue().data.preUpdate();
                    entry.getValue().data = entry.getKey().getDataFactory().create();
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
        addedWatchers.forEach(watcher -> watcher.getDataHolders().stream().filter(Objects::nonNull).forEach(h -> h.addWatcher(watcher)));
        removedWatchers.forEach(watcher -> watcher.getDataHolders().stream().filter(Objects::nonNull).forEach(h -> h.removeWatcher(watcher)));

        addedWatchers.clear();
        removedWatchers.clear();
    }

    public void flushUpdates(){
        DATA_SOURCES.values().forEach(SourceHolder::updateData);
        DATA_MERGERS.forEach(DataMergerHolder::doTick);
    }

    public void constructingPhase(){
        DataManager.instance().flushWatchers();
        DataManager.instance().flushUpdates();
    }

    public void updatingPhase(){
        //TODO ?
    }

    public void notifyingPhase(){
        ////FIXME SEND DATA CHANGES
    }





    ////DATA FACTORIES

    @Nonnull
    public static IDataFactory getFactory(Class dataType){
        if(IData.class.isAssignableFrom(dataType)){
            return getFactoryForData(dataType);
        }else{
            return getFactoryForPrimitive(dataType);
        }
    }

    @Nonnull
    public static <D extends IData> IDataFactory<D> getFactoryForData(Class<D> dataType){
        IDataFactory factory = instance().FACTORIES.get(dataType);
        if(factory == null){
            throw new NullPointerException("NO DATA FACTORY FOR: " + dataType);
        }
        return factory;
    }


    @Nonnull
    public static IDataFactory getFactoryForPrimitive(Class returnType){
        Optional<IDataFactory> factory = instance().FACTORIES.values().stream().filter(f -> f.canConvert(returnType)).findFirst();
        if(!factory.isPresent()){
            throw new NullPointerException("NO DATA FACTORY FOR: " + returnType);
        }
        return factory.get();
    }



    ////REGISTERING

    public void register(){
        DATA_TYPES.put(InventoryData.class, 0);
        MERGERS.computeIfAbsent(InventoryData.class, (c) -> new ArrayList<>()).add(new InventoryDataMerger());
        FACTORIES.put(InventoryData.class, new InventoryDataFactory());

        DATA_TYPES.put(EnergyStorageData.class, 1);
        //MERGERS.computeIfAbsent(EnergyData.class, (c) -> new ArrayList<>()).add(new EnergyDataGenerator());
        FACTORIES.put(EnergyStorageData.class, new EnergyStorageDataFactory());

        DATA_TYPES.put(PrimitiveDataTypes.BooleanData.class, 2);
        FACTORIES.put(PrimitiveDataTypes.BooleanData.class, new PrimitiveDataTypes.BooleanDataFactory());

        DATA_TYPES.put(PrimitiveDataTypes.IntegerData.class, 3);
        FACTORIES.put(PrimitiveDataTypes.IntegerData.class, new PrimitiveDataTypes.IntegerDataFactory());

        DATA_TYPES.put(PrimitiveDataTypes.LongData.class, 4);
        FACTORIES.put(PrimitiveDataTypes.LongData.class, new PrimitiveDataTypes.LongDataFactory());

        DATA_TYPES.put(PrimitiveDataTypes.DoubleData.class, 5);
        FACTORIES.put(PrimitiveDataTypes.DoubleData.class, new PrimitiveDataTypes.DoubleDataFactory());

        DATA_TYPES.put(PrimitiveDataTypes.FloatData.class, 6);
        FACTORIES.put(PrimitiveDataTypes.FloatData.class, new PrimitiveDataTypes.FloatDataFactory());

        DATA_TYPES.put(SpecialDataTypes.StringData.class, 7);
        FACTORIES.put(SpecialDataTypes.StringData.class, new SpecialDataTypes.StringDataFactory());

        DATA_TYPES.put(SpecialDataTypes.ItemStackData.class, 8);
        FACTORIES.put(SpecialDataTypes.ItemStackData.class, new SpecialDataTypes.ItemStackDataFactory());

        DATA_TYPES.put(SpecialDataTypes.NBTData.class, 9);
        FACTORIES.put(SpecialDataTypes.NBTData.class, new SpecialDataTypes.NBTDataFactory());

        DATA_TYPES.put(ItemHandlerData.class, 10);
        FACTORIES.put(ItemHandlerData.class, new ItemHandlerDataFactory());

        DATA_TYPES.put(FluidHandlerData.class, 11);
        FACTORIES.put(FluidHandlerData.class, new FluidHandlerDataFactory());

        DATA_TYPES.put(StoredItemData.class, 12);
        MERGERS.computeIfAbsent(StoredItemData.class, (c) -> new ArrayList<>()).add(new StoredItemDataMerger(ItemStack.EMPTY)); //FIXME MERGERS NEED INPUTS!
        FACTORIES.put(StoredItemData.class, new StoredItemDataFactory());

    }

    public void unregister(){
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
    */
}
