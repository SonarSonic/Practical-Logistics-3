package sonar.logistics.util.registry;

import sonar.logistics.server.address.*;
import sonar.logistics.server.data.types.SubType;
import sonar.logistics.server.data.types.DataType;
import sonar.logistics.server.data.methods.MethodRegistry;
import sonar.logistics.server.data.methods.VanillaMethods;
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
import sonar.logistics.server.data.types.sources.SourceData;
import sonar.logistics.server.data.types.sources.SourceDataFactory;
import sonar.logistics.util.registry.types.HashRegistry;
import sonar.logistics.util.registry.types.SyncableRegistry;

public class Registries {

    public static final Registries INSTANCE = new Registries();

    private final SyncableRegistry<? extends Address> NODE_REGISTRY = new SyncableRegistry<>();
    private final SyncableRegistry<? extends Address> ADDRESS_TYPES = new SyncableRegistry<>();
    private final HashRegistry<DataType> DATA_TYPES = new HashRegistry<>();
    private final MethodRegistry METHODS = new MethodRegistry();

    public void init(){
        ADDRESS_TYPES.register("data", DataAddress.class);
        ADDRESS_TYPES.register("block", BlockAddress.class);
        ADDRESS_TYPES.register("identity", IdentityAddress.class);
        ADDRESS_TYPES.register("invalid", InvalidAddress.class);
        ADDRESS_TYPES.register("network", NetworkAddress.class);
        ADDRESS_TYPES.register("node pin", PinOutputAddress.class);

        DATA_TYPES.register(new DataType(SubType.OBJECT,"inventory", InventoryData.class, new InventoryDataFactory()));
        DATA_TYPES.register(new DataType(SubType.OBJECT,"energy", EnergyStorageData.class, new EnergyStorageDataFactory()));
        DATA_TYPES.register(new DataType(SubType.BOOLEAN, "boolean", PrimitiveDataTypes.BooleanData.class, new PrimitiveDataTypes.BooleanDataFactory()));
        DATA_TYPES.register(new DataType(SubType.NUMBER,"integer", PrimitiveDataTypes.IntegerData.class, new PrimitiveDataTypes.IntegerDataFactory()));
        DATA_TYPES.register(new DataType(SubType.NUMBER,"long", PrimitiveDataTypes.LongData.class, new PrimitiveDataTypes.LongDataFactory()));
        DATA_TYPES.register(new DataType(SubType.NUMBER,"double", PrimitiveDataTypes.DoubleData.class, new PrimitiveDataTypes.DoubleDataFactory()));
        DATA_TYPES.register(new DataType(SubType.NUMBER,"float", PrimitiveDataTypes.FloatData.class, new PrimitiveDataTypes.FloatDataFactory()));
        DATA_TYPES.register(new DataType(SubType.TEXT,"string", SpecialDataTypes.StringData.class, new SpecialDataTypes.StringDataFactory()));
        DATA_TYPES.register(new DataType(SubType.OBJECT,"itemstack", SpecialDataTypes.ItemStackData.class, new SpecialDataTypes.ItemStackDataFactory()));
        DATA_TYPES.register(new DataType(SubType.OBJECT, "nbt", SpecialDataTypes.NBTData.class, new SpecialDataTypes.NBTDataFactory()));
        DATA_TYPES.register(new DataType(SubType.REFERENCE,"item handler", ItemHandlerData.class, new ItemHandlerDataFactory()));
        DATA_TYPES.register(new DataType(SubType.REFERENCE,"fluid handler", FluidHandlerData.class, new FluidHandlerDataFactory()));
        DATA_TYPES.register(new DataType(SubType.REFERENCE,"big itemstack", StoredItemData.class, new StoredItemDataFactory()));
        DATA_TYPES.register(new DataType(SubType.REFERENCE,"source", SourceData.class, new SourceDataFactory()));

        VanillaMethods.init();
    }

    public static SyncableRegistry<? extends Address> getAddressRegistry(){
        return INSTANCE.ADDRESS_TYPES;
    }

    public static HashRegistry<DataType> getDataTypeRegistry(){
        return INSTANCE.DATA_TYPES;
    }

    public static MethodRegistry getMethodRegistry(){
        return INSTANCE.METHODS;
    }

    public void clear() {
        ADDRESS_TYPES.clear();
    }
}
