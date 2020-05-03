package sonar.logistics.server.caches.displays;

import com.google.common.graph.Network;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.blocks.host.MultipartHostHelper;
import sonar.logistics.blocks.host.MultipartHostTile;
import sonar.logistics.blocks.host.NetworkedHostTile;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.displays.LargeDisplayScreenTile;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;
import sonar.logistics.server.cables.*;
import sonar.logistics.server.caches.AbstractCableCacheListener;

import javax.annotation.Nullable;

public class ConnectedDisplayManager extends AbstractCableCacheListener<ConnectedDisplay> implements ICableHelper {

    public static final ConnectedDisplayManager INSTANCE = new ConnectedDisplayManager();

    @Override
    public ConnectedDisplay createNewCache(World world, int globalCacheID, EnumCableTypes cableType) {
        return new ConnectedDisplay(world, globalCacheID, cableType);
    }

    @Override
    public void onCableAdded(World world, BlockPos pos, EnumCableTypes cableType) {
        LargeDisplayScreenTile display = getLargeDisplay(world, pos, cableType.dir);
        if(display != null){
            connectDisplay(display);
        }
    }

    @Override
    public void onCableRemoved(World world, BlockPos pos, EnumCableTypes cableType) {
        LargeDisplayScreenTile display = getLargeDisplay(world, pos, cableType.dir);
        if(display != null){
            disconnectDisplay(display);
        }
    }

    @Nullable
    public LargeDisplayScreenTile getLargeDisplay(World world, BlockPos pos, Direction dir){
        NetworkedHostTile host = MultipartHostHelper.getNetworkedHostTile(world, pos);
        if(host != null){
            MultipartEntry entry = host.getMultipart(EnumMultipartSlot.fromDirection(dir));
            if(entry != null && entry.getMultipartTile() instanceof LargeDisplayScreenTile){
                return (LargeDisplayScreenTile) entry.getMultipartTile();
            }
        }
        return null;
    }

    public void connectDisplay(LargeDisplayScreenTile tile){
        ConnectedDisplay display = getOrCreateCableCache(tile.getHostWorld(), tile.getPos(), EnumCableTypes.getLargeDisplayType(tile.getFacing()));
        if(display != null){
            display.connectDisplay(tile);
            PL3.LOGGER.debug("Connected Display: {} Network ID: {}  FACE: {}", tile.getPos().toString(), display.connectedDisplayID, tile.getFacing());
        }else{
            PL3.LOGGER.error("Connected Displays: FAILED CONNECTION - FAILED TO FIND DISPLAY AT: POS: {} DIM: {} FACE: {}", tile.getPos().toString(), tile.getHostWorld().getDimension().getType().getId(), tile.getFacing());
        }
    }

    public void disconnectDisplay(LargeDisplayScreenTile tile){
        if(tile.connectedDisplayID == -1){
            return;
        }
        ConnectedDisplay network = cached.get(tile.connectedDisplayID); //we obtain the network this way instead, the cable is likely destroyed.
        if(network != null){
            network.disconnectDisplay(tile);
            PL3.LOGGER.debug("Disconnect Display: {} Network ID: {}  FACE: {}", tile.getPos().toString(), network.connectedDisplayID, tile.getFacing());
        }else{
            PL3.LOGGER.error("Connected Displays: FAILED DISCONNECTION - FAILED TO FIND DISPLAY AT: POS: {} DIM: {} FACE: {}", tile.getPos().toString(), tile.getHostWorld().getDimension().getType().getId(), tile.getFacing());
        }
    }


    ///// ICableHelper \\\\\

    @Override
    public boolean canConnectCables(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir, EnumCableTypes cableType) {
        if(dir == cableType.dir || dir.getOpposite() == cableType.dir){
            return false; // displays only connect sideways + up and down.
        }
        return canConnectDisplayOnSide(world, cablePos, cableType) && canConnectDisplayOnSide(world, adjPos, cableType);
    }

    @Override
    public boolean canRenderCables(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir, EnumCableTypes cableType) {
        return canConnectCables(world, cablePos, adjPos, dir, cableType);
    }

    public boolean canConnectDisplayOnSide(IBlockReader world, BlockPos cablePos, EnumCableTypes cableType){
        MultipartHostTile host = MultipartHostHelper.getMultipartHostTile(world, cablePos);
        if(host != null){
            MultipartEntry entry = host.getMultipart(EnumMultipartSlot.fromDirection(cableType.dir));
            if(entry != null && entry.getMultipartTile() instanceof LargeDisplayScreenTile){
                return true;
            }
        }
        return false;
    }
}
