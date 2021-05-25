package sonar.logistics.server.caches.network;

import com.google.common.collect.Lists;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.common.blocks.host.NetworkedHostTile;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.server.ServerDataCache;
import sonar.logistics.server.cables.EnumCableTypes;
import sonar.logistics.server.cables.ICableCache;
import sonar.logistics.server.address.Address;
import sonar.logistics.util.ListHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PL3Network implements ICableCache<PL3Network> {

    public final World world;
    public int globalNetworkID;
    public final EnumCableTypes cableType;

    public final List<NetworkedHostTile> loadedHosts = new ArrayList<>();
    public final List<Address> localAddressList = new ArrayList<>();
    public final List<Address> globalAddressList = new ArrayList<>();
    public final List<Address> externalAddressList = new ArrayList<>();

    public final boolean[] networkUpdates = new boolean[EnumNetworkUpdate.values().length];

    public PL3Network(World world, int globalNetworkID, EnumCableTypes cableType){
        this.world = world;
        this.globalNetworkID = globalNetworkID;
        this.cableType = cableType;
    }

    public void clear(){
        loadedHosts.clear();
        localAddressList.clear();
        globalAddressList.clear();
        externalAddressList.clear();
    }

    public void tick(){
        flushNetworkUpdates();
    }

    public int getNetworkID() {
        return globalNetworkID;
    }

    public void queueNetworkUpdate(EnumNetworkUpdate updateType){
        networkUpdates[updateType.ordinal()] = true;
    }

    public void flushNetworkUpdates(){
        for(int i = 0 ; i < EnumNetworkUpdate.values().length; i++){
            if(networkUpdates[i]){
                EnumNetworkUpdate.values()[i].action.accept(this);
                networkUpdates[i] = false;
            }
        }
    }

    public void updateAddressLists(){
        updateLocalAddressList();
        updateGlobalAddressList();
        updateExternalAddressList();
    }

    public void updateLocalAddressList(){
        localAddressList.clear();
        loadedHosts.forEach(host -> host.forEachNetworkedTile(tile -> ListHelper.addWithCheck(localAddressList, tile.getAddress())));
    }

    public void updateGlobalAddressList(){
        globalAddressList.clear();
        globalAddressList.addAll(localAddressList);

        boolean crossChecked = false;
        while(!crossChecked){
            int size = globalAddressList.size();
            List<INetworkedTile> tiles = ServerDataCache.INSTANCE.getNetworkedTiles(globalAddressList);
            tiles.forEach(tile -> ServerDataCache.INSTANCE.addToGlobalAddressList(globalAddressList, tile, true));
            crossChecked = size == globalAddressList.size();
        }
    }

    public void updateExternalAddressList(){
        externalAddressList.clear();

        List<INetworkedTile> tiles = ServerDataCache.INSTANCE.getNetworkedTiles(globalAddressList);
        tiles.forEach(tile -> ServerDataCache.INSTANCE.addToExternalAddressList(externalAddressList, tile));
    }

    ///// CABLE CACHE \\\\\

    @Override
    public void deleteCache() {
        loadedHosts.forEach(host -> setHostNetwork(host, null));
        clear();
        PL3NetworkManager.INSTANCE.cached.remove(globalNetworkID);
    }

    @Override
    public void shrinkCache() {
        ///TODO IS THIS NECESSARY - DOESN'T IT HAPPE NDUE TO the ADD CABLE in shrink anyway?
        Lists.newArrayList(loadedHosts).forEach(host -> { //we copy the list, as it is modified in disconnectHost
            PL3Network network = PL3NetworkManager.INSTANCE.getCableCache(host.getWorld(), host.getPos(), cableType);
            if(network != this){
                disconnectHost(host);
                if(network != null) { //if the network was null the host's network will already have been set to null in disconnectHost
                    network.connectHost(host);
                }
            }
        });
        verifyIntegrity();
        queueNetworkUpdate(EnumNetworkUpdate.UPDATE_ADDRESS_LISTS);
    }

    @Override
    public void mergeCache(PL3Network merging) {
        merging.loadedHosts.forEach(this::connectHost);
        merging.clear();
        PL3NetworkManager.INSTANCE.cached.remove(merging.globalNetworkID);
        queueNetworkUpdate(EnumNetworkUpdate.UPDATE_ADDRESS_LISTS);
    }

    public void verifyIntegrity(){
        if(loadedHosts.size() == 0){
            deleteCache(); //A PL3 Network is only a Cache, if there is nothing to cache it should be deleted.
            // TODO - if more "Wireless" things are added, hosts might not be the only thing to check.
        }
    }

    @Override
    public void changeCacheID(int networkID) {
        globalNetworkID = networkID;
        loadedHosts.forEach(host -> host.globalNetworkID = networkID);
        queueNetworkUpdate(EnumNetworkUpdate.UPDATE_ADDRESS_LISTS);
    }

    ///// MULTIPART HOSTS \\\\\

    public void connectHost(NetworkedHostTile host){
        ListHelper.addWithCheck(loadedHosts, host);
        host.forEachNetworkedTile(this::connectNetworkedTile);
        setHostNetwork(host, this);
        queueNetworkUpdate(EnumNetworkUpdate.UPDATE_ADDRESS_LISTS);
    }

    public void disconnectHost(NetworkedHostTile host){
        loadedHosts.remove(host);
        host.forEachNetworkedTile(this::disconnectNetworkedTile);
        setHostNetwork(host, null);
        verifyIntegrity();
        queueNetworkUpdate(EnumNetworkUpdate.UPDATE_ADDRESS_LISTS);
    }

    public void setHostNetwork(NetworkedHostTile host, @Nullable PL3Network network){
        host.network = network;
        host.globalNetworkID = network == null ? -1 : network.globalNetworkID;
        host.queueMarkDirty();
        host.queueSyncPacket();
    }


    ///// MULTIPARTS TILES \\\\\

    public void connectNetworkedTile(INetworkedTile part){
        part.onNetworkConnected(this);
        queueNetworkUpdate(EnumNetworkUpdate.UPDATE_ADDRESS_LISTS);
    }

    public void disconnectNetworkedTile(INetworkedTile part){
        part.onNetworkDisconnected(this);
        queueNetworkUpdate(EnumNetworkUpdate.UPDATE_ADDRESS_LISTS);
    }

    ///// CONNECTED SOURCES \\\\\



    public static void dump(@Nullable PL3Network net){
        PL3.LOGGER.debug("------ PL3 NETWORK DATA START ------");
        if(net == null){
            PL3.LOGGER.debug("NO NETWORK FOUND");
        }else {
            PL3.LOGGER.debug("NetworkID: {}", net.globalNetworkID);
            PL3.LOGGER.debug("Dimension: {}", net.world.getDimension().getType());
            PL3.LOGGER.debug("Hosts: {} List: {}",net.loadedHosts.size(), net.loadedHosts);
            PL3.LOGGER.debug("Local Address Size: {} List: {}", net.localAddressList.size(), net.localAddressList);
            PL3.LOGGER.debug("Global Address Size: {} List: {}", net.globalAddressList.size(), net.globalAddressList);
            PL3.LOGGER.debug("External Address Size: {} List: {}", net.externalAddressList.size(), net.externalAddressList);
            PL3.LOGGER.debug("Network Updates {}", net.networkUpdates);
        }
        PL3.LOGGER.debug("------ PL3 NETWORK DATA END ------");
    }
}
