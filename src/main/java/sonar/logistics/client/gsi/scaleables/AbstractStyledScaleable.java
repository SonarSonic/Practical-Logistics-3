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
        renderBorders(context, bounds, styling);
    }

    public static void renderBorders(ScaleableRenderContext context, ComponentBounds alignment, ScaleableStyling styling){
        double marginWidth = styling.marginWidth.getRenderSize(alignment.maxBounds().getWidth());
        double marginHeight = styling.marginHeight.getRenderSize(alignment.maxBounds().getHeight());

        double endX = alignment.maxBounds().getWidth() - marginWidth;
        double endY = alignment.maxBounds().getHeight() - marginHeight;

        double borderWidth = styling.borderSize.getRenderSize(alignment.maxBounds().getWidth());
        double borderHeight = styling.borderSize.getRenderSize(alignment.maxBounds().getHeight());

        ScaleableRenderHelper.renderColouredRect(context, alignment.maxBounds(), marginWidth, marginHeight, borderWidth, endY - borderHeight*2, styling.borderColour);
        ScaleableRenderHelper.renderColouredRect(context, alignment.maxBounds(), endX-borderWidth, marginHeight, borderWidth, endY - borderHeight*2, styling.borderColour);

        ScaleableRenderHelper.renderColouredRect(context, alignment.maxBounds(), marginWidth, marginHeight, endX - borderWidth*2, borderHeight, styling.borderColour);
        ScaleableRenderHelper.renderColouredRect(context, alignment.maxBounds(), marginWidth, endY - borderHeight, endX - borderWidth*2, borderHeight, styling.borderColour);
    }
}
