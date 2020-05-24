package sonar.logistics.client.gsi.interactions.api;

import sonar.logistics.client.gsi.render.GSIRenderContext;

import javax.annotation.Nullable;
import java.util.List;

public interface INestedInteractionListener extends IInteractionListener {


    List<IInteractionListener> getChildren();

    @Nullable
    IInteractionListener getFocused();

    void setFocused(IInteractionListener listener);

    ///

    @Override
    default void renderInteraction(GSIRenderContext context) {
        if(getFocused() != null){
            getFocused().renderInteraction(context);
        }
    }

    @Override
    default void mouseMoved() {
        if(getFocused() != null){
            getFocused().mouseMoved();
        }
    }

    @Override
    default boolean mouseClicked(int button) {
        for(IInteractionListener child : getChildren()){
            if(child.isMouseOver()){
                setFocused(child);
                return child.mouseClicked(button);
            }
        }
        setFocused(null);
        return false;
    }

    @Override
    default boolean mouseReleased(int button) {
        return getFocused() != null && getFocused().mouseReleased(button);
    }

    @Override
    default boolean mouseDragged(int button) {
        return getFocused() != null && getFocused().mouseDragged(button);
    }

    @Override
    default boolean mouseScrolled(double scroll) {
        return getFocused() != null && getFocused().mouseScrolled(scroll);
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return getFocused() != null && getFocused().keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return getFocused() != null && getFocused().keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean charTyped(char c, int modifiers) {
        return getFocused() != null && getFocused().charTyped(c, modifiers);
    }

    ///TODO DO WE REALLY NEED CHANGEFOCUS FOR ANYTHING???
    @Override
    default boolean changeFocus(boolean focused) {
        if(getFocused() != null && getFocused().changeFocus(focused)){
            return true;
        }
        return false;
    }

    @Override
    default boolean isMouseOver() {
        return getFocused() != null && getFocused().isMouseOver();
    }

    @Override
    default boolean isDragging() {
        return getFocused() != null && getFocused().isDragging();
    }

    ///

    @Override
    default void onSettingChanged(Object setting, Object settingObj) {
        if(getFocused() != null){
            getFocused().onSettingChanged(setting, settingObj);
        }
    }
}
