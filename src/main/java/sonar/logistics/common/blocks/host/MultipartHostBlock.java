package sonar.logistics.common.blocks.host;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.common.multiparts.base.IMultipartBlock;
import sonar.logistics.common.multiparts.base.MultipartEntry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class MultipartHostBlock extends Block {

    public MultipartHostBlock() {
        super(Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0f).doesNotBlockMovement());
        setDefaultState(this.stateContainer.getBaseState());
        setRegistryName("multipart_host");
    }

    //// RAY TRACING & SHAPES \\\\

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        MultipartEntry part = MultipartHostHelper.getRayTraceMultipart(reader, pos, MultipartHostHelper.getEntityDangerously(reader, context));
        return part != null ? part.getBlockState().getShape(reader, pos, context) :  VoxelShapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return MultipartHostHelper.getMultipartHostCollisionVoxel(reader, pos);
    }

    @Nullable
    @Override
    public RayTraceResult getRayTraceResult(BlockState state, World world, BlockPos pos, Vec3d start, Vec3d end, RayTraceResult original) {
        return MultipartHostHelper.getRayTraceResult(world, pos, start, end);
    }

    //// TILE ENTITY \\\\

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new NetworkedHostTile();
    }


    //// RENDERING \\\\

    @Override
    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        MultipartEntry entry = MultipartHostHelper.getRayTraceMultipart(world, pos, PL3.proxy.getClientPlayer());
        if(entry != null) {
            manager.addBlockDestroyEffects(pos, entry.getBlockState());
            return true;
        }
        return false;
    }

    @Override
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        if(target instanceof BlockRayTraceResult && target.hitInfo instanceof MultipartEntry){
            manager.addBlockHitEffects(((BlockRayTraceResult) target).getPos(), ((BlockRayTraceResult) target).getFace());
            return true;
        }
        return true;
    }

    //// MULTIPARTS \\\\

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        MultipartEntry entry = MultipartHostHelper.getRayTraceMultipart(world, pos, PL3.proxy.getClientPlayer());
        if(entry != null) {
            return entry.getBlockState().getPickBlock(target, world, pos, player);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        MultipartEntry entry = MultipartHostHelper.getRayTraceMultipart(world, pos, player);
        if(entry != null){
            return entry.multipart.onMultipartActivated(entry, world, pos, player, hand, rayTraceResult);
        }
        return ActionResultType.FAIL;
    }

    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockClicked(state, world, pos, player);
        MultipartEntry entry = MultipartHostHelper.getRayTraceMultipart(world, pos, player);
        if(entry != null){
            entry.getBlockState().onBlockClicked(world, pos, player);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        forMultipartEntries(world, pos, e -> e.multipart.neighborChanged(e, world, pos, block, fromPos, isMoving));
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        forMultipartEntries(world, pos, e -> e.multipart.onNeighborChange(e, world, pos, neighbor));
    }

    ///// HELPER METHODS \\\\\

    public void forMultipartEntries(IWorldReader world, BlockPos pos, Consumer<? super MultipartEntry> action){
        MultipartHostTile entry = MultipartHostHelper.getMultipartHostTile(world, pos);
        if(entry != null){
            entry.MULTIPARTS.forEach(action);
        }
    }

    public void forMultipartBlocks(IWorldReader world, BlockPos pos, Consumer<IMultipartBlock> action){
        MultipartHostTile entry = MultipartHostHelper.getMultipartHostTile(world, pos);
        if(entry != null){
            entry.MULTIPARTS.forEach(M -> action.accept(M.multipart));
        }
    }
}
