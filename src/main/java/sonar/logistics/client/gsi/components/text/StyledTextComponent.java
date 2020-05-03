package sonar.logistics.client.gsi.components.text;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sonar.logistics.client.gsi.api.*;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.api.IGlyphString;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.glyph.StyleGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.context.DisplayClickContext;
import sonar.logistics.client.gsi.context.DisplayInteractionContext;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;

import java.util.ArrayList;
import java.util.List;

public class StyledTextComponent extends AbstractStyledScaleable implements IScaleableComponent, IGlyphRenderer {

    @OnlyIn(Dist.CLIENT)
    public ScaledFontType fontType = ScaledFontType.RALEWAY;

    public List<IGlyphString> glyphStrings = new ArrayList<>();
    public StyledTextWrapper textWrapper = new StyledTextWrapper();
    public GlyphStyle parentStyling = new GlyphStyle();

    public int page;
    public int pageCount;

    public StyledTextComponent() {}

    @Override
    public void build(Vec3d alignment, Vec3d maxSizing) {
        super.build(alignment, maxSizing);
        this.textWrapper.wrap(fontType, glyphStrings, parentStyling, this.alignment.getRenderSizing());
        this.pageCount = textWrapper.cachedGlyphPages.size();
    }

    @Override
    public void render(ScaleableRenderContext context) {
        super.render(context);
        if(textWrapper.cachedGlyphPages.size() > page){
            context.matrix.push();

            context.matrix.translate(alignment.getRenderAlignment().getX(), alignment.getRenderAlignment().getY(), -0.01F);
            StyledTextRenderer.renderCachedGlyphLines(context, fontType, textWrapper.cachedGlyphPages.get(page), this);

            context.matrix.pop();
        }
    }

    @Override
    public float renderGlyph(IGlyphType glyph, StyledTextRenderer.GlyphRenderContext context) {
        if(glyph == hoveredGlyph){ //TODO REMOVE THIS IS JUST FOR TESTING
            context.parentStyling.underlined = !context.parentStyling.underlined;
            float offsetX = glyph.render(context);
            context.parentStyling.underlined = !context.parentStyling.underlined;
            return offsetX;
        }
        if(glyph == clickedGlyph){ //TODO REMOVE THIS IS JUST FOR TESTING
            context.parentStyling.strikethrough = !context.parentStyling.strikethrough;
            float offsetX = glyph.render(context);
            context.parentStyling.strikethrough = !context.parentStyling.strikethrough;
            return offsetX;
        }

        return glyph.render(context);
    }

    IGlyphType hoveredGlyph = null;
    IGlyphType clickedGlyph = null;

    @Override
    public boolean onHovered(DisplayInteractionContext context) {
        Tuple<IGlyphType, GlyphStyle> interactedGlyph = getInteractedGlyph(context);
        if(interactedGlyph != null){
            hoveredGlyph = interactedGlyph.getA();
            return true;
        }
        return false;
    }

    @Override
    public boolean onClicked(DisplayClickContext context) {
        Tuple<IGlyphType, GlyphStyle> interactedGlyph = getInteractedGlyph(context);
        if(interactedGlyph != null){
            clickedGlyph = interactedGlyph.getA();
            return true;
        }
        return false;
    }

    public Tuple<IGlyphType, GlyphStyle> getInteractedGlyph(DisplayInteractionContext context){
        if(textWrapper.cachedGlyphPages.size() > page){
            List<StyledTextWrapper.CachedGlyphLine> lines = textWrapper.cachedGlyphPages.get(page);

            for(StyledTextWrapper.CachedGlyphLine line : lines){
                float startX = line.offsetX;
                float endX = line.offsetX + line.lineWidth;
                float startY = line.offsetY;
                float endY = line.offsetY + line.lineHeight;

                if(startX < context.offsetX && endX > context.offsetX && startY < context.offsetY && endY > context.offsetY){
                    //// we have the correct string
                    GlyphStyle style = line.parentStyling;
                    float totalWidth = 0;

                    for(IGlyphType glyph : line.glyphs){
                        if(glyph instanceof StyleGlyph){
                            style = ((StyleGlyph) glyph).alterStyle(style.copy());
                            continue;
                        }

                        float renderWidth = glyph.isSpace() && line.justifySpaceSize != 0 ? line.justifySpaceSize : glyph.getRenderWidth(fontType, style) * line.lineScaling;
                        float renderHeight = glyph.isSpace() ? line.lineHeight : glyph.getRenderHeight(fontType, style) * line.lineScaling;

                        startX = totalWidth;
                        endX = totalWidth + renderWidth;
                        startY = line.offsetY;
                        endY = line.offsetY + renderHeight;
                        totalWidth+=renderWidth;
                        if(startX < context.offsetX && endX > context.offsetX && startY < context.offsetY && endY > context.offsetY){
                            return new Tuple<>(glyph, style);
                        }
                    }
                }
            }
        }
        return null;
    }
}
