/*
package sonar.logistics.multiparts.displays.gsi;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sonar.logistics.multiparts.displays.api.IDisplay;
import sonar.logistics.multiparts.displays.old.gsi.storage.DisplayElementContainer;

public class ServerGSI extends CommonGSI {


    public ServerGSI(IDisplay display, World world, int identity) {
        super(display, world, identity);
    }

    */
/** creates a new Element Container with the given properties *//*

    public DisplayElementContainer addElementContainer(Vec3d scaling, Vec3d translation, double pScale) {

        int identity = ServerInfoHandler.instance().getNextIdentity();
        DisplayElementContainer container = new DisplayElementContainer(this, scaling, translation, pScale, identity);
        containers.put(identity, container);
        validateContainer(container);
        sendInfoContainerPacket(DisplayGSISavedData.ALL_DATA);

        return container;
    }

    */
/** removes the Element Container with the given id *//*

    public void removeElementContainer(int containerID) {
        invalidateContainer(containers.get(containerID));
        containers.remove(containerID);
        sendInfoContainerPacket(DisplayGSISavedData.ALL_DATA);
    }
    */
/*
    public List<EntityPlayerMP> getWatchers(){
        return DisplayViewerHandler.instance().getWatchingPlayers(this);
    }

    public void forEachWatcher(Consumer<EntityPlayerMP> action){
        getWatchers().forEach(action);
    }

    public List<DisplayGSISavedData> queuedUpdates = new ArrayList<>();

    public void sendInfoContainerPacket(DisplayGSISavedData type) {
        if (world != null && display != null && !world.isRemote) {
            if (!isValid()) {
                return;
            }
            queuedUpdates.add(type);
        }
    }

    public void doQueuedUpdates(){
        if(!queuedUpdates.isEmpty()) {
            DisplayGSISavedData type = queuedUpdates.size()== 1 ? queuedUpdates.get(0) : DisplayGSISavedData.ALL_DATA;
            forEachWatcher(listener -> PL2.network.sendTo(new PacketGSISavedDataPacket(this, type), listener));
            this.display.onInfoContainerPacket();
            queuedUpdates.clear();
        }
    }

    public void sendValidatePacket(ServerPlayerEntity player) {
        if (display instanceof ConnectedDisplay) {
            PL2.network.sendTo(new PacketGSIConnectedDisplayValidate(this, display), player);
        } else if (display instanceof TileAbstractDisplay) {
            PL2.network.sendTo(new PacketGSIStandardDisplayValidate((TileAbstractDisplay) display, this), player);
        }
    }

    public void sendInvalidatePacket(ServerPlayerEntity player) {
        PL2.network.sendTo(new PacketGSIInvalidate(this), player);
    }

    public void validateElement(IDisplayElement e) {
        super.validateElement(e);
        if(!getWorld().isRemote) {
            updateInfoReferences();
            sendInfoContainerPacket(DisplayGSISavedData.ALL_DATA);
        }
    }

    public void invalidateElement(IDisplayElement e) {
        super.invalidateElement(e);
        if(!getWorld().isRemote) {
            updateInfoReferences();
            sendInfoContainerPacket(DisplayGSISavedData.ALL_DATA);
        }
    }
    *//*


    ///// VALIDATION \\\\\


    @Override
    public void onGSIValidate() {
        super.onGSIValidate();

      */
/*  PL2.proxy.getServerManager().gsiMap.put(getIdentity(), this);
        references.clear();
        updateInfoReferences();
        updateCachedInfo();
        updateScaling();
        if (display instanceof ConnectedDisplay) {
            DisplayHandler.updateWatchers(Lists.newArrayList(), (ConnectedDisplay) display);
        }
        forEachWatcher(this::sendValidatePacket);*//*

    }

    @Override
    public void onGSIInvalidate() {
        super.onGSIInvalidate();
        */
/*
        PL2.proxy.getServerManager().gsiMap.remove(getIdentity());
        DisplayInfoReferenceHandler.doInfoReferenceDisconnect(this, references);
        forEachWatcher(this::sendInvalidatePacket);

         *//*

    }
}
*/
