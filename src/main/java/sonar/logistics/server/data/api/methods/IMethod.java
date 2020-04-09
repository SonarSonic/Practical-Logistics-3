package sonar.logistics.server.data.api.methods;

import net.minecraft.util.ResourceLocation;
import sonar.logistics.server.data.api.IDataFactory;
import sonar.logistics.server.data.api.IEnvironment;

public interface IMethod<O> {

    ResourceLocation getIdentifier();

    IDataFactory getDataFactory();

    Class<O> getReturnType();

    boolean canInvoke(IEnvironment environment);

    O invoke(IEnvironment environment);

}
