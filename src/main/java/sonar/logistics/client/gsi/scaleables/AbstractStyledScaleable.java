package sonar.logistics.client.gsi.scaleables;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.api.IRenderable;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.properties.ScaleableAlignment;
import sonar.logistics.client.gsi.properties.ScaleableStyling;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

public abstract class AbstractStyledScaleable extends AbstractScaleable implements IRenderable {

    public ScaleableStyling styling = new ScaleableStyling();

    public AbstractStyledScaleable(){}

    public void setStyling(ScaleableStyling styling){
        this.styling = styling;
    }

    @Override
    public void build(Vec3d alignment, Vec3d maxSizing) {
        this.alignment.build(alignment, maxSizing, styling);
    }

    @Override
    public void render(ScaleableRenderContext context) {
        renderBorders(context, alignment, styling);
    }

    public static void renderBorders(ScaleableRenderContext context, ScaleableAlignment alignment, ScaleableStyling styling){
        float marginWidth = styling.marginWidth.getRenderSize((float)alignment.getSizing().getX());
        float marginHeight = styling.marginHeight.getRenderSize((float)alignment.getSizing().getY());

        float endX = (float)alignment.getSizing().getX() - marginWidth;
        float endY = (float)alignment.getSizing().getY() - marginHeight;

        float borderWidth = styling.borderSize.getRenderSize((float)alignment.getSizing().getX());
        float borderHeight = styling.borderSize.getRenderSize((float)alignment.getSizing().getY());

        ScaleableRenderHelper.renderColouredRect(context, alignment.getAlignment(), marginWidth, marginHeight, borderWidth, endY - borderHeight*2, styling.borderColour);
        ScaleableRenderHelper.renderColouredRect(context, alignment.getAlignment(), endX-borderWidth, marginHeight, borderWidth, endY - borderHeight*2, styling.borderColour);

        ScaleableRenderHelper.renderColouredRect(context, alignment.getAlignment(), marginWidth, marginHeight, endX - borderWidth*2, borderHeight, styling.borderColour);
        ScaleableRenderHelper.renderColouredRect(context, alignment.getAlignment(), marginWidth, endY - borderHeight, endX - borderWidth*2, borderHeight, styling.borderColour);
    }
}
