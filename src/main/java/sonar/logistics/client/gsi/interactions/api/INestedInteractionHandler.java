package sonar.logistics.client.gsi.interactions.api;

import sonar.logistics.client.gsi.render.GSIRenderContext;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;


/// similar to INestedGuiEventHandler for IInteractionListeners - however to be more consistent all interactions will check isMouseOver first
public interface INestedInteractionHandler extends IInteractionHandler {

    List<IInteractionHandler> getChildren();

    void setFocused(IInteractionHandler listener);

    Optional<IInteractionHandler> getFocusedListener();

    Optional<IInteractionHandler> getHoveredListener();

    ///

    @Override
    default void renderInteraction(GSIRenderContext context) {
        getFocusedListener().ifPresent(l -> l.renderInteraction(context));
    }

    @Override
    default void mouseMoved() {
        getFocusedListener().ifPresent(IInteractionHandler::mouseMoved);
    }

    @Override
    default boolean mouseClicked(int button) {
        //first gets the interaction listener at the mouse
        Optional<IInteractionHandler> listener = getHoveredListener();
        //update focus
        setFocused(listener.orElse(null));
        ///updates drag: before mouse clicked allowing drag variables to be set within the listeners mouse clicked method
        tryStartDragging(button);
        return listener.filter(l -> l.mouseClicked(button)).isPresent();
    }

    @Override
    default boolean mouseReleased(int button) {
        tryEndDragging(button);
        return this.getHoveredListener().filter(l -> l.mouseReleased(button)).isPresent();
    }

    @Override
    default boolean mouseDragged() {
        if(!isDragging()){
            return false;
        }
        return getFocusedListener().filter(IInteractionHandler::mouseDragged).isPresent();
    }

    @Override
    default boolean mouseScrolled(double scroll) {
        return this.getHoveredListener().filter(l -> l.mouseScrolled(scroll)).isPresent();
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return getFocusedListener().filter(l -> l.keyPressed(keyCode, scanCode, modifiers)).isPresent();
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return getFocusedListener().filter(l -> l.keyReleased(keyCode, scanCode, modifiers)).isPresent();
    }

    @Override
    default boolean charTyped(char c, int modifiers) {
        return getFocusedListener().filter(l -> l.charTyped(c, modifiers)).isPresent();
    }

    @Override
    default boolean changeFocus(boolean focused) {
        Optional<IInteractionHandler> focusedListener = this.getFocusedListener();
        if (focusedListener.filter(l -> l.changeFocus(focused)).isPresent()) {
            return true;
        } else {
            List<? extends IInteractionHandler> children = this.getChildren();
            int focusedIndex = children.indexOf(focusedListener.orElse(null));
            int startIndex;
            if (focusedListener.isPresent() && focusedIndex >= 0) {
                startIndex = focusedIndex + (focused ? 1 : 0);
            } else if (focused) {
                startIndex = 0;
            } else {
                startIndex = children.size();
            }

            ListIterator<? extends IInteractionHandler> it = children.listIterator(startIndex);
            BooleanSupplier booleanSupplier = focused ? it::hasNext : it::hasPrevious;
            Supplier<? extends IInteractionHandler> supplier = focused ? it::next : it::previous;

            IInteractionHandler listener;
            do {
                if (!booleanSupplier.getAsBoolean()) {
                    this.setFocused(null);
                    return false;
                }

                listener = supplier.get();
            } while(!listener.changeFocus(focused));

            this.setFocused(listener);
            return true;
        }
    }

    @Override
    default boolean isMouseOver() {
        return getHoveredListener().isPresent();
    }

    //special dragging methods

    default void tryStartDragging(int button){
        if(!isDragging()) {
            if(!getFocusedListener().filter(l -> l.canStartDrag(button)).isPresent()){
                return;
            }
            setDragging(true);
            getFocusedListener().ifPresent(l -> l.onDragStarted(button));
        }
    }

    default void tryEndDragging(int button){
        if(isDragging()){
            if(!getFocusedListener().filter(l -> l.isDragButton(button)).isPresent()){
                return;
            }
            setDragging(false);
            getFocusedListener().ifPresent(l -> l.onDragFinished(button));
        }
    }

    boolean isDragging();

    void setDragging(boolean var1);

    ///

    @Override
    default void onSettingChanged(Object setting, Object settingObj) {
        getChildren().forEach(listener -> listener.onSettingChanged(setting, settingObj));
    }
}
