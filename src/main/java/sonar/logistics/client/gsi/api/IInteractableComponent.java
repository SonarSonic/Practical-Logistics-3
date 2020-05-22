package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.interactions.IInteractionListener;

import javax.annotation.Nonnull;

/**implemented on components which don't utilise a default interaction and instead override IInteractionListener themselves*/
public interface IInteractableComponent extends IComponent, IInteractionListener {

    @Nonnull
    @Override
    default IInteractionListener getInteraction(DisplayInteractionHandler context) {
        return this;
    }

    @Override
    default boolean isMouseOver(DisplayInteractionHandler context) {
        return getBounds().renderBounds().contains(context.mousePos);
    }
}
