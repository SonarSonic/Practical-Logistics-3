package sonar.logistics.client.gsi.components.containers;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.vectors.Quad2D;

/**the position of other components doesn't affect the position of others, they can float over each other*/
public class FloatingContainer extends AbstractContainer {

    public FloatingContainer() {
        super();
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        subComponents.forEach(c -> c.build(bounds));
    }

    @Override
    public void render(GSIRenderContext context, DisplayInteractionHandler handler) {
        subComponents.forEach(e -> {
            if(e.canRender(context, handler)) {
                e.render(context, handler);
            }
        });
    }
}