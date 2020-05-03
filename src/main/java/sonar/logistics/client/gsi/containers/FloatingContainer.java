package sonar.logistics.client.gsi.containers;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;

/**the position of other components doesn't affect the position of others, they can float over each other*/
public class FloatingContainer extends AbstractContainer {

    //TODO public ComponentStorage storage = new ComponentStorage(this);

    public FloatingContainer() {
        super();
    }

    @Override
    public void build(Vec3d alignment, Vec3d maxSizing) {
        super.build(alignment, maxSizing);
        subComponents.forEach(c -> c.build(alignment, maxSizing));
    }

    @Override
    public void render(ScaleableRenderContext context) {
        subComponents.forEach(e -> {
            if(e.canRender(context)) {
                e.render(context);
            }
        });
    }
}