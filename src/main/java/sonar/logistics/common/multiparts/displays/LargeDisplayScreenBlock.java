package sonar.logistics.common.multiparts.displays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import sonar.logistics.common.multiparts.base.IMultipartBlock;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.server.cables.EnumCableTypes;
import sonar.logistics.server.cables.GlobalCableData;
import sonar.logistics.server.cables.LocalCableData;
import sonar.logistics.server.cables.WorldCableData;
import sonar.logistics.server.caches.displays.ConnectedDisplay;
import sonar.logistics.server.caches.displays.ConnectedDisplayManager;
import sonar.logistics.util.PL3Properties;
import sonar.logistics.util.PL3Shapes;

public class LargeDisplayScreenBlock extends AbstractScreenBlock implements IMultipartBlock {

    public LargeDisplayScreenBlock(Properties props) {
        super(props, PL3Shapes.LARGE_DISPLAY_ROTATED_VOXELS);
    }

    @Override
    public boolean hasMultipartTile(MultipartEntry entry){
        return true;
    }

    @Override
    public MultipartTile createMultipartTile(MultipartEntry entry){
        return new LargeDisplayScreenTile(entry, PL3Shapes.LARGE_DISPLAY_SCREEN_SCALING);
    }

    @Override
    public ActionResultType onMultipartActivated(MultipartEntry entry, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {

        if(!world.isRemote){
            if(player.isSneaking()) {
                WorldCableData worldData = GlobalCableData.getWorldData(world);//TESTING
                LocalCableData networkCableData = worldData.getCableData(pos, EnumCableTypes.getLargeDisplayType(entry.getBlockState().get(PL3Properties.ORIENTATION)));//TESTING
                if (networkCableData == null) {
                    player.sendMessage(new StringTextComponent(pos.toString() + ": " + "NO SCREEN FOUND"));
                } else {
                    player.sendMessage(new StringTextComponent(pos.toString() + ": " + networkCableData.globalNetworkID + " Cable Count: " + networkCableData.cables.size()));
                }
            }else{
                ConnectedDisplay.dump(ConnectedDisplayManager.INSTANCE.getCableCache(world, pos, EnumCableTypes.getLargeDisplayType(entry.getBlockState().get(PL3Properties.ORIENTATION))));
            }
        }

        return super.onMultipartActivated(entry, world, pos, player, hand, rayTraceResult);
    }

    ////// NETWORKING \\\\\

    public void addDisplay(World world, BlockPos pos, Direction dir){
        if(!world.isRemote) {
            WorldCableData worldData = GlobalCableData.getWorldData(world);
            worldData.addCable(world, pos, EnumCableTypes.getLargeDisplayType(dir));
        }
    }

    public void removeDisplay(World world, BlockPos pos, Direction dir){
        if(!world.isRemote) {
            WorldCableData worldData = GlobalCableData.getWorldData(world);
            worldData.removeCable(world, pos, EnumCableTypes.getLargeDisplayType(dir));
        }
    }

    ///// MULTIPART CONNECTING \\\\\

    @Override
    public void onPlaced(World world, BlockState state, BlockPos pos){
        addDisplay(world, pos, state.get(PL3Properties.ORIENTATION));
    }

    @Override
    public void onDestroyed(World world, BlockState state, BlockPos pos){
        removeDisplay(world, pos, state.get(PL3Properties.ORIENTATION));
    }

    @Override
    public void onNeighborChange(MultipartEntry entry, IWorldReader world, BlockPos pos, BlockPos neighbor){
        entry.host.queueSyncPacket();
    }

    @Override
    public void neighborChanged(MultipartEntry entry, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
        entry.host.queueSyncPacket();
    }
}
