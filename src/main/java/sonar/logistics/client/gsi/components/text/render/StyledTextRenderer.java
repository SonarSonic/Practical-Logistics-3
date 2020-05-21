package sonar.logistics.client.gsi.components.text.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.fonts.EmptyGlyph;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.glyph.CharGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

public class StyledTextRenderer implements IGlyphRenderer {

    public static final StyledTextRenderer INSTANCE = new StyledTextRenderer();

    ///// CACHED RENDER METHODS \\\\\

    @Override
    public void renderGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        glyphInfo.glyph.render(context, glyphInfo);
    }

    public void renderCurrentPage(ScaleableRenderContext context, ScaledFontType fontType, StyledTextPages pages) {
        RenderSystem.enableAlphaTest();
        GlyphRenderContext glyphContext = new GlyphRenderContext(context, fontType);
        context.startRenderBuffer(false);
        for(StyledTextLine line : pages.getCurrentPage()) {
            renderCachedGlyphLine(glyphContext, line, pages.specialGlyphRenderer == null ? this : pages.specialGlyphRenderer);
        }
        context.finishRenderBuffer(false);
    }

    public void renderCachedGlyphLine(GlyphRenderContext context, StyledTextLine line, IGlyphRenderer renderer) {
        context.renderContext.matrix.push();
        context.renderContext.matrix.scale(line.lineScaling, line.lineScaling, 1);

        context.startLine(line);

        for(GlyphRenderInfo glyph : line.glyphInfo){
            context.renderContext.matrix.push();
            context.renderContext.matrix.translate(glyph.quad.x, glyph.quad.y, 0);
            renderer.renderGlyph(context, glyph);
            context.renderContext.matrix.pop();
        }

        context.finishLine(line);
        context.renderContext.matrix.pop();
    }

    ///// CHAR RENDERING \\\\\

    public void renderCharGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo, CharGlyph charGlyph) {

        float downscale = charGlyph.downscale(context.getScaledFont(), glyphInfo.style);
        context.renderContext.matrix.scale(downscale, downscale, downscale);

        if(charGlyph.isSpace()){
            addScaledTextureEffects(context, glyphInfo, downscale, (float)glyphInfo.quad.width * charGlyph.upscale(context.getScaledFont(), glyphInfo.style), 1.0F, glyphInfo.style.shadow);
            return;
        }

        if (glyphInfo.style.shadow) {
            renderCharGlyph(context, glyphInfo, charGlyph,  true);
        }
        context.renderContext.matrix.translate(0, 0, ScaleableRenderHelper.MIN_Z_OFFSET);
        renderCharGlyph(context, glyphInfo, charGlyph, false);
        context.renderContext.matrix.translate(0, 0, -ScaleableRenderHelper.MIN_Z_OFFSET);
    }

    private void renderCharGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo, CharGlyph charGlyph, boolean renderingShadow) {
        context.updateColour(glyphInfo.style.textColour, renderingShadow ? 0.25F : 1);

        IGlyph glyph = context.getFont().findGlyph(charGlyph.getChar());
        TexturedGlyph texturedGlyph = glyphInfo.style.obfuscated && !charGlyph.isSpace() ? context.getFont().obfuscate(glyph) : context.getFont().getGlyph(charGlyph.getChar());
        if (!(texturedGlyph instanceof EmptyGlyph)) {
            float boldOffset = glyphInfo.style.bold ? glyph.getBoldOffset() : 0.0F;
            float shadowOffset = renderingShadow ? glyph.getShadowOffset() : 0.0F;
            IVertexBuilder builder = context.renderContext.getRenderBuffer(false).getBuffer(context.getScaledFont().getRenderType(texturedGlyph));
            texturedGlyph.render(glyphInfo.style.italic, shadowOffset, shadowOffset, context.renderContext.getMatrix4f(), builder, context.red, context.green, context.blue, context.alpha, context.renderContext.light);
            if (glyphInfo.style.bold) {
                texturedGlyph.render(glyphInfo.style.italic, shadowOffset + boldOffset, shadowOffset, context.renderContext.getMatrix4f(), builder, context.red, context.green, context.blue, context.alpha, context.renderContext.light);
            }
        }

        addScaledTextureEffects(context, glyphInfo, charGlyph.downscale(context.getScaledFont(), glyphInfo.style), glyph.getAdvance(glyphInfo.style.bold), renderingShadow ? 1.0F : 0.0F);

    }

    ///// EFFECT RENDERING \\\\\

    public void addScaledTextureEffects(GlyphRenderContext context, GlyphRenderInfo glyphInfo, float downscale, float glyphWidth, float shadowOffset, boolean shadow){
        if(shadow){
            context.updateColour(glyphInfo.style.textColour, 0.25F);
            addScaledTextureEffects(context, glyphInfo, downscale, glyphWidth, shadowOffset);
            context.updateColour(glyphInfo.style.textColour, 1F);
        }
        addScaledTextureEffects(context, glyphInfo, downscale, glyphWidth, 0);
    }

    public void addScaledTextureEffects(GlyphRenderContext context, GlyphRenderInfo glyphInfo, float downscale, float glyphWidth, float shadowOffset){

        if (glyphInfo.style.strikethrough) {
            context.addScaledTextureEffect(glyphInfo, downscale, shadowOffset, shadowOffset + 4.5F*(context.getScaledFont().getElementScaling()/9F), shadowOffset + glyphWidth, shadowOffset + (4.5F - 1.0F)*(context.getScaledFont().getElementScaling()/9F), -0.01F, context.red, context.green, context.blue, context.alpha);
        }

        if (glyphInfo.style.underlined) {
            context.addScaledTextureEffect(glyphInfo, downscale, shadowOffset, shadowOffset + 9.0F*(context.getScaledFont().getElementScaling()/9F), shadowOffset + glyphWidth, shadowOffset + (9.0F - 1.0F)*(context.getScaledFont().getElementScaling()/9F), -0.01F, context.red, context.green, context.blue, context.alpha);
        }

        if (context.renderContext.overlay != 0) {
            float overlayRed = (float) (context.renderContext.overlay >> 24 & 255) / 255.0F;
            float overlayGreen = (float) (context.renderContext.overlay >> 16 & 255) / 255.0F;
            float overlayBlue = (float) (context.renderContext.overlay >> 8 & 255) / 255.0F;
            float overlayAlpha = (float) (context.renderContext.overlay & 255) / 255.0F;
            context.addScaledTextureEffect(glyphInfo, downscale, 1.0F, 9.0F, glyphWidth + 1.0F, 1.0F, 0.01F, overlayGreen, overlayBlue, overlayAlpha, overlayRed);
        }
    }

    public void addCursorToGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo, float downscale, float glyphWidth){
        context.addScaledTextureEffect(glyphInfo, downscale, glyphWidth - 1.0F, 9.0F * (context.getScaledFont().getElementScaling() / 9F), glyphWidth, 0, -0.01F, 1.0F, 1.0F, 1.0F, 1.0F);
     }

    public void addHighlightToGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo, float downscale, float glyphWidth){
        context.addScaledTextureEffect(glyphInfo, downscale, 0, 9.0F * (context.getScaledFont().getElementScaling() / 9F), glyphWidth, 0, 0.01F, 1.0F, 1.0F, 1.0F, 0.5F);
    }

    ///// SIZING \\\\\\

    public float getCharWidth(ScaledFontType fontType, GlyphStyle style, char aChar){
        if(aChar == ' '){ //// SPACE
            return fontType.getMinimumSpaceSize();
        }
        if(aChar == 167){ //// TEXT FORMATTING
            return 0.0F;
        }
        IGlyph glyph = fontType.getFont().findGlyph(aChar);
        return glyph.getAdvance(style.bold) + (style.shadow ? 1.0F : 0);
    }

    public float getCharHeight(ScaledFontType fontType, GlyphStyle style, char aChar){
        return fontType.getFontScaling();
    }

}
