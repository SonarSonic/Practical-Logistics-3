package sonar.logistics.common.multiparts.displays;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import sonar.logistics.common.multiparts.base.IMultipartBlock;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.OrientatedMultipart;

public class AbstractScreenBlock extends OrientatedMultipart implements IMultipartBlock {

    public AbstractScreenBlock(Properties props, VoxelShape[] rotatedVoxels) {
        super(props, rotatedVoxels);
    }

    @Override
    public ActionResultType onMultipartActivated(MultipartEntry entry, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        return ActionResultType.SUCCESS;
    }
}
