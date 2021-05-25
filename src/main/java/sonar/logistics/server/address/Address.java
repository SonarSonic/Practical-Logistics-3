package sonar.logistics.server.address;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.IByteBufSaveable;
import sonar.logistics.util.network.INBTSyncable;

import javax.annotation.Nonnull;

/**
 * A reference to a data source which will always be the same in different versions
 */
public abstract class Address implements INBTSyncable, IByteBufSaveable {

    public static final int INVALID_ADDRESS = -1;
    public static final int DATA_ADDRESS = 0;
    public static final int BLOCK_ADDRESS = 1;
    public static final int NETWORK_ADDRESS = 2;
    public static final int IDENTITY_ADDRESS = 3;

    @Nonnull
    public static Address ofType(int type){
        switch (type){
            case INVALID_ADDRESS:
                return InvalidAddress.INSTANCE;
            case DATA_ADDRESS:
                return new DataAddress();
            case BLOCK_ADDRESS:
                return new BlockAddress();
            case NETWORK_ADDRESS:
                return new NetworkAddress();
            case IDENTITY_ADDRESS:
                return new IdentityAddress();
        }
        throw new NullPointerException("No Address of type " + type);
    }

    public static Address fromPacketBuffer(PacketBuffer buffer){
        int type = buffer.readInt();
        Address address = Address.ofType(type);
        address.read(buffer);
        return address;
    }

    public static void toPacketBuffer(Address address, PacketBuffer buffer){
        buffer.writeInt(address.getType());
        address.write(buffer);
    }

    public static Address fromNBT(CompoundNBT nbt){
        int type = nbt.getInt("type");
        Address address = Address.ofType(type);
        address.read(nbt, EnumSyncType.SAVE);
        return address;
    }

    public static CompoundNBT toNBT(Address address, CompoundNBT nbt){
        nbt.putInt("type", address.getType());
        address.write(nbt, EnumSyncType.SAVE);
        return nbt;
    }

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

    public abstract int getType();

    public abstract boolean updateEnvironment(Environment environment);

    //// COMPARING METHODS \\\\

    @Override
    public abstract String toString();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}
