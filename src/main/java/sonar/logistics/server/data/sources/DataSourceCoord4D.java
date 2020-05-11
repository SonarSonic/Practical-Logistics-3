package sonar.logistics.server.data.sources;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import sonar.logistics.server.data.api.IEnvironment;

import java.util.Objects;

public class DataSourceCoord4D implements IDataSource {

    public int x, y, z;
    public DimensionType dimension;
    public Direction facing;

    public DataSourceCoord4D(BlockPos pos, DimensionType dimension, Direction facing){
        this(pos.getX(), pos.getY(), pos.getZ(), dimension, facing);
    }
    public DataSourceCoord4D(int x, int y, int z, DimensionType dimension, Direction facing){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.facing = facing;
    }

    public IEnvironment getEnvironment(){
        World world = ServerLifecycleHooks.getCurrentServer().getWorld(dimension);
        return new IEnvironment() {
            @Override
            public World world() {
                return world;
            }

            @Override
            public BlockState state() {
                return world.getBlockState(pos());
            }

            @Override
            public BlockPos pos() {
                return new BlockPos(x,y,z);
            }

            @Override
            public Direction face() {
                return facing;
            }

            @Override
            public TileEntity tile() {
                return world.getTileEntity(pos());
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DataSourceCoord4D)) {
            return false;
        }
        DataSourceCoord4D c = (DataSourceCoord4D) obj;
        return x == c.x && y == c.y && z == c.z && dimension == c.dimension && facing == c.facing;
    }

    @Override
    public int hashCode(){
        return Objects.hash(x, y, z, dimension, facing);
    }

}
