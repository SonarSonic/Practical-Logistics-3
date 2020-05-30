package sonar.logistics.client.gsi.interactions.api;

import sonar.logistics.client.gsi.api.IComponent;

public interface IInteractionComponent extends IInteractionListener, IComponent {

    @Override
    default boolean isMouseOver() {
        return getBounds().maxBounds().contains(getMousePos());
    }

}
