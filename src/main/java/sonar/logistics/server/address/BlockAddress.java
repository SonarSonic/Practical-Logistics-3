package sonar.logistics.server.address;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import sonar.logistics.util.network.EnumSyncType;

import java.util.Objects;

public class BlockAddress extends Address {
    public BlockPos blockPos;
    public DimensionType dimension;
    public Direction direction;

    public BlockAddress() {}

    public BlockAddress(long blockPos, int dimension, int direction) {
        this(BlockPos.fromLong(blockPos), DimensionType.getById(dimension), Direction.values()[direction]);
    }

    public BlockAddress(BlockPos blockPos, DimensionType dimension, Direction direction) {
        this.blockPos = blockPos;
        this.dimension = dimension;
        this.direction = direction;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public DimensionType getDimensionType() {
        return dimension;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void read(PacketBuffer buffer) {
        blockPos = BlockPos.fromLong(buffer.readLong());
        dimension = DimensionType.getById(buffer.readInt());
        direction = Direction.values()[buffer.readInt()];
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLong(blockPos.toLong());
        buffer.writeInt(dimension.getId());
        buffer.writeInt(direction.ordinal());
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        blockPos = BlockPos.fromLong(nbt.getLong("pos"));
        dimension = DimensionType.getById(nbt.getInt("dim"));
        direction = Direction.values()[nbt.getInt("dir")];
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        nbt.putLong("pos", blockPos.toLong());
        nbt.putInt("dim", dimension.getId());
        nbt.putInt("dir", direction.ordinal());
        return nbt;
    }

    @Override
    public String getRegistryName() {
        return "block";
    }

    @Override
    public boolean updateEnvironment(Environment environment) {

        environment.reset();

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        environment.world = DimensionManager.getWorld(server, getDimensionType(), false, false);
        if(environment.world != null){
            environment.pos = getBlockPos();
            if(environment.world.isBlockPresent(environment.pos)){
                environment.state = environment.world.getBlockState(environment.pos);
                environment.face = getDirection();
                environment.tile = environment.world.getTileEntity(environment.pos);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BlockAddress){
            BlockAddress blockAddress = (BlockAddress) obj;
            return Objects.equals(blockAddress.blockPos, blockPos) && Objects.equals(blockAddress.dimension, dimension) && Objects.equals(blockAddress.direction, direction);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName(), blockPos.toLong(), dimension.getId(), direction.ordinal());
    }

    @Override
    public String toString() {
        return String.format("X: %s, Y: %s, Z: %s, Dim: %s, Dir: %s ", blockPos.getX(), blockPos.getY(), blockPos.getZ(), dimension.getId(), direction);
    }
}
