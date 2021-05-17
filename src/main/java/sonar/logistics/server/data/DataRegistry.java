package sonar.logistics.server.data;

import net.minecraft.item.ItemStack;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.data.api.IDataMerger;
import sonar.logistics.server.data.mergers.InventoryDataMerger;
import sonar.logistics.server.data.mergers.StoredItemDataMerger;
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

public class DataRegistry {

    public static final DataRegistry INSTANCE = new DataRegistry();

    public final Map<Class<?>, Integer> DATA_TYPES = new HashMap<>();
    public final Map<Class<?>, List<IDataMerger<?>>> MERGERS = new HashMap<>();
    public final Map<Class<?>, IDataFactory<?>> FACTORIES = new HashMap<>();

    public void init(){
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
}
