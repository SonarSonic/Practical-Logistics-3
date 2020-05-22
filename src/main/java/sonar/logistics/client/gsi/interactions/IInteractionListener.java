package sonar.logistics.client.gsi.interactions;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;

/**can handle gui style interactions in a gui setting but also in the world*/
public interface IInteractionListener {

    default void mouseMoved(DisplayInteractionHandler handler) {}

    default boolean mouseClicked(DisplayInteractionHandler handler, int button) {
        return false;
    }

    default boolean mouseReleased(DisplayInteractionHandler handler, int button) {
        return false;
    }

    default boolean mouseDragged(DisplayInteractionHandler handler, int button, double dragX, double dragY) {
        return false;
    }

    default boolean mouseScrolled(DisplayInteractionHandler handler, double scroll) {
        return false;
    }

    default boolean keyPressed(DisplayInteractionHandler handler, int keyCode, int scanCode, int modifiers) {
        return false;
    }

    default boolean keyReleased(DisplayInteractionHandler handler, int keyCode, int scanCode, int modifiers) {
        return false;
    }

    default boolean charTyped(DisplayInteractionHandler handler, char c, int modifiers) {
        return false;
    }

    default boolean changeFocus(DisplayInteractionHandler handler, boolean focused) {
        return false;
    }

    default boolean isMouseOver(DisplayInteractionHandler handler) {
        return false;
    }

}
