package sonar.logistics.server;

import sonar.logistics.common.multiparts.networking.IAddressProvider;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.IdentityAddress;
import sonar.logistics.util.ListHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerDataCache {

    public static final ServerDataCache INSTANCE = new ServerDataCache();

    public final Map<Address, INetworkedTile> connectedTiles = new HashMap<>();

    public void clear(){
        connectedTiles.clear();
    }

    public INetworkedTile getNetworkedTile(int identity){
        for(Map.Entry<Address, INetworkedTile> entry : connectedTiles.entrySet()){
            if(entry.getKey() instanceof IdentityAddress && ((IdentityAddress) entry.getKey()).identity==identity){
                return entry.getValue();
            }
        }
        return null;
    }
    public INetworkedTile getNetworkedTile(Address address){
        if(!(address instanceof IdentityAddress)){
            return null;
        }
        return connectedTiles.get(address);
    }

    public void connectNetworkedTile(INetworkedTile tile){
        connectedTiles.put(tile.getAddress(), tile);
    }

    public void disconnectNetworkedTile(INetworkedTile tile){
        connectedTiles.remove(tile.getAddress());
    }

    public List<INetworkedTile> getNetworkedTiles(List<Address> addressList){
        List<INetworkedTile> tiles = new ArrayList<>();
        for(Address address : addressList){
            INetworkedTile tile = connectedTiles.get(address);
            if(tile != null){
                tiles.add(tile);
            }
        }
        return tiles;
    }

    public void addToGlobalAddressList(List<Address> addressList, INetworkedTile tile, boolean addSelf){
        if(addSelf){
            ListHelper.addWithCheck(addressList, tile.getAddress());
        }
        if(tile instanceof IAddressProvider){
            IAddressProvider provider = (IAddressProvider) tile;
            provider.addToGlobalAddressList(addressList);
        }
    }

    public void addToExternalAddressList(List<Address> addressList, INetworkedTile tile){
        if(tile instanceof IAddressProvider){
            IAddressProvider provider = (IAddressProvider) tile;
            provider.addToExternalAddressList(addressList);
        }
    }


    //// IDENTITIES \\\\

    public int getNextIdentity() {
        return ServerWorldData.get().getNextIdentity();
    }
}
