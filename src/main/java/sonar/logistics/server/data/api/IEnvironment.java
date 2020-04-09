package sonar.logistics.server.data.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEnvironment {

    World world();

    BlockState state();

    BlockPos pos();

    Direction face();

    TileEntity tile();

    default Entity entity(){
        return null; //FIXME ENTITY SYSTEM
    }

}
