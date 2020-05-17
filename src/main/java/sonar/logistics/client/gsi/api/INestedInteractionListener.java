package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

//TODO MAKE THIS CUSTOM - This is currently a complete copy of INestedGuiEventListener
public interface INestedInteractionListener extends IInteractionListener {
    
    List<IScaleableComponent> children();

    default Optional<IInteractionListener> getEventListenerForPos(DisplayInteractionHandler context) {
        Iterator<IScaleableComponent> it = this.children().iterator();

        IInteractionListener listener;
        do {
            if (!it.hasNext()) {
                return Optional.empty();
            }

            listener = it.next().getInteraction(context);
        } while(listener == null || !listener.isMouseOver(context));

        return Optional.of(listener);
    }

    default boolean mouseClicked(DisplayInteractionHandler context, int button) {
        Iterator<IScaleableComponent> it = this.children().iterator();

        IInteractionListener listener;
        IScaleableComponent component;
        do {
            if (!it.hasNext()) {
                return false;
            }
            component = it.next();
            listener = component.getInteraction(context);
        } while(!listener.mouseClicked(context, button));

        this.setFocused(component);
        if (button == 0) {
            this.setDragging(true);
        }

        return true;
    }

    default boolean mouseReleased(DisplayInteractionHandler context, int button) {
        this.setDragging(false);
        return this.getEventListenerForPos(context).filter((listener) -> {
            return listener.mouseReleased(context, button);
        }).isPresent();
    }

    default boolean mouseDragged(DisplayInteractionHandler context, int button, double dragX, double dragY) {
        return this.getFocused() != null && this.isDragging() && button == 0 ? this.getFocused().getInteraction(context).mouseDragged(context, button, dragX, dragY) : false;
    }

    boolean isDragging();

    void setDragging(boolean var1);

    default boolean mouseScrolled(DisplayInteractionHandler context, double scroll) {
        return this.getEventListenerForPos(context).filter((listener) -> {
            return listener.mouseScrolled(context, scroll);
        }).isPresent();
    }

    default boolean keyPressed(DisplayInteractionHandler context, int keyCode, int scanCode, int modifiers) {
        return this.getFocused() != null && this.getFocused().getInteraction(context).keyPressed(context, keyCode, scanCode, modifiers);
    }

    default boolean keyReleased(DisplayInteractionHandler context, int keyCode, int scanCode, int modifiers) {
        return this.getFocused() != null && this.getFocused().getInteraction(context).keyReleased(context, keyCode, scanCode, modifiers);
    }

    default boolean charTyped(DisplayInteractionHandler context, char c, int modifiers) {
        return this.getFocused() != null && this.getFocused().getInteraction(context).charTyped(context, c, modifiers);
    }

    @Nullable
    IScaleableComponent getFocused();

    void setFocused(@Nullable IScaleableComponent var1);

    default boolean changeFocus(DisplayInteractionHandler context, boolean change) {
        IScaleableComponent focused = this.getFocused();
        boolean hasFocused = focused != null;
        if (hasFocused && focused.getInteraction(context).changeFocus(context, change)) {
            return true;
        } else {
            List<IScaleableComponent> components = this.children();
            int index = components.indexOf(focused);
            int newIndex;
            if (hasFocused && index >= 0) {
                newIndex = index + (change ? 1 : 0);
            } else if (change) {
                newIndex = 0;
            } else {
                newIndex = components.size();
            }

            ListIterator<IScaleableComponent> iterator = components.listIterator(newIndex);
            BooleanSupplier canContinue = change ? iterator::hasNext : iterator::hasPrevious;
            Supplier<IScaleableComponent> nextComponent = change ? iterator::next : iterator::previous;

            IScaleableComponent component;
            IInteractionListener listener;
            do {
                if (!canContinue.getAsBoolean()) {
                    this.setFocused(null);
                    return false;
                }
                component = nextComponent.get();
                listener = component.getInteraction(context);
            } while(!listener.changeFocus(context, change));

            this.setFocused(component);
            return true;
        }
    }
}
