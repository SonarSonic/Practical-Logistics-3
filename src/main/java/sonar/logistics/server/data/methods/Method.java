package sonar.logistics.server.data.methods;

import net.minecraft.util.ResourceLocation;
import sonar.logistics.server.data.DataRegistry;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.data.api.IDataSource;

import java.util.function.Function;

public class Method {

    private final String identifier;
    private final DataRegistry.DataType dataType;
    private final Function<IDataSource, Boolean> canInvoke;
    private final Function<IDataSource, ?> invoke;

    public Method(String identifier, DataRegistry.DataType dataType, Function<IDataSource, Boolean> canInvoke, Function<IDataSource, ?> invoke){
        this.identifier = identifier;
        this.dataType = dataType;
        this.canInvoke = canInvoke;
        this.invoke = invoke;
    }

    public String getIdentifier() {
        return identifier;
    }

    public DataRegistry.DataType getDataType() {
        return dataType;
    }

    public IDataFactory<IData> getDataFactory(){
        return dataType.factory;
    }

    public boolean canInvoke(IDataSource source){
        return canInvoke.apply(source);
    }

    public Object invoke(IDataSource source){
        return invoke.apply(source);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Method){
            Method method = (Method) obj;
            return method.identifier.equals(identifier);
        }
        return super.equals(obj);
    }
}