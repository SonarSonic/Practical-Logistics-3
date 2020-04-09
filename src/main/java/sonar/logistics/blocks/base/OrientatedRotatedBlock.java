package sonar.logistics.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import sonar.logistics.utils.PL3Properties;

import javax.annotation.Nullable;

public class OrientatedRotatedBlock extends Block {

    public OrientatedRotatedBlock(Properties props) {
        super(props);
        setDefaultContainer();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext itemUse) {
        Direction facing = itemUse.getFace();
        Direction rotation = Direction.NORTH;
        if (itemUse.getPlayer().rotationPitch > 75 || itemUse.getPlayer().rotationPitch < -75) {
            rotation = itemUse.getPlayer().getHorizontalFacing().getOpposite();
        } else {
            facing = itemUse.getPlayer().getHorizontalFacing().getOpposite();
        }
        return this.getDefaultState().with(PL3Properties.ORIENTATION, facing).with(PL3Properties.ROTATION, rotation);
    }

    public Direction getOrientation(BlockState state) {
        return state.get(PL3Properties.ORIENTATION);
    }

    public Direction getRotation(BlockState state) {
        return state.get(PL3Properties.ROTATION);
    }

    public void setDefaultContainer(){
        setDefaultState(this.stateContainer.getBaseState().with(PL3Properties.ORIENTATION, Direction.UP).with(PL3Properties.ROTATION, Direction.NORTH));
    }

    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PL3Properties.ORIENTATION, PL3Properties.ROTATION);
    }

}