package sonar.logistics.client.gsi.interactions.api;

import sonar.logistics.client.gsi.render.GSIRenderContext;

import javax.annotation.Nullable;

/**implemented on components which use more advanced interactions and so switch the interaction used based on the mouse pos or context (e.g. gui/world)*/
public interface IFlexibleInteractionListener extends IInteractionListener {

    /**this shouldn't never return itself*/
    @Nullable
    IInteractionListener getInteractionListener();

    @Override
    default void renderInteraction(GSIRenderContext context) {
        if(getInteractionListener() != null){
            getInteractionListener().renderInteraction(context);
        }
    }

    @Override
    default void mouseMoved() {
        if(getInteractionListener() != null){
            getInteractionListener().mouseMoved();
        }
    }

    @Override
    default boolean mouseClicked(int button) {
        return getInteractionListener() != null && getInteractionListener().mouseClicked(button);
    }

    @Override
    default boolean mouseReleased(int button) {
        return getInteractionListener() != null && getInteractionListener().mouseReleased(button);
    }

    @Override
    default boolean mouseDragged(int button) {
        return getInteractionListener() != null && getInteractionListener().mouseDragged(button);
    }

    @Override
    default boolean mouseScrolled(double scroll) {
        return getInteractionListener() != null && getInteractionListener().mouseScrolled(scroll);
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return getInteractionListener() != null && getInteractionListener().keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return getInteractionListener() != null && getInteractionListener().keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean charTyped(char c, int modifiers) {
        return getInteractionListener() != null && getInteractionListener().charTyped(c, modifiers);
    }

    @Override
    default boolean changeFocus(boolean focused) {
        return getInteractionListener() != null && getInteractionListener().changeFocus(focused);
    }

    @Override
    default boolean isMouseOver() {
        return getInteractionListener() != null && getInteractionListener().isMouseOver();
    }

    @Override
    default boolean isDragging() {
        return getInteractionListener() != null && getInteractionListener().isDragging();
    }

    ///


    @Override
    default void onSettingChanged(Object setting, Object settingObj) {
        if(getInteractionListener() != null){
            getInteractionListener().onSettingChanged(setting, settingObj);
        }
    }
}
