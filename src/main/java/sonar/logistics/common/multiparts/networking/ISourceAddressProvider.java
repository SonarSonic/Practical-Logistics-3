package sonar.logistics.common.multiparts.networking;


import sonar.logistics.server.data.source.Address;

import java.util.List;

public interface ISourceAddressProvider extends INetworkedTile {

    void addSourceAddresses(List<Address> addressList);

}
