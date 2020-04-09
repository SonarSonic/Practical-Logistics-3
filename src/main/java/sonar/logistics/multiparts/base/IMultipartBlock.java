package sonar.logistics.multiparts.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import sonar.logistics.blocks.host.MultipartHostTile;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;

/**implemented on Blocks which can become Multiparts - internal PL3 use only at the moment*/
public interface IMultipartBlock {

    default Block getDefaultBlock(){
        return (Block)this;
    }

    default ResourceLocation getMultipartRegistryName(){
        return getDefaultBlock().getRegistryName();
    }

    EnumMultipartSlot getMultipartSlotFromState(BlockState state);

    BlockState getMultipartStateFromSlot(EnumMultipartSlot slot);

    default BlockState getMultipartRenderState(MultipartHostTile host, MultipartEntry entry){
        return getMultipartStateFromSlot(entry.slot);
    }

    //// TILE \\\\

    default void onPlaced(World world, BlockPos pos){}

    default void onDestroyed(World world, BlockPos pos){}

    default void onMultipartAdded(MultipartHostTile host, MultipartEntry entry, MultipartEntry added){}

    default void onMultipartRemoved(MultipartHostTile host, MultipartEntry entry, MultipartEntry removed){}

    default boolean hasMultipartTile(MultipartEntry entry){
        return false;
    }

    default MultipartTile createMultipartTile(MultipartEntry entry){
        return null;
    }


    //// CABLE METHODS \\\\

    /**only for cables!*/
    default boolean requiresMultipartHost(){
        return true;
    }

    /**only for cables!
     * @return if the block has replaced the MultipartHostTile*/
    default boolean onHostRemoved(MultipartEntry entry){
         return false;
    }


    //// BLOCK METHODS \\\\

    default void onNeighborChange(MultipartEntry entry, IWorldReader world, BlockPos pos, BlockPos neighbor){
        entry.getBlockState().onNeighborChange(world, pos, neighbor);
    }

    default void neighborChanged(MultipartEntry entry, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
        entry.getBlockState().neighborChanged(world, pos, block, fromPos, isMoving);
    }
}