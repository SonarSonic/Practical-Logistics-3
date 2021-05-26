package sonar.logistics.server.data.types;

import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.util.registry.IRegistryObject;
import sonar.logistics.util.registry.Registries;

public class DataType implements IRegistryObject {
    public String registryName;
    public SubType subType;
    public Class<IData> dataType;
    public IDataFactory<IData> factory;

    public DataType(SubType subType, String registryName, Class<? extends IData> dataType, IDataFactory<? extends IData> factory) {
        this.registryName = registryName;
        this.subType = subType;
        this.dataType = (Class<IData>) dataType;
        this.factory = (IDataFactory<IData>) factory;
    }

    @Override
    public int hashCode() {
        return registryName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataType) {
            return ((DataType) obj).registryName.equals(registryName);
        }
        return super.equals(obj);
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }


    public static DataType getDataType(Class dataType){
        if(IData.class.isAssignableFrom(dataType)){
            return Registries.INSTANCE.getDataTypeRegistry().registry.values().stream().filter(type -> type.dataType.isAssignableFrom(dataType)).findFirst().orElseThrow(() -> new NullPointerException("Invalid Special Data Type: " + dataType));
        }
        return Registries.INSTANCE.getDataTypeRegistry().registry.values().stream().filter(type -> type.factory.canConvert(dataType)).findFirst().orElseThrow(() -> new NullPointerException("Invalid Primitive Data Type: " + dataType));
    }
}
