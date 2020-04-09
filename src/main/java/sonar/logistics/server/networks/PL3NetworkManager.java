package sonar.logistics.server.networks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.blocks.host.MultipartHostHelper;
import sonar.logistics.blocks.host.NetworkedHostTile;
import sonar.logistics.server.cables.CableHelper;
import sonar.logistics.server.cables.GlobalCableData;
import sonar.logistics.server.cables.LocalCableData;
import sonar.logistics.server.cables.WorldCableData;

import javax.annotation.Nullable;
import java.util.HashMap;

public class PL3NetworkManager {

    public static final PL3NetworkManager INSTANCE = new PL3NetworkManager();
    public HashMap<Integer, PL3Network> cachedNetworks = new HashMap<>();

    public void clear(){
        INSTANCE.cachedNetworks.clear();
    }

    public void onCableAdded(World world, BlockPos pos, byte cableType){
        NetworkedHostTile host = MultipartHostHelper.getNetworkedHostTile(world, pos);
        if(host != null){
            connectHost(host);
        }
    }

    public void onCableRemoved(World world, BlockPos pos, byte cableType){
        NetworkedHostTile host = MultipartHostHelper.getNetworkedHostTile(world, pos);
        if(host != null){
            disconnectHost(host);
        }
    }

    public void deleteNetwork(int networkID){
        PL3Network network = cachedNetworks.get(networkID);
        if(network != null){
            network.delete();
        }
    }

    public void shrinkNetwork(int networkID){
        PL3Network network = cachedNetworks.get(networkID);
        if(network != null){
            network.shrink();
        }
    }

    public void mergeNetworks(int networkID, int mergedID){
        PL3Network network = cachedNetworks.get(networkID);
        PL3Network merging = cachedNetworks.get(mergedID);
        if(network == null && merging == null){
            return; //no networks currently exist for these cables
        }
        if(network != null && merging == null){
            return; //if the merging network is just cables, there is nothing to merge.
        }
        if(network != null){
            network.merge(merging);
        }else{
            merging.changeNetworkID(networkID);
        }
    }


    @Nullable
    public LocalCableData getCableData(World world, BlockPos pos){
        WorldCableData worldData = GlobalCableData.getWorldData(world);
        return worldData.getCableData(pos, CableHelper.DATA_CABLE_TYPE);
    }

    public int getGlobalNetworkID(World world, BlockPos pos){
        LocalCableData cableData = getCableData(world, pos);
        return cableData == null ? -1 : cableData.globalNetworkID;
    }

    public PL3Network getNetwork(World world, BlockPos pos){
        int globalNetworkID = getGlobalNetworkID(world, pos);
        if (globalNetworkID != -1) {
            return cachedNetworks.get(globalNetworkID);
        }
        return null;
    }

    public PL3Network getNetworkOrCreate(World world, BlockPos pos){
        int globalNetworkID = getGlobalNetworkID(world, pos);
        if (globalNetworkID != -1) {
            return getOrCreateNetwork(world, globalNetworkID);
        }
        return null;
    }

    public void connectHost(NetworkedHostTile tile){
        PL3Network network = getNetworkOrCreate(tile.getWorld(), tile.getPos());
        if(network == null){
            PL3.LOGGER.error("PL3NetworkManager: HOST DISCONNECTION - FAILED TO FIND NETWORK AT: POS: {} DIM: {}", tile.getPos().toString(), tile.getWorld().getDimension().getType().getId());
        }else{
            network.connectHost(tile);
            PL3.LOGGER.debug("Connected Host: {} Network ID: {}", tile.getPos().toString(), network.getNetworkID());
        }
    }

    public void disconnectHost(NetworkedHostTile tile){
        if(tile.globalNetworkID == -1){
            return;
        }
        PL3Network network = cachedNetworks.get(tile.globalNetworkID); //we obtain the network this way instead, the cable is likely destroyed.
        if(network != null){
            network.disconnectHost(tile);
            PL3.LOGGER.debug("Disconnect Host: {} Network ID: {} ", tile.getPos().toString(), network.getNetworkID());

        }else{
            PL3.LOGGER.error("PL3NetworkManager: HOST DISCONNECTION - FAILED TO FIND NETWORK AT: POS: {} DIM: {}", tile.getPos().toString(), tile.getWorld().getDimension().getType().getId());
        }
    }

    public PL3Network getOrCreateNetwork(World world, int globalNetworkID){
        PL3Network network = cachedNetworks.get(globalNetworkID);
        if(network == null){
            network = new PL3Network(world.getDimension().getType(), globalNetworkID);
            cachedNetworks.put(globalNetworkID, network);
        }
        return network;
    }

}
