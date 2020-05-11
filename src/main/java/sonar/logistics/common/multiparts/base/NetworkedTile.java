package sonar.logistics.common.multiparts.base;

import sonar.logistics.common.multiparts.networking.INetworkedTile;

public class NetworkedTile extends MultipartTile implements INetworkedTile {


    public NetworkedTile(MultipartEntry entry) {
        super(entry);
    }

}
