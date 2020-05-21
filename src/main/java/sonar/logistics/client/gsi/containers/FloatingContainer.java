package sonar.logistics.client.gsi.containers;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.vectors.Quad2D;

/**the position of other components doesn't affect the position of others, they can float over each other*/
public class FloatingContainer extends AbstractContainer {

    //TODO public ComponentStorage storage = new ComponentStorage(this);

    public FloatingContainer() {
        super();
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        subComponents.forEach(c -> c.build(bounds));
    }

    @Override
    public void render(ScaleableRenderContext context, DisplayInteractionHandler handler) {
        subComponents.forEach(e -> {
            if(e.canRender(context, handler)) {
                e.render(context, handler);
            }
        });
    }
}