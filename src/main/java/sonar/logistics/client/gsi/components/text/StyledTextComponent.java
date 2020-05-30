package sonar.logistics.client.gsi.components.text;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sonar.logistics.client.gsi.api.ITextComponent;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.render.*;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.interactions.text.StyledTextInteraction;
import sonar.logistics.client.gsi.interactions.api.IFlexibleInteractionListener;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.client.vectors.Quad2D;

public class StyledTextComponent extends AbstractComponent implements ITextComponent, IFlexibleInteractionListener {

    @OnlyIn(Dist.CLIENT)
    public ScaledFontType fontType = ScaledFontType.DEFAULT_MINECRAFT;
    public StyledTextPages pages = new StyledTextPages(new StyledTextString());
    public StyledTextInteraction<ITextComponent> textInteraction = new StyledTextInteraction<>(this);

    public StyledTextComponent() {}

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        StyledTextWrapper.INSTANCE.build(pages, fontType, this.bounds.renderBounds(), new LineStyle(), null);
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        context.matrix.push();
        context.matrix.translate(0, 0, -0.01);
        StyledTextRenderer.INSTANCE.renderCurrentPage(context, fontType, pages);
        context.matrix.pop();
    }

    @Override
    public StyledTextPages pages() {
        return pages;
    }

    @Override
    public IInteractionListener getInteractionListener() {
        return textInteraction;
    }
}