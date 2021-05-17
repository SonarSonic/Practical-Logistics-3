package sonar.logistics.server.caches.network;

import com.google.common.collect.Lists;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.common.blocks.host.NetworkedHostTile;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.server.cables.EnumCableTypes;
import sonar.logistics.server.cables.ICableCache;
import sonar.logistics.server.data.sources.IDataSource;
import sonar.logistics.util.ListHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PL3Network implements ICableCache<PL3Network> {

    private final World world;
    private int globalNetworkID;
    private final EnumCableTypes cableType;

    private final List<NetworkedHostTile> loadedHosts = new ArrayList<>();
    private final List<IDataSource> localSources = new ArrayList<>();
    private final Map<PL3NetworkCaches<?>, List<INetworkedTile>> localCaches = PL3NetworkCaches.newCachesMap();
    private final boolean[] networkUpdates = new boolean[EnumNetworkUpdate.values().length];

    public PL3Network(World world, int globalNetworkID, EnumCableTypes cableType){
        this.world = world;
        this.globalNetworkID = globalNetworkID;
        this.cableType = cableType;
    }

    public void tick(){
        flushNetworkUpdates();
    }

    public int getNetworkID() {
        return globalNetworkID;
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

    ///// CABLE CACHE \\\\\

    @Override
    public void deleteCache() {
        loadedHosts.forEach(host -> setHostNetwork(host, null));
        clear();
        PL3NetworkManager.INSTANCE.cached.remove(globalNetworkID);
    }

    public void clear(){
        loadedHosts.clear();
        localSources.clear();
        localCaches.clear();
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
    }

    @Override
    public void mergeCache(PL3Network merging) {
        merging.loadedHosts.forEach(this::connectHost);
        merging.clear();
        PL3NetworkManager.INSTANCE.cached.remove(merging.globalNetworkID);
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
            PL3.LOGGER.debug("Dimension: {}", net.world.getDimension().getType());
            PL3.LOGGER.debug("Hosts: {} List: {}",net.loadedHosts.size(), net.loadedHosts);
            PL3.LOGGER.debug("Sources: {} List: {}", net.localSources.size(), net.localSources);
            PL3.LOGGER.debug("Network Updates {}", net.networkUpdates);
            PL3.LOGGER.debug("Cache Map: {}", net.localCaches);

        }
        PL3.LOGGER.debug("------ PL3 NETWORK DATA END ------");
    }
}
