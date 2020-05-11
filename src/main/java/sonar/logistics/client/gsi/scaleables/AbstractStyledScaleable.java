package sonar.logistics.client.gsi.scaleables;

import sonar.logistics.client.gsi.api.IRenderable;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gsi.properties.ScaleableStyling;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;
import sonar.logistics.client.vectors.Quad2D;

public abstract class AbstractStyledScaleable extends AbstractScaleable implements IRenderable {

    public ScaleableStyling styling = new ScaleableStyling();

    public AbstractStyledScaleable(){}

    public void setStyling(ScaleableStyling styling){
        this.styling = styling;
    }

    @Override
    public void build(Quad2D bounds) {
        this.alignment.build(bounds, styling);
    }

    @Override
    public void render(ScaleableRenderContext context) {
        renderBorders(context, alignment, styling);
    }

    public static void renderBorders(ScaleableRenderContext context, ScaleableBounds alignment, ScaleableStyling styling){
        double marginWidth = styling.marginWidth.getRenderSize(alignment.getBounds().getWidth());
        double marginHeight = styling.marginHeight.getRenderSize(alignment.getBounds().getHeight());

        double endX = alignment.getBounds().getWidth() - marginWidth;
        double endY = alignment.getBounds().getHeight() - marginHeight;

        double borderWidth = styling.borderSize.getRenderSize(alignment.getBounds().getWidth());
        double borderHeight = styling.borderSize.getRenderSize(alignment.getBounds().getHeight());

        ScaleableRenderHelper.renderColouredRect(context, alignment.getBounds(), marginWidth, marginHeight, borderWidth, endY - borderHeight*2, styling.borderColour);
        ScaleableRenderHelper.renderColouredRect(context, alignment.getBounds(), endX-borderWidth, marginHeight, borderWidth, endY - borderHeight*2, styling.borderColour);

        ScaleableRenderHelper.renderColouredRect(context, alignment.getBounds(), marginWidth, marginHeight, endX - borderWidth*2, borderHeight, styling.borderColour);
        ScaleableRenderHelper.renderColouredRect(context, alignment.getBounds(), marginWidth, endY - borderHeight, endX - borderWidth*2, borderHeight, styling.borderColour);
    }
}
