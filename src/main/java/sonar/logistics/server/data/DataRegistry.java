package sonar.logistics.server.data;

import imgui.ImColor;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.data.types.energy.EnergyStorageData;
import sonar.logistics.server.data.types.energy.EnergyStorageDataFactory;
import sonar.logistics.server.data.types.sources.SourceData;
import sonar.logistics.server.data.types.sources.SourceDataFactory;
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

import java.util.*;

/**
 * For registering data types
 */
public class DataRegistry {

    public static final DataRegistry INSTANCE = new DataRegistry();

    public final List<DataType> DATA_TYPES = new ArrayList<>();

    public void registerDataType(Integer typeID, SubType subType, String displayName, Class<? extends IData> type, IDataFactory<? extends IData> factory){
        DATA_TYPES.add(new DataType(typeID, subType, displayName, type, factory));
    }

    public void init(){
        int typeID = 0;
        registerDataType(typeID++, SubType.OBJECT,"inventory", InventoryData.class, new InventoryDataFactory());
        registerDataType(typeID++, SubType.OBJECT,"energy", EnergyStorageData.class, new EnergyStorageDataFactory());
        registerDataType(typeID++, SubType.BOOLEAN, "boolean", PrimitiveDataTypes.BooleanData.class, new PrimitiveDataTypes.BooleanDataFactory());
        registerDataType(typeID++, SubType.NUMBER,"integer", PrimitiveDataTypes.IntegerData.class, new PrimitiveDataTypes.IntegerDataFactory());
        registerDataType(typeID++, SubType.NUMBER,"long", PrimitiveDataTypes.LongData.class, new PrimitiveDataTypes.LongDataFactory());
        registerDataType(typeID++, SubType.NUMBER,"double", PrimitiveDataTypes.DoubleData.class, new PrimitiveDataTypes.DoubleDataFactory());
        registerDataType(typeID++, SubType.NUMBER,"float", PrimitiveDataTypes.FloatData.class, new PrimitiveDataTypes.FloatDataFactory());
        registerDataType(typeID++, SubType.TEXT,"string", SpecialDataTypes.StringData.class, new SpecialDataTypes.StringDataFactory());
        registerDataType(typeID++, SubType.OBJECT,"itemstack", SpecialDataTypes.ItemStackData.class, new SpecialDataTypes.ItemStackDataFactory());
        registerDataType(typeID++, SubType.OBJECT, "nbt", SpecialDataTypes.NBTData.class, new SpecialDataTypes.NBTDataFactory());
        registerDataType(typeID++, SubType.REFERENCE,"item handler", ItemHandlerData.class, new ItemHandlerDataFactory());
        registerDataType(typeID++, SubType.REFERENCE,"fluid handler", FluidHandlerData.class, new FluidHandlerDataFactory());
        registerDataType(typeID++, SubType.REFERENCE,"big itemstack", StoredItemData.class, new StoredItemDataFactory());
        registerDataType(typeID++, SubType.REFERENCE,"source", SourceData.class, new SourceDataFactory());

        //MERGERS.computeIfAbsent(InventoryData.class, (c) -> new ArrayList<>()).add(new InventoryDataMerger());
        //MERGERS.computeIfAbsent(EnergyData.class, (c) -> new ArrayList<>()).add(new EnergyDataGenerator());
        //MERGERS.computeIfAbsent(StoredItemData.class, (c) -> new ArrayList<>()).add(new StoredItemDataMerger(ItemStack.EMPTY)); //FIXME MERGERS NEED INPUTS!

    }

    ////DATA FACTORIES

    /*
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
        IDataFactory factory = INSTANCE.FACTORIES.get(dataType);
        if(factory == null){
            throw new NullPointerException("NO DATA FACTORY FOR: " + dataType);
        }
        return factory;
    }


    @Nonnull
    public static IDataFactory<?> getFactoryForPrimitive(Class<?> returnType){
        Optional<IDataFactory<?>> factory = INSTANCE.FACTORIES.values().stream().filter(f -> f.canConvert(returnType)).findFirst();
        if(!factory.isPresent()){
            throw new NullPointerException("NO DATA FACTORY FOR: " + returnType);
        }
        return factory.get();
    }
        */

    public DataType getDataType(Class dataType){
        if(IData.class.isAssignableFrom(dataType)){
            return DATA_TYPES.stream().filter(type -> type.dataType.isAssignableFrom(dataType)).findFirst().orElseThrow(() -> new NullPointerException("Invalid Special Data Type: " + dataType));
        }
        return DATA_TYPES.stream().filter(type -> type.factory.canConvert(dataType)).findFirst().orElseThrow(() -> new NullPointerException("Invalid Primitive Data Type: " + dataType));
    }

    public DataType getDataType(int id){
        return DATA_TYPES.stream().filter(type -> type.id == id).findFirst().orElseThrow(() -> new NullPointerException("No Data Type with ID: " + id));
    }

    public enum SubType{
        BOOLEAN,
        NUMBER,
        TEXT,
        OBJECT,
        REFERENCE;

        public int getSubTypeColour(){
            switch (this){
                case BOOLEAN:
                    return ImColor.intToColor(76, 83, 245);
                case NUMBER:
                    return ImColor.intToColor(245, 138, 88);
                case TEXT:
                    return ImColor.intToColor(53, 165, 245);
                case OBJECT:
                    return ImColor.intToColor(245, 204, 39);
                case REFERENCE:
                    return ImColor.intToColor(51, 245, 225);
            }
            return -1;
        }

    }

    public static class DataType{
        public int id;
        public SubType subType;
        public String displayName;
        public Class<IData> dataType;
        public IDataFactory<IData> factory;

        public DataType(int id, SubType subType, String displayName, Class<? extends IData> dataType, IDataFactory<? extends IData> factory){
            this.id = id;
            this.subType = subType;
            this.displayName = displayName;
            this.dataType = (Class<IData>) dataType;
            this.factory = (IDataFactory<IData>) factory;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof DataType){
                return ((DataType) obj).id == id;
            }
            return super.equals(obj);
        }
    }
}
