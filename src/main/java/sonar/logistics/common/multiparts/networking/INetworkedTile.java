package sonar.logistics.common.multiparts.networking;

import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.address.Address;

/**no methods needed for now, it's just so we know it's valid.*/
public interface INetworkedTile {

    PL3Network getNetwork();

    Address getAddress();

    int getNetworkID();

    void onNetworkConnected(PL3Network network);

    void onNetworkDisconnected(PL3Network network);

}
