/*
package sonar.logistics.common.multiparts.displays.gsi;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sonar.logistics.common.multiparts.displays.api.IDisplay;
import sonar.logistics.common.multiparts.displays.old.gsi.interaction.DisplayScreenClick;
import sonar.logistics.common.multiparts.displays.old.gsi.storage.DisplayElementContainer;
import sonar.logistics.common.multiparts.displays.old.info.elements.base.*;
import sonar.logistics.server.data.api.DataUUID;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.utils.network.EnumSyncType;
import sonar.logistics.utils.network.ISyncable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CommonGSI implements ISyncable {

    public final IDisplay display;
    public final World world;
    public int identity;

    public List<DataUUID> references = new ArrayList<>();
    public Map<DataUUID, IData> cachedData = new HashMap<>();
    public Map<Integer, DisplayElementContainer> containers = new HashMap<>();

    public static final int EDIT_CONTAINER_ID = 0;

    // click info
    public long lastClickTime;
    public UUID lastClickUUID;

    // client side - look element
    public IDisplayElement lookElement = null;
    public double lookX, lookY;
    private long lastLookElementUpdate;

    public Vec3d displaySizing;

    public CommonGSI(IDisplay display, World world, int identity) {
        this.display = display;
        this.world = world;
        this.identity = identity;
    }

    ///// HELPER METHODS \\\\\

    public final int getIdentity() {
        return identity;
    }

    public final IDisplay getDisplay() {
        return display;
    }

    public final World getWorld() {
        return world;
    }


    //// MAIN ACTIONS \\\\

    public boolean onClicked(DisplayScreenClick click){
        return true;
    }

    */
/** gets the Element at the given XY, used to know element clicked/hovered *//*

    public Tuple<IDisplayElement, double[]> getElementFromXY(double x, double y) {
        for (DisplayElementContainer container : containers.values()) {
            if (container.canRender() && container.canClickContainer(x, y)) {
                Tuple<IDisplayElement, double[]> e = container.getElementFromXY(x, y);
                if (e != null) {
                    return e;
                }
            }
        }
        return null;
    }

    */
/** if the screen was double clicked, called during the click method, don't call it elsewhere *//*

    private boolean wasDoubleClick(World world, PlayerEntity player) {
        boolean doubleClick = false;
        if (System.currentTimeMillis() - lastClickTime < 10 && player.getUniqueID().equals(lastClickUUID)) {
            doubleClick = true;
        }
        lastClickTime = System.currentTimeMillis()*100;
        lastClickUUID = player.getUniqueID();
        return doubleClick;
    }

    //// GSI/ELEMENT SCALING \\\\

    public void updateScaling() {
        updateDisplayScaling();
        containers.values().forEach(DisplayElementContainer::updateActualScaling);
    }

    public Stream<DisplayElementContainer> getViewableContainers() {
        return containers.values().stream().filter(DisplayElementContainer::canRender);
    }

    public void updateDisplayScaling() {
        displaySizing = display.getScreenSizing();
    }

    public Vec3d getDisplaySizing() {
        if (displaySizing == null) {
            updateDisplayScaling();
        }
        return displaySizing;
    }

    //// INFO REFERENCES \\\\

    public void forEachValidUUID(Consumer<DataUUID> action) {
        references.forEach(action);
    }

    public void forEachElement(Consumer<IDisplayElement> action) {
        forEachContainer(c -> c.getElements().forEach(action));
    }

    public void forEachContainer(Consumer<DisplayElementContainer> action) {
        containers.values().forEach(action);
    }

    */
/*
    public boolean isDisplayingUUID(DataUUID id) {
        return references.contains(id);
    }

    public void onDataChanged(DataUUID uuid, IData info) {
        updateCachedInfo();
        forEachElement(e -> {
            if (e instanceof IInfoReferenceElement) {
                if (((IInfoReferenceElement) e).getInfoReferences().stream().anyMatch(holder -> holder.equals(uuid))) {
                    ((IInfoReferenceElement) e).onInfoReferenceChanged(uuid, info);
                }
            }
        });
    }
    *//*


    //// INFO REFERENCES \\\\
    */
/*
    public void updateInfoReferences() {
        if (!isValid() || getWorld().isRemote) {
            return;
        }
        List<InfoUUID> newReferences = new ArrayList<>();
        forEachElement(element -> {
            if (element instanceof IInfoReferenceElement) {
                ListHelper.addWithCheck(newReferences, ((IInfoReferenceElement) element).getInfoReferences());
            }
        });
        List<InfoUUID> removed = new ArrayList<>();
        for (InfoUUID ref : references) {
            if (!newReferences.contains(ref)) {
                removed.add(ref);
                continue;
            }
            newReferences.remove(ref);
        }
        if (!newReferences.isEmpty() || !removed.isEmpty()) {
            DisplayInfoReferenceHandler.doInfoReferenceConnect(this, newReferences);
            DisplayInfoReferenceHandler.doInfoReferenceDisconnect(this, removed);
            references.addAll(newReferences);
            references.removeAll(removed);
        }
        cleanSavedErrors();
    }

    public void validateAllInfoReferences() {
        updateInfoReferences();
        DisplayInfoReferenceHandler.doInfoReferenceConnect(this, references);

    }

    public void sendConnectedInfo(EntityPlayer player){
        references.forEach(ref -> {
            ILogicListenable listen = ServerInfoHandler.instance().getNetworkTileMap().get(ref.identity);
            if(listen != null){
                listen.getListenerList().addListener(player, ListenerType.TEMPORARY_LISTENER);
            }
        });
    }
    *//*

    //// CACHED INFO \\\\
    */
/*
    public void updateCachedInfo() {
        IInfoManager manager = PL2.proxy.getInfoManager(world.isRemote);
        Map<InfoUUID, IInfo> newCache = new HashMap<>();
        references.forEach(ref -> newCache.put(ref, manager.getInfoMap().getOrDefault(ref, InfoError.noData)));
        cachedInfo = newCache;
    }

    public IInfo getCachedInfo(InfoUUID uuid) {
        return cachedInfo.get(uuid);
    }
    *//*

    //// GUIS \\\\

    */
/*
    public void requestGui(TileAbstractDisplay display, World world, BlockPos pos, EntityPlayer player, int elementIdentity, int guiID, NBTTagCompound guiTag) {
        GSIElementPacketHelper.sendGSIPacket(GSIElementPacketHelper.createGuiRequestPacket(guiID, guiTag), elementIdentity, this);
    }

    public IDisplayElement getElementFromIdentity(int identity) {
        for (DisplayElementContainer c : containers.values()) {
            IDisplayElement e = c.getElements().getElementFromIdentity(identity);
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    public IFlexibleGui getElementFromGuiPacket(IDisplayTile obj, int containerID, int elementID, World world, EntityPlayer player, NBTTagCompound tag) {
        DisplayElementContainer c = getContainer(containerID);
        if (c != null) {
            IDisplayElement e = c.getElements().getElementFromIdentity(elementID);
            if (e instanceof IFlexibleGui) {
                return (IFlexibleGui) e;
            }
        }
        return null;
    }

    public void onGuiOpened(IDisplayTile obj, int id, World world, PlayerEntity player, CompoundNBT tag) {
        int containerID = tag.contains("CONT_ID") ? tag.getInt("CONT_ID") : -1;
        int elementID = tag.contains("ELE_ID") ? tag.getInt("ELE_ID") : -1;
        if (containerID == -1 || elementID == -1) {
            switch (id) {
                case 0:
                case 1:
                    TileAbstractDisplay display = (TileAbstractDisplay) obj.getActualDisplay();
                    InfoPacketHelper.sendLocalProvidersFromScreen(display, world, display.getPos(), player);
                    break;
                case 2:

                    break;
            }
        } else {
            IFlexibleGui guiHandler = getElementFromGuiPacket(obj, containerID, elementID, world, player, tag);
            if (guiHandler != null) {
                guiHandler.onGuiOpened(obj, id, world, player, tag);
            }
        }
    }

    public Object getServerElement(IDisplayTile obj, int id, World world, PlayerEntity player, CompoundNBT tag) {
        int containerID = tag.contains("CONT_ID") ? tag.getInt("CONT_ID") : -1;
        int elementID = tag.contains("ELE_ID") ? tag.getInt("ELE_ID") : -1;
        if (containerID == -1 || elementID == -1) {
            switch (id) {
                case 0:
                case 1:
                    TileAbstractDisplay display = (TileAbstractDisplay) obj.getActualDisplay();
                    return new ContainerMultipartSync(display);
                case 2:
                    if(obj instanceof TileAbstractHolographicDisplay) {
                        TileAbstractHolographicDisplay holographic = (TileAbstractHolographicDisplay) obj;
                        return new ContainerMultipartSync(holographic);
                    }
                    break;
            }
        } else {
            IFlexibleGui guiHandler = getElementFromGuiPacket(obj, containerID, elementID, world, player, tag);
            if (guiHandler != null) {
                return guiHandler.getServerElement(obj, id, world, player, tag);
            }
        }
        return null;
    }

    public Object getClientElement(IDisplayTile obj, int id, World world, PlayerEntity player, CompoundNBT tag) {
        int containerID = tag.contains("CONT_ID") ? tag.getInt("CONT_ID") : -1;
        int elementID = tag.contains("ELE_ID") ? tag.getInt("ELE_ID") : -1;
        if (containerID == -1 || elementID == -1) {
            switch (id) {
                case 0:
                    TileAbstractDisplay display = (TileAbstractDisplay) obj.getActualDisplay();
                    return new GuiEditElementsList(this, display);
                case 1:
                    display = (TileAbstractDisplay) obj.getActualDisplay();
                    int element_id = tag.getInteger("clicked");
                    IDisplayElement element = getElementFromIdentity(element_id);
                    return element.getClientEditGui(display, null, world, player);
                case 2:
                    if(obj instanceof TileAdvancedHolographicDisplay){
                        TileAdvancedHolographicDisplay holographic = (TileAdvancedHolographicDisplay)obj;
                        return new GuiHolographicRescaling(new ContainerMultipartSync(holographic), holographic);
                    }
                    if(obj instanceof TileHolographicDisplay){
                        TileHolographicDisplay holographic = (TileHolographicDisplay)obj;
                        return new GuiColourSelection(new ContainerMultipartSync(holographic), holographic, holographic.getScreenColour(), i -> {
                            holographic.setScreenColour(i);
                            holographic.sendPropertiesToServer();
                        });
                    }
                    break;
            }
        } else {
            IFlexibleGui guiHandler = getElementFromGuiPacket(obj, containerID, elementID, world, player, tag);
            if (guiHandler != null) {
                return guiHandler.getClientElement(obj, id, world, player, tag);
            }
        }
        return null;
    }
    *//*

    //// NBT \\\\

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
        //DisplayGSISaveHandler.readGSIData(this, nbt, type, DisplayGSISavedData.ALL_DATA);
        return tag;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
        //DisplayGSISaveHandler.writeGSIData(this, nbt, type, DisplayGSISavedData.ALL_DATA);
        return tag;
    }

    //// ADDITION + VALIDATION \\\\

    public boolean isValid = false;

    public DisplayElementContainer getContainer(int identity) {
        return containers.get(identity);
    }

    public void doAddElement(int containerID, IDisplayElement element) {
        containers.get(containerID).getElements().addElement(element);
        validateElement(element);
    }

    public void validateElement(IDisplayElement element){
        element.validate(this);
    }

    public void doRemoveElement(int containerID, IDisplayElement element) {
        containers.get(containerID).getElements().removeElement(element);
        invalidateElement(element);
    }

    public void invalidateElement(IDisplayElement element){
        element.invalidate(this);
    }

    public void validateContainer(DisplayElementContainer c) {
        if (c != null) {
            c.getElements().forEach(this::validateElement);
        }
    }

    public void invalidateContainer(DisplayElementContainer c) {
        if (c != null) {
            c.getElements().forEach(this::invalidateElement);
        }
    }
    */
/*
    public void removeElement(int identity) {
        IDisplayElement element = getElementFromIdentity(identity);
        if (element != null) {
            IElementStorageHolder holder = element.getHolder();
            holder.getElements().removeElement(element);
            if (holder.getElements().getElementCount() == 0) {
                if (holder instanceof DisplayElementContainer) {
                    containers.remove(((DisplayElementContainer) holder).getContainerIdentity());
                } else if (holder instanceof IDisplayElement) {
                    removeElement(((IDisplayElement) holder).getElementIdentity());
                }
            }
        }
    }
    *//*


    public void validate() {
        if (!isValid) {
            isValid = true;
            forEachElement(this::validateElement);
            display.onGSIValidate();
            onGSIValidate();
        }
    }

    public void onGSIValidate(){ }

    public void invalidate() {
        if (isValid) {
            isValid = false;
            forEachElement(this::invalidateElement);
            display.onGSIInvalidate();
            onGSIInvalidate();
        }
    }

    public void onGSIInvalidate(){}
}
*/
