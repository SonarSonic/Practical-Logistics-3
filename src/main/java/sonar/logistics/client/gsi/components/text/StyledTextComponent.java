package sonar.logistics.client.gsi.components.text;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.render.*;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;
import sonar.logistics.client.vectors.Quad2D;

public class StyledTextComponent extends AbstractStyledScaleable implements IScaleableComponent {

    @OnlyIn(Dist.CLIENT)
    public ScaledFontType fontType = ScaledFontType.DEFAULT_MINECRAFT;
    public StyledTextPages pages = new StyledTextPages(new StyledTextString());

    public StyledTextComponent() {}

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        StyledTextWrapper.INSTANCE.build(pages, fontType, this.bounds.renderBounds());
    }

    @Override
    public void render(ScaleableRenderContext context, DisplayInteractionHandler interact) {
        super.render(context, interact);
        context.matrix.push();
        context.matrix.translate(0, 0, -0.01F);
        StyledTextRenderer.renderCurrentPage(context, fontType, pages);
        context.matrix.pop();
    }
}
