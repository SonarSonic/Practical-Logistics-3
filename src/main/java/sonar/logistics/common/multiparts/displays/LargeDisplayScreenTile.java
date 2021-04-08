package sonar.logistics.common.multiparts.displays;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.util.vectors.Quad2D;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.server.caches.displays.ConnectedDisplay;
import sonar.logistics.server.caches.displays.ConnectedDisplayManager;
import sonar.logistics.util.network.EnumSyncType;

import javax.annotation.Nullable;

public class LargeDisplayScreenTile extends DisplayScreenTile {

    public int connectedDisplayID = -1;
    public ConnectedDisplay connectedDisplay;

    public LargeDisplayScreenTile(MultipartEntry entry, Quad2D scaling) {
        super(entry, scaling);
    }

    @Nullable
    public ConnectedDisplay getConnectedDisplay(){
        return connectedDisplay;
    }

    public boolean canConnect(){
        return true;///TODO LOCKING
    }

    public void connectNetwork(){
        if(!getHostWorld().isRemote && canConnect()) {
            ConnectedDisplayManager.INSTANCE.connectDisplay(this);
        }
    }

    public void disconnectNetwork(){
        if(!getHostWorld().isRemote) {
            ConnectedDisplayManager.INSTANCE.disconnectDisplay(this);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType) {
        if(syncType == EnumSyncType.SAVE || syncType == EnumSyncType.SYNC || syncType == EnumSyncType.CHUNK_DATA){
            tag.putInt("displayID", connectedDisplayID);
        }
        return super.write(tag, syncType);
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType) {
        connectedDisplayID = tag.getInt("displayID");
        return super.read(tag, syncType);
    }


    @Override
    public void onLoadTick() {
        super.onLoadTick();
        connectNetwork();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        disconnectNetwork();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        disconnectNetwork();
    }

}
