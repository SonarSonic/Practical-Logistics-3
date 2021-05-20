package sonar.logistics.common.multiparts.base;

import sonar.logistics.common.blocks.host.NetworkedHostTile;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.server.caches.network.PL3Network;

public class NetworkedTile extends MultipartTile implements INetworkedTile {

    public NetworkedTile(MultipartEntry entry) {
        super(entry);
    }

    @Override
    public PL3Network getNetwork() {
        if(entry.getHost() instanceof NetworkedHostTile){
            NetworkedHostTile tile = (NetworkedHostTile) entry.getHost();
            return tile.getNetwork();
        }
        return null;
    }

    @Override
    public int getNetworkID() {
        if(entry.getHost() instanceof NetworkedHostTile){
            NetworkedHostTile tile = (NetworkedHostTile) entry.getHost();
            return tile.getNetworkID();
        }
        return -1;
    }

    @Override
    public void onNetworkConnected(PL3Network network) {}

    @Override
    public void onNetworkDisconnected(PL3Network network) {}
}
