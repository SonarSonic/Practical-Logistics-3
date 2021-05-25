package sonar.logistics.server.caches.network;

import java.util.function.Consumer;

public enum EnumNetworkUpdate {

    UPDATE_ADDRESS_LISTS(PL3Network::updateAddressLists),
    UPDATE_EXTERNAL_ADDRESS_LIST(PL3Network::updateExternalAddressList);

    public Consumer<PL3Network> action;
    EnumNetworkUpdate(Consumer<PL3Network> action){
        this.action = action;
    }

}
