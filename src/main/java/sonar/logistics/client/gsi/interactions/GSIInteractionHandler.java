package sonar.logistics.client.gsi.interactions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.util.vectors.Vector2D;
import sonar.logistics.util.vectors.VectorHelper;
import sonar.logistics.common.multiparts.displays.api.IDisplay;

import javax.annotation.Nullable;

public class GSIInteractionHandler implements IGuiEventListener {

    public GSI gsi;
    public PlayerEntity player;
    private InteractionType interactionType = InteractionType.WORLD_INTERACTION;

    public Vector2D mousePos = new Vector2D(-1, -1);

    public boolean isDoubleClick = false;
    public boolean isTripleClick = false;
    public long lastClickTime = -1;

    public GSIInteractionHandler(GSI gsi, PlayerEntity player){
        this.gsi = gsi;
        this.player = player;
    }

    public InteractionType getInteractionType(){
        return interactionType;
    }

    public void setInteractionType(InteractionType interactionType){
        this.interactionType = interactionType;
        this.gsi.setFocused(null);
    }

    ///

    public void updateMouseFromGui(double mouseX, double mouseY){
        if(!interactionType.isUsingGui()){
            setInteractionType(InteractionType.GUI_INTERACTION);
        }
        update(new Vector2D(mouseX, mouseY));
    }

    public void updateMouseFromDisplay(PlayerEntity player, IDisplay display){
        if(Minecraft.getInstance().currentScreen == null) { //only update from the display if no screen is open
            if(interactionType.isUsingGui()){
                setInteractionType(InteractionType.WORLD_INTERACTION);
            }
            update(VectorHelper.getEntityLook(player, display, 8, false));
        }
    }

    public void update(@Nullable  Vector2D mousePos){ //click position relative to the display
        this.mousePos = mousePos == null ? new Vector2D(-1, -1 ): mousePos;
        //
    }

    ///

    public boolean hasShiftDown(){
        if(getInteractionType().isUsingGui()){
            return Screen.hasShiftDown();
        }
        return player.isSneaking();
    }

    public boolean hasControlDown() {
        if(getInteractionType().isUsingGui()){
            return Screen.hasControlDown();
        }
        return false;
    }

    public boolean hasAltDown(){
        if(getInteractionType().isUsingGui()){
            return Screen.hasAltDown();
        }
        return false;
    }

    ///

    public boolean trigger(Object source, int triggerId){
        return gsi.toggle(source, triggerId);
    }

    public boolean isActive(Object source, int triggerId){
        return gsi.isActive(source, triggerId);
    }

    /// IGuiEventListener

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        gsi.mouseMoved();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        long currentTime = System.currentTimeMillis();
        //500ms is the windows default, next click after triple is a normal click
        if(!isTripleClick && lastClickTime != -1 && currentTime - lastClickTime < 500){
            isTripleClick = isDoubleClick; //if it already was a double click it's must be a triple!
            isDoubleClick = !isTripleClick;
        }else{
            isDoubleClick = false;
            isTripleClick = false;
        }
        lastClickTime = currentTime;
        System.out.println("TRIPLE: " + isTripleClick + " DOUBLE: " + isDoubleClick);
        return gsi.mouseClicked(button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return gsi.mouseReleased(button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        //dragging is now performed by the GSI itself, this allows more consistency between World & Gui interactions.
        return false; //return gsi.mouseDragged(button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return gsi.mouseScrolled(scroll);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return gsi.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return gsi.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int modifiers) {
        return gsi.charTyped(c, modifiers);
    }

    @Override
    public boolean changeFocus(boolean change) {
        return gsi.changeFocus(change);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return gsi.isMouseOver();
    }


    public enum InteractionType{

        WORLD_INTERACTION,
        GUI_INTERACTION,
        GUI_EDITING;

        public boolean isUsingGui(){
            return this != WORLD_INTERACTION;
        }

    }
}