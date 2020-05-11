package sonar.logistics.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import sonar.logistics.util.PL3Properties;

import javax.annotation.Nullable;

public class OrientatedBlock extends Block {

    public OrientatedBlock(Properties props) {
        super(props);
        setDefaultContainer();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext itemUse) {
        return this.getDefaultState().with(PL3Properties.ORIENTATION, itemUse.getNearestLookingDirection().getOpposite());
    }

    public Direction getOrientation(BlockState state) {
        return state.get(PL3Properties.ORIENTATION);
    }

    public void setDefaultContainer(){
        setDefaultState(this.stateContainer.getBaseState().with(PL3Properties.ORIENTATION, Direction.UP));
    }

    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PL3Properties.ORIENTATION);
    }

}
