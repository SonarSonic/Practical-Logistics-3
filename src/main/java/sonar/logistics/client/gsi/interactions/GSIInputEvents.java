package sonar.logistics.client.gsi.interactions;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.common.blocks.host.MultipartHostBlock;
import sonar.logistics.common.blocks.host.MultipartHostHelper;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.displays.api.IDisplay;

import javax.annotation.Nullable;

//this class handles all GSI interactions, it uses the DrawHighlightEvent to update the the focused gsi and then uses standard input events on the focused gsi.
public class GSIInputEvents {

    public static GSI focusedGSI = null;

    public static void updateFocusedGSI(@Nullable IDisplay display, @Nullable GSI gsi){
        if(gsi != focusedGSI){
            focusedGSI = gsi;
        }

        if(focusedGSI != null && display != null) {
            focusedGSI.interactionHandler.updateMouseFromDisplay(Minecraft.getInstance().player, display);
        }
    }

    public static void defocusGSI(GSI gsi){
        if(gsi != null){
            gsi.changeFocus(false);
        }
    }

    @SubscribeEvent
    public static void drawHighlights(DrawHighlightEvent.HighlightBlock event){

        if(Minecraft.getInstance().currentScreen != null){
            //if the player is in a gui disable any GSI interactions and defocus the current gsi
            defocusGSI(focusedGSI);
            updateFocusedGSI(null, null);
            return;
        }

        if(focusedGSI != null && focusedGSI.isDragging()){
            //if the player is dragging we will fire the mouse update.
            updateFocusedGSI(focusedGSI.display, focusedGSI);
            return;
        }

        BlockState state = Minecraft.getInstance().world.getBlockState(event.getTarget().getPos());
        if(state.getBlock() instanceof MultipartHostBlock){
            MultipartEntry entry = MultipartHostHelper.getRayTraceMultipart(Minecraft.getInstance().world, event.getTarget().getPos(), Minecraft.getInstance().player);
            if(entry != null && entry.getMultipartTile() instanceof IDisplay){
                IDisplay display = (IDisplay)entry.getMultipartTile();
                if(event.getTarget().getFace() == display.getFacing()){
                    updateFocusedGSI(display, display.getGSI());
                    return;
                }
            }
        }

        //if no display was selected, deselect the focused GSI
        updateFocusedGSI(null, null);

    }

    @SubscribeEvent
    public static void mouseReleased(InputEvent.MouseInputEvent event){
        if(focusedGSI != null){
            switch(event.getAction()){
                case GLFW.GLFW_RELEASE:
                    focusedGSI.interactionHandler.mouseReleased(0, 0, event.getButton());
                    break;
                case GLFW.GLFW_PRESS:
                    focusedGSI.interactionHandler.mouseClicked(0, 0, event.getButton());
                    break;
            }
        }
    }

    @SubscribeEvent
    public static void keyEvent(InputEvent.KeyInputEvent event){
        if(focusedGSI != null){
            switch(event.getAction()){
                case GLFW.GLFW_RELEASE:
                    focusedGSI.interactionHandler.keyReleased(event.getKey(), event.getScanCode(), event.getModifiers());
                    break;
                case GLFW.GLFW_PRESS:
                    focusedGSI.interactionHandler.keyPressed(event.getKey(), event.getScanCode(), event.getModifiers());
                    break;
                case GLFW.GLFW_REPEAT:
                    break;
            }
        }
    }


    @SubscribeEvent
    public static void scrollEvent(InputEvent.MouseScrollEvent event){
        if(focusedGSI != null){
            focusedGSI.interactionHandler.mouseScrolled(0, 0, event.getScrollDelta());
        }
    }
}
