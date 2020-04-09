package sonar.logistics.server.networks;

import sonar.logistics.multiparts.base.MultipartTile;
import sonar.logistics.multiparts.networking.IDataSourceNode;
import sonar.logistics.multiparts.networking.INetworkedTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PL3NetworkCaches<T extends INetworkedTile> {

    public Class<T> clazz;
    public Consumer<PL3Network> changeCache;

    public PL3NetworkCaches(Class<T> clazz, Consumer<PL3Network> changeCache) {
        this.clazz = clazz;
    }

    public static final PL3NetworkCaches<INetworkedTile> TILE = new PL3NetworkCaches<INetworkedTile>(INetworkedTile.class, (network) -> {}) {};
    public static final PL3NetworkCaches<IDataSourceNode> DATA_SOURCE_NODES = new PL3NetworkCaches<IDataSourceNode>(IDataSourceNode.class, network -> network.queueNetworkUpdate(EnumNetworkUpdate.DATA_SOURCES)) {};
    public static final PL3NetworkCaches<?>[] handlers = new PL3NetworkCaches<?>[]{TILE, DATA_SOURCE_NODES};

    public static Map<PL3NetworkCaches<?>, List<INetworkedTile>> newCachesMap(){
        Map<PL3NetworkCaches<?>, List<INetworkedTile>> map = new HashMap<>();
        for(PL3NetworkCaches<?> handler : handlers){
            map.put(handler, new ArrayList<>());
        }
        return map;
    }

    public String toString(){
        return clazz.getSimpleName();
    }

}