package sonar.logistics.client.gsi.components.text;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.fonts.EmptyGlyph;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.glyph.CharGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.properties.ColourProperty;

import java.util.ArrayList;
import java.util.List;

public class StyledTextRenderer {

    public IGlyphRenderer DEFAULT_RENDERER = IGlyphType::render;

    ///// CACHED RENDER METHODS \\\\\

    public static void renderCachedGlyphLines(ScaleableRenderContext context, ScaledFontType fontType, List<StyledTextWrapper.CachedGlyphLine> lines, IGlyphRenderer renderer) {
        GlyphRenderContext glyphContext = new GlyphRenderContext(context, fontType);
        for(StyledTextWrapper.CachedGlyphLine line : lines) {
            renderCachedGlyphLine(glyphContext, line, renderer);
        }
    }

    public static void renderCachedGlyphLine(GlyphRenderContext context, StyledTextWrapper.CachedGlyphLine line, IGlyphRenderer renderer) {
        context.renderContext.matrix.push();
        context.renderContext.matrix.scale(line.lineScaling, line.lineScaling, 0);

        context.startLine(line);
        for(IGlyphType glyph : line.glyphs){
            context.renderContext.matrix.push();
            context.renderContext.matrix.translate(context.offsetX, context.offsetY, 0);
            context.offsetX += renderer.renderGlyph(glyph, context);
            context.renderContext.matrix.pop();
        }

        context.finishLine(line);
        context.renderContext.matrix.pop();
    }

    ///// CHAR RENDERING \\\\\

    public static float renderCharGlyph(GlyphRenderContext context, CharGlyph charGlyph) {

        float downscale = charGlyph.downscale(context.getScaledFont(), context.parentStyling);
        context.renderContext.matrix.scale(downscale, downscale, downscale);

        if(context.justifySpaceSizing != 0 && charGlyph.isSpace()){
            addScaledTextureEffects(context, downscale, context.justifySpaceSizing * charGlyph.upscale(context.getScaledFont(), context.parentStyling), 1.0F, context.parentStyling.shadow);
            return context.justifySpaceSizing;
        }

        if (context.parentStyling.shadow) {
            renderCharGlyph(context, charGlyph,  true);
        }

        context.renderContext.matrix.translate(0, 0, -0.1F);
        renderCharGlyph(context, charGlyph, false);
        context.renderContext.matrix.translate(0, 0, 0.1F);
        return charGlyph.getRenderWidth(context.getScaledFont(), context.parentStyling);
    }

    private static void renderCharGlyph(GlyphRenderContext context, CharGlyph charGlyph, boolean renderingShadow) {
        if(renderingShadow){
            context.updateColour(context.parentStyling.textColour, 0.25F);
        }

        IGlyph glyph = context.getFont().findGlyph(charGlyph.getChar());
        TexturedGlyph texturedGlyph = context.parentStyling.obfuscated && !charGlyph.isSpace() ? context.getFont().obfuscate(glyph) : context.getFont().getGlyph(charGlyph.getChar());
        if (!(texturedGlyph instanceof EmptyGlyph)) {
            float boldOffset = context.parentStyling.bold ? glyph.getBoldOffset() : 0.0F;
            float shadowOffset = renderingShadow ? glyph.getShadowOffset() : 0.0F;
            IVertexBuilder builder = context.renderContext.getTessellatorBuffer().getBuffer(context.getScaledFont().getRenderType(texturedGlyph));
            texturedGlyph.render(context.parentStyling.italic, shadowOffset, shadowOffset, context.renderContext.getMatrix4f(), builder, context.red, context.green, context.blue, context.alpha, context.renderContext.light);
            if (context.parentStyling.bold) {
                texturedGlyph.render(context.parentStyling.italic, shadowOffset + boldOffset, shadowOffset, context.renderContext.getMatrix4f(), builder, context.red, context.green, context.blue, context.alpha, context.renderContext.light);
            }
        }

        addScaledTextureEffects(context, charGlyph.downscale(context.getScaledFont(), context.parentStyling), glyph.getAdvance(context.parentStyling.bold), renderingShadow ? 1.0F : 0.0F);

        if(renderingShadow){
            context.updateColour(context.parentStyling.textColour, 1);
        }
    }

    ///// EFFECT RENDERING \\\\\

    public static void addScaledTextureEffects(GlyphRenderContext context, float downscale, float glyphWidth, float shadowOffset, boolean shadow){
        if(shadow){
            context.updateColour(context.parentStyling.textColour, 0.25F);
            addScaledTextureEffects(context, downscale, glyphWidth, shadowOffset);
            context.updateColour(context.parentStyling.textColour, 1F);
        }
        addScaledTextureEffects(context, downscale, glyphWidth, 0);
    }

    public static void addScaledTextureEffects(GlyphRenderContext context, float downscale, float glyphWidth, float shadowOffset){

        if (context.parentStyling.strikethrough) {
            context.addScaledTextureEffect(downscale, shadowOffset, shadowOffset + 4.5F*(context.getScaledFont().getElementScaling()/9F), shadowOffset + glyphWidth, shadowOffset + (4.5F - 1.0F)*(context.getScaledFont().getElementScaling()/9F), -0.01F, context.red, context.green, context.blue, context.alpha);
        }

        if (context.parentStyling.underlined) {
            context.addScaledTextureEffect(downscale, shadowOffset, shadowOffset + 9.0F*(context.getScaledFont().getElementScaling()/9F), shadowOffset + glyphWidth, shadowOffset + (9.0F - 1.0F)*(context.getScaledFont().getElementScaling()/9F), -0.01F, context.red, context.green, context.blue, context.alpha);
        }

        if (context.renderContext.overlay != 0) {
            float overlayRed = (float) (context.renderContext.overlay >> 24 & 255) / 255.0F;
            float overlayGreen = (float) (context.renderContext.overlay >> 16 & 255) / 255.0F;
            float overlayBlue = (float) (context.renderContext.overlay >> 8 & 255) / 255.0F;
            float overlayAlpha = (float) (context.renderContext.overlay & 255) / 255.0F;
            context.addScaledTextureEffect(downscale, 1.0F, 9.0F, glyphWidth + 1.0F, 1.0F, 0.01F, overlayGreen, overlayBlue, overlayAlpha, overlayRed);
        }
    }

    public static void addCursorToGlyph(GlyphRenderContext context, float downscale, float glyphWidth){
        context.addScaledTextureEffect(downscale, glyphWidth - 1.0F, 9.0F * (context.getScaledFont().getElementScaling() / 9F), glyphWidth, 0, -0.01F, 1.0F, 1.0F, 1.0F, 1.0F);
     }

    public static void addHighlightToGlyph(GlyphRenderContext context, float downscale, float glyphWidth){
        context.addScaledTextureEffect(downscale, 0, 9.0F * (context.getScaledFont().getElementScaling() / 9F), glyphWidth, 0, 0.01F, 1.0F, 1.0F, 1.0F, 0.5F);
    }

    ///// SIZING \\\\\\

    public static float getGlyphWidth(ScaledFontType fontType, GlyphStyle style, IGlyphType renderableGlyph){
        if (renderableGlyph instanceof CharGlyph) {
            CharGlyph charGlyph = (CharGlyph) renderableGlyph;
            if(charGlyph.isSpace()){ //// SPACE
                return fontType.getMinimumSpaceSize();
            }
            if(charGlyph.aChar == 167){ //// TEXT FORMATTING
                return 0.0F;
            }
            IGlyph glyph = fontType.getFont().findGlyph(charGlyph.getChar());
            return glyph.getAdvance(style.bold) + (style.shadow ? 1.0F : 0);
        }
        return 0;
    }

    public static float getGlyphHeight(ScaledFontType fontType, GlyphStyle style, IGlyphType renderableGlyph){
        return fontType.getFontScaling();
    }

    ///// RENDER CONTEXT \\\\\

    public static class GlyphRenderContext {

        public ScaleableRenderContext renderContext;
        public ScaledFontType fontType;
        public GlyphStyle parentStyling;
        public float justifySpaceSizing;
        public float offsetX, offsetY;
        public float red, green, blue, alpha;

        public List<TexturedGlyph.Effect> effects;

        public GlyphRenderContext(ScaleableRenderContext renderContext, ScaledFontType fontType){
            this.renderContext = renderContext;
            this.fontType = fontType;
        }

        public void startLine(StyledTextWrapper.CachedGlyphLine line){
            this.parentStyling = line.parentStyling;
            this.justifySpaceSizing = line.justifySpaceSize;
            this.effects = new ArrayList<>();
            this.offsetX = line.offsetX;
            this.offsetY = line.offsetY;
            changeStyling(parentStyling);
        }

        public void finishLine(StyledTextWrapper.CachedGlyphLine line){
            flushEffects();
        }

        public void changeStyling(GlyphStyle styling){
            flushEffects();
            parentStyling = styling;
            updateColour(parentStyling.textColour, 1);
        }

        public void flushEffects(){
            if (!effects.isEmpty()) {
                TexturedGlyph whiteGlyph = ScaledFontType.DEFAULT_MINECRAFT.getFont().getWhiteGlyph();
                IVertexBuilder builder = renderContext.getTessellatorBuffer().getBuffer(ScaledFontType.DEFAULT_MINECRAFT.getRenderType(whiteGlyph));
                for (TexturedGlyph.Effect effect : effects) {
                    whiteGlyph.renderEffect(effect, renderContext.getMatrix4f(), builder, renderContext.light);
                }
                effects.clear();
            }
        }

        public ScaledFontType getScaledFont(){
            return fontType;
        }

        public Font getFont(){
            return fontType.getFont();
        }

        ///// RENDERING HELPERS \\\\\\

        public void updateColour(ColourProperty colour, float brightness){
            red = colour.getRed() / 255.0F * brightness;
            green = colour.getGreen() / 255.0F * brightness;
            blue = colour.getBlue() / 255.0F  * brightness;
            alpha = colour.getAlpha() / 255.0F;
        }

        /**effects are batched so their position isn't relative to the chars scaling*/
        public void addScaledTextureEffect(float glyphScaling, float x0, float y0, float x1, float y1, float depth, float r, float g, float b, float a) {
            effects.add(new TexturedGlyph.Effect(offsetX + x0*glyphScaling, offsetY + y0*glyphScaling, offsetX + x1*glyphScaling, offsetY + y1*glyphScaling, depth, r, g, b, a));
        }
    }
}
