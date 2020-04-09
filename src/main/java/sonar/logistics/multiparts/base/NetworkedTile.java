package sonar.logistics.multiparts.base;

import sonar.logistics.multiparts.networking.INetworkedTile;

public class NetworkedTile extends MultipartTile implements INetworkedTile {


    public NetworkedTile(MultipartEntry entry) {
        super(entry);
    }

}
