package sonar.logistics.server.caches.displays;

import com.google.common.collect.Lists;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.logistics.PL3;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.common.multiparts.displays.LargeDisplayScreenTile;
import sonar.logistics.common.multiparts.displays.api.IDisplay;
import sonar.logistics.server.cables.EnumCableTypes;
import sonar.logistics.server.cables.ICableCache;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.util.ListHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConnectedDisplay implements ICableCache<ConnectedDisplay> {

    public List<LargeDisplayScreenTile> displays = new ArrayList<>();
    public final World world;
    public int connectedDisplayID;
    public final EnumCableTypes cableType;

    public ConnectedDisplay(World world, int connectedDisplayID, EnumCableTypes cableType) {
        this.world = world;
        this.connectedDisplayID = connectedDisplayID;
        this.cableType = cableType;
    }

    public void update(){}

    ///// NETWORKING \\\\\

    @Override
    public void deleteCache() {
        displays.forEach(host -> setConnectedDisplay(host, null));
        clear();
        ConnectedDisplayManager.INSTANCE.cached.remove(connectedDisplayID);
    }

    public void clear(){
        displays.clear();
    }

    @Override
    public void shrinkCache() {
        ///TODO IS THIS NECESSARY - DOESN'T IT HAPPEN DUE TO the ADD CABLE in shrink anyway?
        Lists.newArrayList(displays).forEach(display -> { //we copy the list, as it is modified in disconnectHost
            ConnectedDisplay network = ConnectedDisplayManager.INSTANCE.getCableCache(display.getHostWorld(), display.getHostPos(), cableType);
            if(network != this){
                disconnectDisplay(display);
                if(network != null) { //if the network was null the host's network will already have been set to null in disconnectHost
                    network.connectDisplay(display);
                }
            }
        });
        verifyIntegrity();
    }

    @Override
    public void mergeCache(ConnectedDisplay merging) {
        merging.displays.forEach(this::connectDisplay);
        merging.clear();
        ConnectedDisplayManager.INSTANCE.cached.remove(merging.connectedDisplayID);
    }

    public void verifyIntegrity(){
        if(displays.size() == 0){
            deleteCache(); //A PL3 Network is only a Cache, if there is nothing to cache it should be deleted.
            // TODO - if more "Wireless" things are added, hosts might not be the only thing to check.
        }
    }

    @Override
    public void changeCacheID(int networkID) {
        connectedDisplayID = networkID;
        displays.forEach(host -> host.connectedDisplayID = networkID);
    }

    ///// DISPLAYS \\\\\

    public void setConnectedDisplay(LargeDisplayScreenTile display, @Nullable ConnectedDisplay connectedDisplay){
        display.connectedDisplay = connectedDisplay;
        display.connectedDisplayID = connectedDisplay == null ? -1 : connectedDisplay.connectedDisplayID;
        display.queueMarkDirty();
        display.queueSyncPacket();
    }

    public void connectDisplay(LargeDisplayScreenTile display){
        ListHelper.addWithCheck(displays, display);
        setConnectedDisplay(display, this);
    }

    public void disconnectDisplay(LargeDisplayScreenTile display){
        displays.remove(display);
        setConnectedDisplay(display, null);
        verifyIntegrity();
    }

    //// IDisplay \\\\


    public static void dump(@Nullable ConnectedDisplay net){
        PL3.LOGGER.debug("------ CONNECTED DISPLAY DATA START ------");
        if(net == null){
            PL3.LOGGER.debug("NO DISPLAY FOUND");
        }else {
            PL3.LOGGER.debug("DisplayID: {}", net.connectedDisplayID);
            PL3.LOGGER.debug("Dimension: {}", net.world.getDimension().getType());
            PL3.LOGGER.debug("Hosts: {} List: {}",net.displays.size(), net.displays);

        }
        PL3.LOGGER.debug("------ CONNECTED DISPLAY DATA END ------");
    }
}
