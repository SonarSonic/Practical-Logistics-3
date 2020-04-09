/*
package sonar.logistics.multiparts.displays.gsi;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import sonar.logistics.multiparts.displays.api.IDisplay;
import sonar.logistics.multiparts.displays.old.gsi.interaction.DisplayScreenClick;
import sonar.logistics.multiparts.displays.old.gsi.storage.DisplayElementContainer;

import javax.annotation.Nullable;

public class ClientGSI extends CommonGSI {
    public ClientGSI(IDisplay display, World world, int identity) {
        super(display, world, identity);
    }

    ///// RENDERING \\\\\

    public void render() {
        //fixes brightness issues when transparent blocks are nearby.
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        updateLookElement();
        getViewableContainers().forEach(DisplayElementContainer::render);
    }

    ///// CLICKS & LOOKS \\\\\

    public void updateLookElement() {
        */
/*
        if (lastLookElementUpdate == 0 || (System.currentTimeMillis() - lastLookElementUpdate) > 50) {
            lastLookElementUpdate = System.currentTimeMillis();
            DisplayScreenLook look = GSIOverlays.getCurrentLook(this);
            lookElement = null;
            if (look != null) {
                Tuple<IDisplayElement, double[]> e = getElementFromXY(look.lookX, look.lookY);
                if (e != null && e.getA() instanceof ILookableElement) {
                    lookElement = e.getA();
                    lookX = e.getB()[0];
                    lookY = e.getB()[1];
                }
            }
        }
        *//*

    }

    public boolean onClicked(DisplayScreenClick click) {
        */
/*if (display instanceof ConnectedDisplay && !((ConnectedDisplay) display).canBeRendered.getObject()) {
            player.sendMessage(new TextComponentTranslation("THE DISPLAY IS INCOMPLETE"));
            return true;
        }
        if (mode != grid_mode && (type == BlockInteractionType.SHIFT_RIGHT) || LogisticsHelper.isPlayerUsingOperator(player)) {
            GSIElementPacketHelper.sendGSIPacket(GSIElementPacketHelper.createEditModePacket(!edit_mode.getObject()), -1, this);
            player.sendMessage(new TextComponentTranslation("Edit Mode: " + !edit_mode.getObject()));
            return true;
        }
        DisplayScreenClick click = getClientClick(DisplayVectorHelper.createClick(player, display, type));
        if (click != null) {
            click.doubleClick = wasDoubleClick(world, player);
            if (mode.renderEditContainer() && isEditContainer(click.clickedContainer) && click.clickedElement != null) {
                doDefaultElementClick(part, pos, click, type, player);
                return true;
            }
            if (!mode.onClicked(part, pos, click, type, player) && mode.renderElements() && click.clickedElement != null) {
                List<IInfoError> errors = getErrors(click.clickedElement);
                if (errors != null && !errors.isEmpty()) {
                    player.sendMessage(new TextComponentTranslation(errors.get(0).getDisplayMessage().get(0)));
                    return true;
                } else {
                    doDefaultElementClick(part, pos, click, type, player);
                }
            }
        }*//*

        return false;
    }

    public boolean doDefaultElementClick(DisplayScreenClick click) {
        */
/*if(click.clickedElement instanceof IClickableElement) {
            int gui = ((IClickableElement) click.clickedElement).onGSIClicked(click, player, click.subClickX, click.subClickY);
            if (gui != -1) {
                requestGui(part, world, pos, player, click.clickedElement.getElementIdentity(), gui, new NBTTagCompound());
            }
            return true;
        }*//*

        return false;
    }

    @Nullable
    public DisplayScreenClick getClientClick(@Nullable DisplayScreenClick click){
        */
/*if(click == null){
            return null;
        }
        for (DisplayElementContainer container : containers.values()) {
            if (container.canRender() && container.canClickContainer(click.clickX, click.clickY)) {
                click.clickedContainer = container;
                Tuple<IDisplayElement, double[]> e = container.getElementFromXY(click.clickX, click.clickY);
                if (e != null) {
                    click.clickedElement = e.getA();
                    click.subClickX = e.getB()[0];
                    click.subClickY = e.getB()[1];
                    return click;
                }
            }
        }*//*

        return click;
    }

    ///// VALIDATION \\\\\


    @Override
    public void onGSIValidate() {
        super.onGSIValidate();

        //PL2.proxy.getClientManager().getGSIMap().put(getIdentity(), this);
        //updateCachedInfo();
        updateScaling();
    }

    @Override
    public void onGSIInvalidate() {
        super.onGSIInvalidate();

        //PL2.proxy.getClientManager().getGSIMap().remove(getIdentity());
    }
}
*/
