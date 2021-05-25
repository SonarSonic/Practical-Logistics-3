package sonar.logistics.server.data.methods;

import sonar.logistics.server.data.DataRegistry;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.address.Environment;

import java.util.function.Function;

public class Method {

    private final MethodCategory category;
    private final String methodName;
    private final DataRegistry.DataType dataType;
    private final Function<Environment, Boolean> canInvoke;
    private final Function<Environment, ?> invoke;

    public Method(MethodCategory category, String methodName, DataRegistry.DataType dataType, Function<Environment, Boolean> canInvoke, Function<Environment, ?> invoke){
        this.category = category;
        this.methodName = methodName;
        this.dataType = dataType;
        this.canInvoke = canInvoke;
        this.invoke = invoke;
    }

    public MethodCategory getCategory() {
        return category;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getIdentifier() {
        return category.getID() + ":" + getMethodName();
    }

    public DataRegistry.DataType getDataType() {
        return dataType;
    }

    public IDataFactory<IData> getDataFactory(){
        return dataType.factory;
    }

    public boolean canInvoke(Environment environment){
        return canInvoke.apply(environment);
    }

    public Object invoke(Environment environment){
        return invoke.apply(environment);
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Method){
            Method method = (Method) obj;
            return method.getIdentifier().equals(getIdentifier());
        }
        return super.equals(obj);
    }
}