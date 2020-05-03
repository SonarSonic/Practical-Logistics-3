package sonar.logistics.server.caches.network;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.blocks.PL3Blocks;
import sonar.logistics.blocks.host.MultipartHostHelper;
import sonar.logistics.blocks.host.MultipartHostTile;
import sonar.logistics.blocks.host.NetworkedHostTile;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;
import sonar.logistics.server.cables.*;
import sonar.logistics.server.caches.AbstractCableCacheListener;

public class PL3NetworkManager extends AbstractCableCacheListener<PL3Network> implements ICableHelper, ICableListener {

    public static final PL3NetworkManager INSTANCE = new PL3NetworkManager();

    @Override
    public PL3Network createNewCache(World world, int globalCacheID, EnumCableTypes cableType) {
        return new PL3Network(world, globalCacheID, cableType);
    }

    @Override
    public void onCableAdded(World world, BlockPos pos, EnumCableTypes cableType){
        NetworkedHostTile host = MultipartHostHelper.getNetworkedHostTile(world, pos);
        if(host != null){
            connectHost(host, cableType);
        }
    }

    @Override
    public void onCableRemoved(World world, BlockPos pos, EnumCableTypes cableType){
        NetworkedHostTile host = MultipartHostHelper.getNetworkedHostTile(world, pos);
        if(host != null){
            disconnectHost(host, cableType);
        }
    }

    public void connectHost(NetworkedHostTile tile, EnumCableTypes cableType){
        PL3Network network = getOrCreateCableCache(tile.getWorld(), tile.getPos(), cableType);
        if(network == null){
            PL3.LOGGER.error("PL3NetworkManager: FAILED HOST CONNECTION - FAILED TO FIND NETWORK AT: POS: {} DIM: {}", tile.getPos().toString(), tile.getWorld().getDimension().getType().getId());
        }else{
            network.connectHost(tile);
            PL3.LOGGER.debug("Connected Host: {} Network ID: {}", tile.getPos().toString(), network.getNetworkID());
        }
    }

    public void disconnectHost(NetworkedHostTile tile, EnumCableTypes cableType){
        if(tile.globalNetworkID == -1){
            return;
        }
        PL3Network network = cached.get(tile.globalNetworkID); //we obtain the network this way instead, the cable is likely destroyed.
        if(network != null){
            network.disconnectHost(tile);
            PL3.LOGGER.debug("Disconnect Host: {} Network ID: {} ", tile.getPos().toString(), network.getNetworkID());

        }else{
            PL3.LOGGER.error("PL3NetworkManager: FAILED HOST DISCONNECTION - FAILED TO FIND NETWORK AT: POS: {} DIM: {}", tile.getPos().toString(), tile.getWorld().getDimension().getType().getId());
        }
    }


    ///// ICableHelper \\\\\

    @Override
    public boolean canConnectCables(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir, EnumCableTypes cableType) {
        return canConnectDataCableOnSide(world, cablePos, dir) && canConnectDataCableOnSide(world, adjPos, dir.getOpposite());
    }

    @Override
    public boolean canRenderCables(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir, EnumCableTypes cableType) {
        return canConnectDataCableOnSide(world, cablePos, dir) && canConnectDataCableOnSide(world, adjPos, dir.getOpposite()) || canConnectDataCableInternally(world, cablePos, dir);
    }

    public static boolean canConnectDataCableOnSide(IBlockReader world, BlockPos cablePos, Direction dir){
        if(world.getBlockState(cablePos).getBlock() == PL3Blocks.DATA_CABLE){
            ////normal cable blocks don't have any sort of special blocking etc.
            return true;
        }
        MultipartHostTile host = MultipartHostHelper.getMultipartHostTile(world, cablePos);
        if(host != null){
            MultipartEntry entry = host.getMultipart(EnumMultipartSlot.CENTRE);
            if(entry != null && entry.multipart == PL3Blocks.DATA_CABLE){
                MultipartEntry blocking = host.getMultipart(EnumMultipartSlot.fromDirection(dir));
                return blocking == null; //TODO COVERS IMPLEMENTATION ETC.
            }
        }
        return false;
    }

    public static boolean canConnectDataCableInternally(IBlockReader world, BlockPos cablePos, Direction dir){
        MultipartHostTile host = MultipartHostHelper.getMultipartHostTile(world, cablePos);
        if(host != null){
            MultipartEntry entry = host.getMultipart(EnumMultipartSlot.CENTRE);
            if(entry != null && entry.multipart == PL3Blocks.DATA_CABLE){
                MultipartEntry blocking = host.getMultipart(EnumMultipartSlot.fromDirection(dir));
                return blocking != null; //TODO SPECIAL CONNECTIONS ETC
            }
        }
        return false;
    }
}
