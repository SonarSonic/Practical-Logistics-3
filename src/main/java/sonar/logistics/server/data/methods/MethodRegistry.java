package sonar.logistics.server.data.methods;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.data.DataRegistry;
import sonar.logistics.server.data.api.IDataSource;
import sonar.logistics.server.data.methods.categories.DataCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MethodRegistry {

    public static Map<String, Method> methods = new HashMap<>();

    public static void init(){
        VanillaMethods.init();
    }

    public static Method getMethodFromIdentifier(String identifier){
        return methods.get(identifier);
    }

    public static <O, T extends TileEntity> Method registerTileEntityMethod(DataCategory category, String name, Class<O> returnType, Class<T> tileEntity, BiFunction<IDataSource, T, O> method){
        return registerMethod(category, name, returnType, source -> source.tile() != null && tileEntity.isInstance(source.tile()), source -> method.apply(source, (T)source.tile()));
    }

    public static <O, B extends Block> Method registerBlockMethod(DataCategory category, String name, Class<O> returnType, Class<B> block, BiFunction<IDataSource, B, O> method){
        return registerMethod(category, name, returnType, source -> source.state() != null && block.isInstance(source.state().getBlock()), source -> method.apply(source, (B)source.state().getBlock()));
    }

    public static <O, E extends Entity> Method registerEntityMethod(DataCategory category, String name, Class<O> returnType, Class<E> entity, BiFunction<IDataSource, E, O> method){
        return registerMethod(category, name, returnType, source -> source.entity() != null && entity.isInstance(source.entity()), source -> method.apply(source, (E)source.entity()));
    }

    public static <O> Method registerWorldMethod(DataCategory category, String name, Class<O> returnType, Function<World, O> method){
        return registerMethod(category, name, returnType, source -> source.world() != null, source -> method.apply(source.world()));
    }

    public static <O> Method registerNetworkMethod(DataCategory category, String name, Class<O> returnType, Function<PL3Network, O> method){
        return registerMethod(category, name, returnType, source -> source.network() != null, source -> method.apply(source.network()));
    }

    public static <O> Method registerMethod(DataCategory category, String name, Class<O> returnType, Function<IDataSource, Boolean> canInvoke, Function<IDataSource, O> invoke){
        String identifier = category.getID() + ":" + name;
        if(methods.containsKey(identifier)){
            PL3.LOGGER.error("Duplicate Method: {}", identifier);
            return null;
        }

        DataRegistry.DataType dataType = DataRegistry.INSTANCE.getDataType(returnType);

        Method method = new Method(identifier, dataType, canInvoke, invoke);
        methods.put(identifier, method);
        PL3.LOGGER.info("Registered Method: {}, Return Type {}", identifier, returnType.getSimpleName());
        return method;
    }

}