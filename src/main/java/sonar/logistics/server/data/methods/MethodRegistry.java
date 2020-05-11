package sonar.logistics.server.data.methods;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import sonar.logistics.PL3;
import sonar.logistics.server.data.api.categories.IDataCategory;
import sonar.logistics.server.data.api.methods.IMethod;
import sonar.logistics.server.data.api.methods.MethodBlock;
import sonar.logistics.server.data.api.methods.MethodTileEntity;
import sonar.logistics.server.data.api.methods.MethodWorld;
import sonar.logistics.server.data.api.methods.types.IMethodBlock;
import sonar.logistics.server.data.api.methods.types.IMethodEntity;
import sonar.logistics.server.data.api.methods.types.IMethodTileEntity;
import sonar.logistics.server.data.api.methods.types.IMethodWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MethodRegistry {

    public static List<IMethod> tileEntityFunction = new ArrayList<>();
    public static List<IMethod> blockFunction = new ArrayList<>();
    public static List<IMethod> worldFunction = new ArrayList<>();
    public static List<IMethod> specialFunction = new ArrayList<>();

    static{
        VanillaMethods.init();
        SpecialMethods.init();
    }

    public static <O, T extends TileEntity> IMethod<O> registerMethodTileEntity(IDataCategory category, String name, Class<O> returnType, Class<T> tileEntity, IMethodTileEntity<O,T> method){
        ResourceLocation identifier = new ResourceLocation(category.getID(), name);
        IMethod hashTest = hashTest(identifier.hashCode());
        if(hashTest != null){
            logHashError(hashTest.getIdentifier(), identifier);
            return null;
        }else{
            MethodTileEntity<O,T> function = new MethodTileEntity<>(identifier, returnType, tileEntity, method);
            tileEntityFunction.add(function);
            return function;
        }

    }

    public static <O, B extends Block> IMethod<O> registerMethodBlock(IDataCategory category, String name, Class<O> returnType, Class<B> block, IMethodBlock<O,B> method){
        ResourceLocation identifier = new ResourceLocation(category.getID(), name);
        IMethod hashTest = hashTest(identifier.hashCode());
        if(hashTest != null){
            logHashError(hashTest.getIdentifier(), identifier);
            return null;
        }else {
            MethodBlock<O, B> function = new MethodBlock<>(identifier, returnType, block, method);
            blockFunction.add(function);
            return function;
        }
    }


    public static <O> IMethod<O> registerMethodWorld(IDataCategory category, String name, Class<O> returnType, IMethodWorld<O> method){
            ResourceLocation identifier = new ResourceLocation(category.getID(), name);
            IMethod hashTest = hashTest(identifier.hashCode());
            if(hashTest != null){
                logHashError(hashTest.getIdentifier(), identifier);
                return null;
            }else {
                MethodWorld<O> function = new MethodWorld<>(identifier, returnType, method);
                worldFunction.add(function);
                return function;
            }
    }

    public static <O, E extends Entity> IMethod<O> registerMethodEntity(IDataCategory category, String name, Class<O> returnType, Class<E> entity, IMethodEntity<O,E> method){
        /* FIXME ENTITY SYSTEM
        ResourceLocation identifier = new ResourceLocation(domain, name);
        IFunction hashTest = hashTest(identifier.hashCode());
        if(hashTest != null){
            logHashError(hashTest.getIdentifier(), identifier);
            return null;
        }else{
            FunctionEntity<O,E> method = new FunctionEntity<>(identifier, returnType, entity, method);
            entityFunction.add(method);
            return method;
        }
        */
        return null;
    }

    /*
    public static <O> IMethod<O> registerMethodSpecial(IDataCategory category, String name, Class<O> returnType, IMethodSpecial<O> method){
        ResourceLocation identifier = new ResourceLocation(category.getID(), name);
        IMethod hashTest = hashTest(identifier.hashCode());
        if(hashTest != null){
            logHashError(hashTest.getIdentifier(), identifier);
            return null;
        }else {
            MethodSpecial<O> function = new MethodSpecial<>(identifier, returnType, method);
            specialFunction.add(function);
            return function;
        }
    }

     */

    public static IMethod hashTest(int hashCode){
        Optional<IMethod> tileFunc = tileEntityFunction.stream().filter(f -> f.getIdentifier().hashCode() == hashCode).findAny();
        Optional<IMethod> blockFunc = blockFunction.stream().filter(f -> f.getIdentifier().hashCode() == hashCode).findAny();
        Optional<IMethod> worldFunc = worldFunction.stream().filter(f -> f.getIdentifier().hashCode() == hashCode).findAny();
        Optional<IMethod> specialFunc = specialFunction.stream().filter(f -> f.getIdentifier().hashCode() == hashCode).findAny();
        return tileFunc.orElseGet(() -> blockFunc.orElseGet(() -> worldFunc.orElseGet(() -> specialFunc.orElse(null))));
    }

    public static void logHashError(ResourceLocation existing, ResourceLocation identifier){
        PL3.LOGGER.fatal("TWO METHODS HAVE MATCHING HASH CODES! Existing Method: {} New Method {}", existing, identifier);
        PL3.LOGGER.fatal("REGISTERING HAS FAILED FOR New Method {}", identifier);
    }


}
