package sonar.logistics.common.multiparts.base;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.common.blocks.host.NetworkedHostTile;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.server.ServerDataCache;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.server.data.watchers.DataWatcher;
import sonar.logistics.util.network.EnumSyncType;

import javax.annotation.Nullable;

public class NetworkedTile extends MultipartTile implements INetworkedTile {

    private int identity = 0;
    private Address address;

    public NetworkedTile(MultipartEntry entry) {
        super(entry);
    }

    private int getOrCreateIdentity(){
        if(identity <= 0){
            identity = ServerDataCache.INSTANCE.getNextIdentity();
        }
        return identity;
    }

    @Override
    public Address getAddress() {
        if(address == null){
            address = Address.createIdentityAddress(getOrCreateIdentity());
        }
        return address;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType) {
        tag.putInt("id", getOrCreateIdentity());
        return super.write(tag, syncType);
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType) {
        identity = tag.getInt("id");
        return super.read(tag, syncType);
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
    public void onNetworkConnected(PL3Network network) {
        ServerDataCache.INSTANCE.connectNetworkedTile(this);
        DataWatcher dataWatcher = getDataWatcher();
        if(dataWatcher != null){
            DataManager.INSTANCE.addDataWatcher(dataWatcher);
        }
    }

    @Override
    public void onNetworkDisconnected(PL3Network network) {
        ServerDataCache.INSTANCE.disconnectNetworkedTile(this);
        DataWatcher dataWatcher = getDataWatcher();
        if(dataWatcher != null){
            DataManager.INSTANCE.removeDataWatcher(dataWatcher);
        }
    }

    @Nullable
    @Override
    public DataWatcher getDataWatcher() {
        return null;
    }
}
