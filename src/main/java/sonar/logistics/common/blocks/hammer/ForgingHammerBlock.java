package sonar.logistics.common.blocks.hammer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import sonar.logistics.common.blocks.PL3Blocks;

public class ForgingHammerBlock extends Block {

    public static final IntegerProperty POS = IntegerProperty.create("fhpos", 0, 2);

    public ForgingHammerBlock() {
        super(Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0f).notSolid());
        setDefaultState(this.stateContainer.getBaseState().with(POS, 0));
        setRegistryName("forging_hammer");
    }

    ////ACTION \\\\

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        BlockPos tileEntityPos = getTileEntityPos(state, pos);
        TileEntity tile = world.getTileEntity(tileEntityPos);
        if (!world.isRemote && tile instanceof ForgingHammerTile) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (ForgingHammerTile)tile, tileEntityPos);
        }
        return ActionResultType.SUCCESS;
    }

    //// CREATE \\\\

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(POS) == 0;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return hasTileEntity(state) ? new ForgingHammerTile() : null;
    }

    public BlockPos getTileEntityPos(BlockState state, BlockPos pos){
        return pos.offset(Direction.DOWN, state.get(POS));
    }

    public BlockPos getMiddlePos(BlockState state, BlockPos pos){
        switch(state.get(POS)){
            case 0:
                return pos.offset(Direction.UP);
            case 2:
                return pos.offset(Direction.DOWN);
            default:
                return pos;
        }
    }

    //// EVENTS \\\\

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader reader, BlockPos pos) {
        return reader.isAirBlock(pos.offset(Direction.UP, 1)) && reader.isAirBlock(pos.offset(Direction.UP, 2));
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, world, pos, oldState, isMoving);
        if(state.get(POS) == 0) {
            world.setBlockState(pos.offset(Direction.UP, 1), PL3Blocks.FORGING_HAMMER_BLOCK.getDefaultState().with(POS, 1));
            world.setBlockState(pos.offset(Direction.UP, 2), PL3Blocks.FORGING_HAMMER_BLOCK.getDefaultState().with(POS, 2));
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
        BlockPos base = getTileEntityPos(state, pos);
        for(int i = 0; i < 3 ; i ++){
            BlockPos targetPos = base.offset(Direction.UP, i);
            if(targetPos != pos){
                world.removeBlock(targetPos, false);
            }
        }
        if(state.get(POS) == 0){
            spawnAsEntity(world, pos, new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK));
        }

        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        if(state.get(POS) != 1){
            BlockPos middlePos = getMiddlePos(state, pos);
            BlockState middleState = world.getBlockState(middlePos);
            manager.addBlockDestroyEffects(middlePos, middleState);
            return true;
        }
        return super.addDestroyEffects(state, world, pos, manager);
    }

    @Override
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        if(target instanceof BlockRayTraceResult){
            BlockRayTraceResult result = (BlockRayTraceResult) target;
            if(state.get(POS) != 1){
                BlockPos middlePos = getMiddlePos(state, result.getPos());
                manager.addBlockHitEffects(middlePos, result.getFace());
                return true;
            }
        }
        return super.addHitEffects(state, world, target, manager);
    }



    //// BASE \\\\\

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POS);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity) {
        return true;
    }

    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.create(0, -state.get(POS),0, 1, 3 - state.get(POS), 1);
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState p_199600_1_, IBlockReader p_199600_2_, BlockPos p_199600_3_) {
        return super.getRaytraceShape(p_199600_1_, p_199600_2_, p_199600_3_);
    }

    //// RENDERING \\\\

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(POS) == 1 ? BlockRenderType.MODEL  : BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
