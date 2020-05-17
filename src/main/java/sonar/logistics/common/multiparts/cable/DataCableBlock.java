package sonar.logistics.common.multiparts.cable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import sonar.logistics.common.blocks.host.MultipartHostTile;
import sonar.logistics.common.multiparts.base.IMultipartBlock;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.utils.EnumMultipartSlot;
import sonar.logistics.server.cables.EnumCableTypes;
import sonar.logistics.server.cables.GlobalCableData;
import sonar.logistics.server.cables.LocalCableData;
import sonar.logistics.server.cables.WorldCableData;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.caches.network.PL3NetworkManager;
import sonar.logistics.util.PL3Shapes;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class DataCableBlock extends Block implements IMultipartBlock {

    public static final BooleanProperty[] CONNECTIONS = new BooleanProperty[]{DOWN, UP, NORTH, SOUTH, EAST, WEST};

    public DataCableBlock(Properties props) {
        super(props);
        setDefaultState(setConnectedState(getStateContainer().getBaseState(), false));
    }

    ///// SHAPES \\\\\

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = PL3Shapes.DATA_CABLE_CENTRE_VOXEL;
        for(Direction dir : Direction.values()){
            if(state.get(CONNECTIONS[dir.ordinal()])){
                shape = VoxelShapes.combine(shape, PL3Shapes.DATA_CABLE_CONNECTOR_ROTATED_VOXELS[dir.ordinal()], IBooleanFunction.OR);
            }
        }
        return shape;
    }

    ///// BLOCK STATE \\\\\

    public BlockState setConnectedState(BlockState state, boolean bool){
        for(BooleanProperty prop : CONNECTIONS){
            state = state.with(prop, bool);
        }
        return state;
    }

    public BlockState getConnectedState(BlockState state, IBlockReader reader, BlockPos pos){
        for(Direction dir : Direction.values()){
            state = state.with(CONNECTIONS[dir.ordinal()], PL3NetworkManager.INSTANCE.canRenderCables(reader, pos, pos.offset(dir), dir, EnumCableTypes.NETWORK_CABLE));
        }
        return state;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getConnectedState(getDefaultState(), context.getWorld(), context.getPos());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONNECTIONS);
    }

    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
       return state.with(CONNECTIONS[facing.ordinal()], PL3NetworkManager.INSTANCE.canRenderCables(world, currentPos, facingPos, facing, EnumCableTypes.NETWORK_CABLE));
    }

    ///// MULTIPART STATES \\\\\

    @Override
    public EnumMultipartSlot getMultipartSlotFromState(BlockState state) {
        return EnumMultipartSlot.CENTRE;
    }

    @Override
    public BlockState getMultipartStateFromSlot(EnumMultipartSlot slot) {
        return getDefaultState();
    }

    @Override
    public BlockState getMultipartRenderState(MultipartHostTile host, MultipartEntry entry) {
        return getConnectedState(entry.getBlockState(), host.getWorld(), host.getPos());
    }



    ///// INTERACTIONS \\\\\

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(!world.isRemote){
            if(player.isSneaking()) {
                WorldCableData worldData = GlobalCableData.getWorldData(world);//TESTING
                LocalCableData networkCableData = worldData.getCableData(pos, EnumCableTypes.NETWORK_CABLE);//TESTING
                if (networkCableData == null) {
                    player.sendMessage(new StringTextComponent(pos.toString() + ": " + "NO NETWORK FOUND"));
                } else {
                    player.sendMessage(new StringTextComponent(pos.toString() + ": " + networkCableData.globalNetworkID + " Cable Count: " + networkCableData.cables.size()));
                }
            }else{
                PL3Network.dump(PL3NetworkManager.INSTANCE.getCableCache(world, pos, EnumCableTypes.NETWORK_CABLE));
            }
        }
        return ActionResultType.SUCCESS;
    }


    ///// NETWORKING \\\\\

    public void addCable(World world, BlockPos pos){
        if(!world.isRemote) {
            WorldCableData worldData = GlobalCableData.getWorldData(world);
            worldData.addCable(world, pos, EnumCableTypes.NETWORK_CABLE);
        }
    }

    public void removeCable(World world, BlockPos pos){
        if(!world.isRemote) {
            WorldCableData worldData = GlobalCableData.getWorldData(world);
            worldData.removeCable(world, pos, EnumCableTypes.NETWORK_CABLE);
        }
    }

    public void checkConnection(World world, BlockPos pos, Direction dir){
        if(!world.isRemote) {
            WorldCableData worldData = GlobalCableData.getWorldData(world);
            worldData.checkCableConnection(world, pos, dir, EnumCableTypes.NETWORK_CABLE);
        }
    }

    ///// BLOCK CONNECTING \\\\\

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        world.setBlockState(pos, getConnectedState(state, world, pos));
        addCable(world, pos);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
        removeCable(world, pos);
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        world.setBlockState(pos, getConnectedState(state, world, pos));
    }

    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        if(world instanceof World){
            ((World)world).setBlockState(pos, getConnectedState(state, world, pos));
        }
    }

    ///// MULTIPART CONNECTING \\\\\

    @Override
    public void onPlaced(World world, BlockState state, BlockPos pos){
        addCable(world, pos);
    }

    @Override
    public void onDestroyed(World world, BlockState state, BlockPos pos){
        removeCable(world, pos);
    }

    @Override
    public void onNeighborChange(MultipartEntry entry, IWorldReader world, BlockPos pos, BlockPos neighbor){
        entry.host.queueSyncPacket();
    }

    @Override
    public void neighborChanged(MultipartEntry entry, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
        entry.host.queueSyncPacket();
    }

    @Override
    public void onMultipartAdded(MultipartHostTile host, MultipartEntry entry, MultipartEntry added) {
        if(added.slot.getDirection() != null) {
            checkConnection(host.getWorld(), host.getPos(), added.slot.getDirection());
        }
    }

    @Override
    public void onMultipartRemoved(MultipartHostTile host, MultipartEntry entry, MultipartEntry removed) {
        if(removed.slot.getDirection() != null) {
            checkConnection(host.getWorld(), host.getPos(), removed.slot.getDirection());
        }
    }

    @Override
    public boolean requiresMultipartHost() {
        return false;
    }

    @Override
    public boolean onHostRemoved(MultipartEntry entry){
        entry.host.getWorld().setBlockState(entry.host.getPos(), getConnectedState(this.getDefaultState(),entry.host.getWorld(), entry.host.getPos()));
        return true;
    }

}
