package sonar.logistics.server.cables;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public interface ICableHelper {

    boolean canConnectCables(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir, EnumCableTypes cableType);

    boolean canRenderCables(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir, EnumCableTypes cableType);

}
