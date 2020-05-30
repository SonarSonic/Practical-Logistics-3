package sonar.logistics.client.gsi.components.groups;

import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.interactions.api.INestedInteractionListener;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.vectors.Quad2D;


public class BasicGroup extends AbstractGroup implements INestedInteractionListener {

    /**if the container is inline the components will be built relative to the container not the gsi*/
    public boolean inlineBlock = true;
    public boolean isMoveable = false;

    public BasicGroup() {
        super();
    }

    @Override
    public boolean isMouseOver() {
        return interactions.stream().anyMatch(IInteractionListener::isMouseOver);
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        subComponents.forEach(c -> c.build(inlineBlock ? getBounds().renderBounds() : bounds));
    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible) {
            super.render(context);
            subComponents.forEach(e -> e.render(context));
        }
    }

    @Override
    public boolean isMoveable() {
        return isMoveable;
    }
}
