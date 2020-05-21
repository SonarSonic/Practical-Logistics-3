package sonar.logistics.client.gsi.scaleables;

import sonar.logistics.client.gsi.api.IRenderable;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gsi.properties.ScaleableStyling;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;
import sonar.logistics.client.vectors.Quad2D;

public abstract class AbstractStyledScaleable extends AbstractScaleable implements IRenderable, IScaleableComponent {

    public ScaleableStyling styling = new ScaleableStyling();

    public AbstractStyledScaleable(){}

    public void setStyling(ScaleableStyling styling){
        this.styling = styling;
    }

    @Override
    public void build(Quad2D bounds) {
        this.bounds.build(bounds, styling);
    }

    @Override
    public void render(ScaleableRenderContext context, DisplayInteractionHandler handler) {
        ScaleableRenderHelper.renderBorders(context, bounds, styling);
    }
}
