package sonar.logistics.multiparts.base;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import sonar.logistics.blocks.base.OrientatedBlock;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;
import sonar.logistics.utils.PL3Properties;

public abstract class OrientatedMultipart extends OrientatedBlock implements IMultipartBlock {

    public final VoxelShape[] rotatedVoxels;

    public OrientatedMultipart(Properties props, VoxelShape[] rotatedVoxels) {
        super(props);
        this.rotatedVoxels = rotatedVoxels;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return rotatedVoxels[getOrientation(state).ordinal()];
    }

    @Override
    public EnumMultipartSlot getMultipartSlotFromState(BlockState state) {
        return EnumMultipartSlot.fromDirection(state.get(PL3Properties.ORIENTATION));
    }

    @Override
    public BlockState getMultipartStateFromSlot(EnumMultipartSlot slot) {
        return getDefaultState().with(PL3Properties.ORIENTATION, slot.getDirection());
    }

}
