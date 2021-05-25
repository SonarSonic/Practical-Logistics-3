package sonar.logistics.common.blocks.host;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.common.blocks.PL3Blocks;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.common.multiparts.utils.EnumMultipartSlot;
import sonar.logistics.server.cables.EnumCableTypes;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.caches.network.PL3NetworkManager;
import sonar.logistics.util.network.EnumSyncType;

import javax.annotation.Nullable;

public class NetworkedHostTile extends MultipartHostTile {

    public int globalNetworkID = -1; //saved id
    public PL3Network network; //cached network

    @Nullable
    public PL3Network getNetwork(){
        return network;
    }

    public int getNetworkID(){
        return globalNetworkID;
    }

    public boolean canConnect(){
        MultipartEntry entry = getMultipart(EnumMultipartSlot.CENTRE);
        return entry != null && entry.multipart == PL3Blocks.DATA_CABLE;
    }

    public void connectNetwork(){
        if(!world.isRemote && canConnect()) {
            PL3NetworkManager.INSTANCE.connectHost(this, EnumCableTypes.NETWORK_CABLE);
        }
    }

    public void disconnectNetwork(){
        if(!world.isRemote) {
            PL3NetworkManager.INSTANCE.disconnectHost(this, EnumCableTypes.NETWORK_CABLE);
        }
    }

    @Override
    public boolean doAddMultipart(MultipartEntry entry) {
        boolean added = super.doAddMultipart(entry);
        if(!isNBTLoading && entry.getMultipartTile() instanceof INetworkedTile && network != null){
            network.connectNetworkedTile((INetworkedTile) entry.getMultipartTile());
        }
        return added;
    }

    @Override
    public boolean doRemoveMultipart(MultipartEntry entry) {
        boolean removed = super.doRemoveMultipart(entry);
        if(entry.getMultipartTile() instanceof INetworkedTile && network != null){
            network.disconnectNetworkedTile((INetworkedTile) entry.getMultipartTile());
        }
        return removed;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType) {
        if(syncType == EnumSyncType.SAVE || syncType == EnumSyncType.SYNC || syncType == EnumSyncType.CHUNK_DATA){
            tag.putInt("networkID", globalNetworkID);
        }
        return super.write(tag, syncType);
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType) {
        globalNetworkID = tag.getInt("networkID");
        return super.read(tag, syncType);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        connectNetwork();
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        disconnectNetwork();
    }

    @Override
    public void remove() {
        super.remove();
        disconnectNetwork();
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() +" "+ getPos().toString();
    }
}
