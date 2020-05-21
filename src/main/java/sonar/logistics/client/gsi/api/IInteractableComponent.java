package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;

import javax.annotation.Nonnull;

public interface IInteractableComponent extends IScaleableComponent, IInteractionListener {

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
