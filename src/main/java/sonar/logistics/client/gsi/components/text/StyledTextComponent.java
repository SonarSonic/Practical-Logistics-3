package sonar.logistics.client.gsi.components.text;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.render.*;
import sonar.logistics.client.gsi.context.DisplayClickContext;
import sonar.logistics.client.gsi.context.DisplayInteractionContext;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

import javax.annotation.Nullable;
import java.util.function.Function;

public class StyledTextComponent extends AbstractStyledScaleable implements IScaleableComponent, IGlyphRenderer {

    @OnlyIn(Dist.CLIENT)
    public ScaledFontType fontType = ScaledFontType.DEFAULT_MINECRAFT;

    public StyledTextString glyphString = new StyledTextString();
    public StyledTextWrapper textWrapper = new StyledTextWrapper();
    public IGlyphRenderer specialGlyphRenderer = null;

    public int page;

    public StyledTextComponent() {}

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        this.textWrapper.build(glyphString, fontType, this.bounds.renderBounds());
    }

    @Override
    public void render(ScaleableRenderContext context) {
        super.render(context);
        context.matrix.push();

        context.matrix.translate(0, 0, -0.01F);
        StyledTextRenderer.renderStyledTextLines(context, fontType, bounds.renderBounds(), textWrapper.styledTextPages.getCurrentPage(page), this);

        context.matrix.pop();
    }

    @Override
    public void renderGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        if(specialGlyphRenderer != null){
            specialGlyphRenderer.renderGlyph(context, glyphInfo);
            return;
        }
        glyphInfo.glyph.render(context, glyphInfo);
    }

    IGlyphType hoveredGlyph = null;
    IGlyphType clickedGlyph = null;

    @Override
    public boolean onHovered(DisplayInteractionContext context) {
        GlyphRenderInfo glyphHit = getGlyphHit(context.displayHit, g -> g.quad.contains(context.displayHit) && StyledTextPages.FILTER_VISIBLE.apply(g));
        if(glyphHit != null){
            clickedGlyph = glyphHit.glyph;
            return true;
        }
        return false;
    }

    @Override
    public boolean onClicked(DisplayClickContext context) {
        GlyphRenderInfo glyphHit = getGlyphHit(context.displayHit, g -> g.quad.contains(context.displayHit) && StyledTextPages.FILTER_VISIBLE.apply(g));
        if(glyphHit != null){
            clickedGlyph = glyphHit.glyph;
            return true;
        }
        return false;
    }

    public StyledTextLine getInteractedLine(Vector2D textHit){
        for (StyledTextLine line : textWrapper.styledTextPages.getCurrentPage(page)) {
            if (line.renderSize.contains(textHit)) {
                return line;
            }
        }
        return null;
    }

    @Nullable
    public GlyphRenderInfo getGlyphHit(Vector2D textHit, Function<GlyphRenderInfo, Boolean> filter){
        StyledTextLine line = getInteractedLine(textHit);
        if(line != null){
            return getGlyphHit(line, filter);
        }
        return null;
    }

    @Nullable
    public GlyphRenderInfo getGlyphHit(StyledTextLine line, Function<GlyphRenderInfo, Boolean> filter){
        for(GlyphRenderInfo glyphInfo : line.glyphInfo){
            if(filter.apply(glyphInfo)){
                return glyphInfo;
            }
        }
        return null;
    }

}
