package sonar.logistics.common.multiparts.networking;


import sonar.logistics.server.address.Address;

import java.util.List;

/**
 * A networked tile which can provide more addresses too the network, such as readers,
 */
public interface IAddressProvider extends INetworkedTile {

    void addSourceAddresses(List<Address> addressList);

}
