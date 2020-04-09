package sonar.logistics.server.networks;

import com.google.common.collect.Lists;
import net.minecraft.world.dimension.DimensionType;
import sonar.logistics.PL3;
import sonar.logistics.blocks.host.NetworkedHostTile;
import sonar.logistics.multiparts.networking.INetworkedTile;
import sonar.logistics.server.data.sources.IDataSource;
import sonar.logistics.utils.ListHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PL3Network {

    private final DimensionType hostDim;
    private int globalNetworkID;

    private List<NetworkedHostTile> loadedHosts = new ArrayList<>();
    private List<IDataSource> localSources = new ArrayList<>();
    private Map<PL3NetworkCaches<?>, List<INetworkedTile>> localCaches = PL3NetworkCaches.newCachesMap();
    private boolean[] networkUpdates = new boolean[EnumNetworkUpdate.values().length];

    public PL3Network(DimensionType hostDim, int globalNetworkID){
        this.hostDim = hostDim;
        this.globalNetworkID = globalNetworkID;
    }

    public int getNetworkID() {
        return globalNetworkID;
    }

    public DimensionType getDimType(){
        return hostDim;
    }

    public <T extends INetworkedTile> List<T> getCacheList(PL3NetworkCaches<T> cacheType){
        return (List<T>) localCaches.getOrDefault(cacheType, new ArrayList<>());
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

    public void updateNetworkSources(){
        localSources.clear();
        getCacheList(PL3NetworkCaches.DATA_SOURCE_NODES).forEach(node -> node.addDataSources(localSources));
        //TODO if a source has gone offline and someone is waiting for it.
    }

    ///// NETWORKING \\\\\

    public void delete() {
        loadedHosts.forEach(host -> setHostNetwork(host, null));
        clear();
        PL3NetworkManager.INSTANCE.cachedNetworks.remove(globalNetworkID);
    }

    public void clear(){
        loadedHosts.clear();
        localSources.clear();
        localCaches.clear();
    }

    public void shrink() {
        ///TODO IS THIS NECESSARY - DOESN'T IT HAPPE NDUE TO the ADD CABLE in shrink anyway?
        Lists.newArrayList(loadedHosts).forEach(host -> { //we copy the list, as it is modified in disconnectHost
            PL3Network network = PL3NetworkManager.INSTANCE.getNetworkOrCreate(host.getWorld(), host.getPos());
            if(network != this){
                disconnectHost(host);
                if(network != null) { //if the network was null the host's network will already have been set to null in disconnectHost
                    network.connectHost(host);
                }
            }
        });
        verifyIntegrity();
    }

    public void merge(PL3Network merging) {
        merging.loadedHosts.forEach(this::connectHost);
        merging.clear();
        PL3NetworkManager.INSTANCE.cachedNetworks.remove(merging.globalNetworkID);
    }

    public void verifyIntegrity(){
        if(loadedHosts.size() == 0){
            delete(); //A PL3 Network is only a Cache, if there is nothing to cache it should be deleted. TODO - if more "Wireless" things are added, hosts might not be the only thing to check.
        }
    }

    public void changeNetworkID(int networkID) {
        globalNetworkID = networkID;
        loadedHosts.forEach(host -> host.globalNetworkID = networkID);
    }

    ///// MULTIPART HOSTS \\\\\

    public void setHostNetwork(NetworkedHostTile host, @Nullable PL3Network network){
        host.network = network;
        host.globalNetworkID = network == null ? -1 : network.globalNetworkID;
        host.queueMarkDirty();
    }

    public void connectHost(NetworkedHostTile host){
        ListHelper.addWithCheck(loadedHosts, host);
        host.forEachNetworkedTile(this::loadNetworkTile);
        setHostNetwork(host, this);
    }

    public void disconnectHost(NetworkedHostTile host){
        loadedHosts.remove(host);
        host.forEachNetworkedTile(this::unloadNetworkTile);
        setHostNetwork(host, null);
        verifyIntegrity();
    }


    ///// MULTIPARTS TILES \\\\\

    public void loadNetworkTile(INetworkedTile part){
        for (PL3NetworkCaches<?> handler : PL3NetworkCaches.handlers) {
            if (handler.clazz.isInstance(part) && ListHelper.addWithCheck(localCaches.get(handler), part)) {
                handler.changeCache.accept(this);
            }
        }
    }

    public void unloadNetworkTile(INetworkedTile part){
        for (PL3NetworkCaches<?> handler : PL3NetworkCaches.handlers) {
            if (handler.clazz.isInstance(part) && localCaches.get(handler).remove(part)) {
                handler.changeCache.accept(this);
            }
        }
    }

    public static void dump(@Nullable PL3Network net){
        PL3.LOGGER.debug("------ PL3 NETWORK DATA START ------");
        if(net == null){
            PL3.LOGGER.debug("NO NETWORK FOUND");
        }else {
            PL3.LOGGER.debug("NetworkID: {}", net.globalNetworkID);
            PL3.LOGGER.debug("Dimension: {}", net.hostDim);
            PL3.LOGGER.debug("Hosts: {} List: {}",net.loadedHosts.size(), net.loadedHosts);
            PL3.LOGGER.debug("Sources: {} List: {}", net.localSources.size(), net.localSources);
            PL3.LOGGER.debug("Network Updates {}", net.networkUpdates);
            PL3.LOGGER.debug("Cache Map: {}", net.localCaches);

        }
        PL3.LOGGER.debug("------ PL3 NETWORK DATA END ------");
    }
}
