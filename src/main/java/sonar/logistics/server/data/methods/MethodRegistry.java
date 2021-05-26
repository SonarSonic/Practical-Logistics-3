package sonar.logistics.server.data.methods;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.address.Environment;
import sonar.logistics.server.data.types.DataType;
import sonar.logistics.util.registry.Registries;
import sonar.logistics.util.registry.types.StringRegistry;

import java.util.function.BiFunction;
import java.util.function.Function;

public class MethodRegistry extends StringRegistry<Method> {

    public Method getMethodFromIdentifier(String identifier){
        return registry.get(identifier);
    }

    public <O, T extends TileEntity> Method registerTileEntityMethod(MethodCategory category, String name, Class<O> returnType, Class<T> tileEntity, BiFunction<Environment, T, O> method){
        return registerMethod(category, name, returnType, source -> source.tile() != null && tileEntity.isInstance(source.tile()), source -> method.apply(source, (T)source.tile()));
    }

    public <O, B extends Block> Method registerBlockMethod(MethodCategory category, String name, Class<O> returnType, Class<B> block, BiFunction<Environment, B, O> method){
        return registerMethod(category, name, returnType, source -> source.state() != null && block.isInstance(source.state().getBlock()), source -> method.apply(source, (B)source.state().getBlock()));
    }

    public <O, E extends Entity> Method registerEntityMethod(MethodCategory category, String name, Class<O> returnType, Class<E> entity, BiFunction<Environment, E, O> method){
        return registerMethod(category, name, returnType, source -> source.entity() != null && entity.isInstance(source.entity()), source -> method.apply(source, (E)source.entity()));
    }

    public <O> Method registerWorldMethod(MethodCategory category, String name, Class<O> returnType, Function<World, O> method){
        return registerMethod(category, name, returnType, source -> source.world() != null, source -> method.apply(source.world()));
    }

    public <O> Method registerNetworkMethod(MethodCategory category, String name, Class<O> returnType, Function<PL3Network, O> method){
        return registerMethod(category, name, returnType, source -> source.network() != null, source -> method.apply(source.network()));
    }

    public <O> Method registerMethod(MethodCategory category, String name, Class<O> returnType, Function<Environment, Boolean> canInvoke, Function<Environment, O> invoke){
        DataType dataType = DataType.getDataType(returnType);
        Method method = new Method(category, name, dataType, canInvoke, invoke);

        if(registry.containsKey(method.getRegistryName())){
            PL3.LOGGER.error("Duplicate Method: {}", method.getRegistryName());
            return null;
        }

        registry.put(method.getRegistryName(), method);
        PL3.LOGGER.info("Registered Method: {}, Return Type {}", method.getRegistryName(), returnType.getSimpleName());
        return method;
    }

}