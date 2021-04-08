package sonar.logistics.client.gui.widgets;

import net.minecraft.client.gui.IGuiEventListener;

/**allows for flexibly changing the type of interactions which take place with the widget, mainly for neatness and avoiding repeated work*/
public interface IFlexibleGuiEventListener extends IGuiEventListener {

    IGuiEventListener getEventListener();

    default void mouseMoved(double mouseX, double mouseY) {
        getEventListener().mouseMoved(mouseX, mouseY);
    }

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return getEventListener().mouseClicked(mouseX, mouseY, button);
    }

    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return getEventListener().mouseReleased(mouseX, mouseY, button);
    }

    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return getEventListener().mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    default boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return getEventListener().mouseScrolled(mouseX, mouseY, scroll);
    }

    default boolean keyPressed(int key, int scanCode, int modifiers) {
        return getEventListener().keyPressed(key, scanCode, modifiers);
    }

    default boolean keyReleased(int key, int scanCode, int modifiers) {
        return getEventListener().keyReleased(key, scanCode, modifiers);
    }

    default boolean charTyped(char aChar, int modifiers) {
        return getEventListener().charTyped(aChar, modifiers);
    }

    default boolean changeFocus(boolean focused) {
        return getEventListener().changeFocus(focused);
    }

    default boolean isMouseOver(double mouseX, double mouseY) {
        return getEventListener().isMouseOver(mouseX, mouseY);
    }

}
