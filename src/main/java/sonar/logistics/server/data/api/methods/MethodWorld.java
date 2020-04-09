package sonar.logistics.server.data.api.methods;

import net.minecraft.util.ResourceLocation;
import sonar.logistics.server.data.api.IEnvironment;
import sonar.logistics.server.data.api.methods.types.IMethodWorld;

public class MethodWorld<O> extends MethodAbstract<O> {

    private IMethodWorld<O> method;

    public MethodWorld(ResourceLocation identifier, Class<O> returnType, IMethodWorld<O> method) {
        super(identifier, returnType);
        this.method = method;
    }

    @Override
    public boolean canInvoke(IEnvironment environment){
        return environment != null && environment.world() != null;
    }

    @Override
    public O invoke(IEnvironment environment){
        return method.invoke(environment.world());
    }
}
