package sonar.logistics.client.gsi.interactions.api;

import sonar.logistics.client.gsi.render.GSIRenderContext;

/**implemented on components which can handle gui style interactions in a gui setting but also in the world*/
public interface IInteractionListener {

    default void renderInteraction(GSIRenderContext context){}

    ///

    default void mouseMoved() {}

    default boolean mouseClicked(int button) {
        return false;
    }

    default boolean mouseReleased(int button) {
        return false;
    }

    default boolean mouseDragged(int button) {
        return false;
    }

    default boolean mouseScrolled(double scroll) {
        return false;
    }

    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    default boolean charTyped(char c, int modifiers) {
        return false;
    }

    default boolean changeFocus(boolean focused) {
        return false;
    }

    default boolean isMouseOver() {
        return false;
    }

    default boolean isDragging() {
        return false;
    }

    ///

    default void onSettingChanged(Object setting, Object settingObj){}

}
