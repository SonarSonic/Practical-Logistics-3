package sonar.logistics.server;

import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.server.address.Address;

import java.util.HashMap;
import java.util.Map;

public class ServerDataCache {

    public static final ServerDataCache INSTANCE = new ServerDataCache();

    public static final Map<Address, INetworkedTile> connectedTiles = new HashMap<>();

    public void clear(){
        connectedTiles.clear();
    }

    public INetworkedTile getNetworkedTile(Address address){
        return connectedTiles.get(address);
    }

    public void connectNetworkedTile(INetworkedTile tile){
        connectedTiles.put(tile.getAddress(), tile);
    }

    public void disconnectNetworkedTile(INetworkedTile tile){
        connectedTiles.remove(tile.getAddress());
    }


    //// IDENTITIES \\\\

    public int getNextIdentity() {
        return ServerWorldData.get().getNextIdentity();
    }
}
