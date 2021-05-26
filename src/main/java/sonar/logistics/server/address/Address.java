package sonar.logistics.server.address;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.util.network.IByteBufSaveable;
import sonar.logistics.util.network.INBTSyncable;
import sonar.logistics.util.registry.IRegistryObject;

/**
 * A reference to a data source which will always be the same in different versions
 */
public abstract class Address implements IRegistryObject, INBTSyncable, IByteBufSaveable {

    public static DataAddress createDataAddress(Address source, Method method){
        return new DataAddress(source, method);
    }

    public static BlockAddress createBlockAddress(BlockPos blockPos, DimensionType dimension, Direction direction){
        return new BlockAddress(blockPos, dimension, direction);
    }

    public static NetworkAddress createNetworkAddress(int networkID){
        return new NetworkAddress(networkID);
    }

    public static IdentityAddress createIdentityAddress(int identity){
        return new IdentityAddress(identity);
    }

    public abstract boolean updateEnvironment(Environment environment);

    //// COMPARING METHODS \\\\

    @Override
    public abstract String toString();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}
