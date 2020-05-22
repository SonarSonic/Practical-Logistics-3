package sonar.logistics.client.gui.api;

import net.minecraft.client.gui.IGuiEventListener;

import javax.annotation.Nullable;
import java.util.List;

/**allows for flexibly changing the type of interactions which take place with the widget, mainly for neatness and avoiding repeated work*/
public interface IMultipleGuiEventListener extends IGuiEventListener {

    List<? extends IGuiEventListener> getEventListeners();

    @Nullable
    default IGuiEventListener getEventListenerAt(double mouseX, double mouseY){
        for(IGuiEventListener e : getEventListeners()){
            if(e.isMouseOver(mouseX, mouseY)){
                return e;
            }
        }
        return null;
    }

    default void mouseMoved(double mouseX, double mouseY) {
        IGuiEventListener e = getEventListenerAt(mouseX, mouseY);
        if(e != null){
            e.mouseMoved(mouseX, mouseY);
        }
    }

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        IGuiEventListener e = getEventListenerAt(mouseX, mouseY);
        if(e != null){
            return e.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        IGuiEventListener e = getEventListenerAt(mouseX, mouseY);
        if(e != null){
            return e.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        IGuiEventListener e = getEventListenerAt(mouseX, mouseY);
        if(e != null){
            return e.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return false;
    }

    default boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        IGuiEventListener e = getEventListenerAt(mouseX, mouseY);
        if(e != null){
            return e.mouseScrolled(mouseX, mouseY, scroll);
        }
        return false;
    }

    default boolean keyPressed(int key, int scanCode, int modifiers) {
        for(IGuiEventListener e : getEventListeners()){
            if(e.keyPressed(key, scanCode, modifiers)){
                return true;
            }
        }
        return false;
    }

    default boolean keyReleased(int key, int scanCode, int modifiers) {
        for(IGuiEventListener e : getEventListeners()){
            if(e.keyReleased(key, scanCode, modifiers)){
                return true;
            }
        }
        return false;
    }

    default boolean charTyped(char aChar, int modifiers) {
        for(IGuiEventListener e : getEventListeners()){
            if(e.charTyped(aChar, modifiers)){
                return true;
            }
        }
        return false;
    }

    default boolean changeFocus(boolean focused) {
        for(IGuiEventListener e : getEventListeners()){
            if(e.changeFocus(focused)){
                return true;
            }
        }
        return false;
    }

    default boolean isMouseOver(double mouseX, double mouseY) {
        return getEventListenerAt(mouseX, mouseY) != null;
    }

}
