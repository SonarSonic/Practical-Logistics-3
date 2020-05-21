package sonar.logistics.client.gsi.context;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.entity.player.PlayerEntity;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IInteractionListener;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.vectors.Vector2D;
import sonar.logistics.client.vectors.VectorHelper;
import sonar.logistics.common.multiparts.displays.api.IDisplay;

import javax.annotation.Nullable;

public class DisplayInteractionHandler implements IGuiEventListener {

    public GSI gsi;
    public PlayerEntity player;
    public boolean isUsingGui;

    public Vector2D mousePos = new Vector2D(-1, -1);
    public IScaleableComponent hovered;

    public DisplayInteractionHandler(GSI gsi, PlayerEntity player, boolean isUsingGui){
        this.gsi = gsi;
        this.player = player;
        this.isUsingGui = isUsingGui;
    }

    ///

    public void updateMouseFromGui(double mouseX, double mouseY){
        update(new Vector2D(mouseX, mouseY));
    }

    public void updateMouseFromDisplay(PlayerEntity player, IDisplay display){
        update(VectorHelper.getEntityLook(player, display, 8));
    }

    public void update(@Nullable  Vector2D mousePos){ //click position relative to the display
        if(mousePos == null || !mousePos.equals(this.mousePos)) {
            this.mousePos = mousePos == null ? new Vector2D(-1, -1 ): mousePos;
            this.hovered = mousePos == null ? null : gsi.getInteractedComponent(this);
        }
    }

    ///

    public boolean hasShiftDown(){
        return player.isSneaking();
    }

    public boolean hasControlDown() {
        return false;
    }

    public boolean hasAltDown(){
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
        gsi.mouseMoved(this);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return gsi.mouseClicked(this, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return gsi.mouseReleased(this, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return gsi.mouseDragged(this, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return gsi.mouseScrolled(this, scroll);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return gsi.keyPressed(this, keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return gsi.keyReleased(this, keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int modifiers) {
        return gsi.charTyped(this, c, modifiers);
    }

    @Override
    public boolean changeFocus(boolean change) {
        return gsi.changeFocus(this, change);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return gsi.isMouseOver(this);
    }
}