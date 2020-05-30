package sonar.logistics.client.gsi.interactions.api;

import sonar.logistics.client.gsi.render.GSIRenderContext;

/**implemented on components which can handle gui style interactions in a gui setting but also in the world*/
public interface IInteractionListener {

    default void renderInteraction(GSIRenderContext context){}

    ///

    /** only called if the component is "focused" */
    default void mouseMoved() {}

    /** only called if the component is "hovered" */
    default boolean mouseClicked(int button) {
        return false;
    }

    /** only called if the component is "hovered" */
    default boolean mouseReleased(int button) {
        return false;
    }

    /** only called if the component is "focused" */
    default boolean mouseDragged() {
        return false;
    }

    /** only called if the component is "hovered" */
    default boolean mouseScrolled(double scroll) {
        return false;
    }

    /** only called if the component is "focused" */
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /** only called if the component is "focused" */
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /** only called if the component is "focused" */
    default boolean charTyped(char c, int modifiers) {
        return false;
    }

    default boolean changeFocus(boolean focused) {
        return false;
    }

    /**enables clicking / and focusing*/
    default boolean isMouseOver() {
        return false;
    }

    /// special dragging methods

    default boolean isDragButton(int button){
        return button == 0 || button == 1;
    }

    default boolean canStartDrag(int button){
        return isDragButton(button);
    }

    default void onDragStarted(int button){}


    default void onDragFinished(int button){}


    ///

    default void onSettingChanged(Object setting, Object settingObj){}

}
