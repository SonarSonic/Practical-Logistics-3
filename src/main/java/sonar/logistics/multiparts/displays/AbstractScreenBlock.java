package sonar.logistics.multiparts.displays;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import sonar.logistics.client.gsi.api.BlockInteractionType;
import sonar.logistics.client.gsi.context.DisplayClickContext;
import sonar.logistics.multiparts.base.IMultipartBlock;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.base.MultipartTile;
import sonar.logistics.multiparts.base.OrientatedMultipart;
import sonar.logistics.multiparts.displays.api.IDisplay;

public class AbstractScreenBlock extends OrientatedMultipart implements IMultipartBlock {

    public AbstractScreenBlock(Properties props, VoxelShape[] rotatedVoxels) {
        super(props, rotatedVoxels);
    }

    @Override
    public ActionResultType onMultipartActivated(MultipartEntry entry, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(world.isRemote) {
            MultipartTile tile = entry.getMultipartTile();
            if (tile instanceof IDisplay) {
                IDisplay display = (IDisplay) tile;
                BlockInteractionType interactionType = player.isShiftKeyDown() ? BlockInteractionType.SHIFT_RIGHT : BlockInteractionType.RIGHT;
                DisplayClickContext clickContext = DisplayVectorHelper.createClickContext(interactionType, player, display);
                if(clickContext != null) {
                    return display.getGSI().onClicked(clickContext) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
                }
            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    public void onBlockClicked(BlockState p_196270_1_, World p_196270_2_, BlockPos p_196270_3_, PlayerEntity p_196270_4_) {
        super.onBlockClicked(p_196270_1_, p_196270_2_, p_196270_3_, p_196270_4_);
    }
}
