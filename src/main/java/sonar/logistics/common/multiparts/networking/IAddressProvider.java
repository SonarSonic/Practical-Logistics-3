package sonar.logistics.common.multiparts.networking;


import sonar.logistics.server.address.Address;

import java.util.List;

/**
 * A networked tile which can provide more addresses too the network, such as readers,
 */
public interface IAddressProvider extends INetworkedTile {

    /**
     * The external address list contains addresses routing too objects which have no relation to any network e.g. tile entities, blocks, entities, worlds or data //todo should data have a seperate address list?
     * Duplicates must not be added.
     */
    void addToExternalAddressList(List<Address> addressList);

    /**
     * The internal address list contains addresses to other network devices, the device itself will already be included on this list, this relates to wireless connections only
     * Duplicates must not be added.
     */
    void addToGlobalAddressList(List<Address> addressList);

}
