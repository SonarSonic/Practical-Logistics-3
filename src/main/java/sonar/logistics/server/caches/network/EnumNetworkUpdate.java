package sonar.logistics.server.caches.network;

import java.util.function.Consumer;

public enum EnumNetworkUpdate {

    DATA_SOURCES(PL3Network::updateNetworkSources);

    public Consumer<PL3Network> action;
    EnumNetworkUpdate(Consumer<PL3Network> action){
        this.action = action;
    }

}
