package sonar.logistics.client.gsi.components.text.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.fonts.EmptyGlyph;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import sonar.logistics.client.gsi.components.text.api.CursorPoint;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.glyph.CharGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;

public class StyledTextRenderer implements IGlyphRenderer {

    public static final StyledTextRenderer INSTANCE = new StyledTextRenderer();

    ///// CACHED RENDER METHODS \\\\\

    @Override
    public void renderGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        glyphInfo.glyph.render(context, glyphInfo);
    }

    @Override
    public void renderEffects(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        addStylingEffects(context, glyphInfo, glyphInfo.style.shadow);
    }

    public void renderCurrentPage(GSIRenderContext context, ScaledFontType fontType, StyledTextPages pages) {
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
            renderer.renderEffects(context, glyph);
        }

        context.finishLine(line);
        context.renderContext.matrix.pop();
    }

    ///// CHAR RENDERING \\\\\

    public void renderCharGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo, CharGlyph charGlyph) {

        float downscale = charGlyph.downscale(context.getScaledFont(), glyphInfo.style);
        context.renderContext.matrix.scale(downscale, downscale, 1);

        if(charGlyph.isSpace()){
            return;
        }

        if (glyphInfo.style.shadow) {
            renderCharGlyph(context, glyphInfo, charGlyph,  true);
        }
        context.renderContext.matrix.translate(0, 0, GSIRenderHelper.MIN_Z_OFFSET);
        renderCharGlyph(context, glyphInfo, charGlyph, false);
        context.renderContext.matrix.translate(0, 0, -GSIRenderHelper.MIN_Z_OFFSET);
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

    }

    ///// EFFECT RENDERING \\\\\

    public void addStylingEffects(GlyphRenderContext context, GlyphRenderInfo glyphInfo, boolean shadow){
        if(shadow){
            context.updateColour(glyphInfo.style.textColour, 0.25F);
            addStylingEffects(context, glyphInfo);
            context.updateColour(glyphInfo.style.textColour, 1F);
        }
        addStylingEffects(context, glyphInfo);
    }

    public void addStylingEffects(GlyphRenderContext context, GlyphRenderInfo glyphInfo){

        context.renderContext.matrix.translate(0, 0, GSIRenderHelper.MIN_Z_OFFSET);

        if (glyphInfo.style.strikethrough) {
            float strikeOffset = (float) (glyphInfo.quad.getHeight()/9)*4;
            GSIRenderHelper.renderColouredRect(context.renderContext, false, (float)glyphInfo.quad.getX(), (float)glyphInfo.quad.getY() + strikeOffset, (float)glyphInfo.quad.getMaxX(), (float)glyphInfo.quad.getMaxY() - strikeOffset, glyphInfo.style.textColour.getRed(), glyphInfo.style.textColour.getGreen(), glyphInfo.style.textColour.getBlue(), glyphInfo.style.textColour.getAlpha());
        }

        if (glyphInfo.style.underlined) {
            float lineOffset = (float) (glyphInfo.quad.getHeight()/9)*8;
            GSIRenderHelper.renderColouredRect(context.renderContext, false, (float)glyphInfo.quad.getX(), (float)glyphInfo.quad.getY() + lineOffset, (float)glyphInfo.quad.getMaxX(), (float)glyphInfo.quad.getMaxY(), glyphInfo.style.textColour.getRed(), glyphInfo.style.textColour.getGreen(), glyphInfo.style.textColour.getBlue(), glyphInfo.style.textColour.getAlpha());
        }

        context.renderContext.matrix.translate(0, 0, -GSIRenderHelper.MIN_Z_OFFSET);
        /*
        if (context.renderContext.overlay != 0) {
            float overlayRed = (float) (context.renderContext.overlay >> 24 & 255) / 255.0F;
            float overlayGreen = (float) (context.renderContext.overlay >> 16 & 255) / 255.0F;
            float overlayBlue = (float) (context.renderContext.overlay >> 8 & 255) / 255.0F;
            float overlayAlpha = (float) (context.renderContext.overlay & 255) / 255.0F;
            context.addScaledTextureEffect(glyphInfo, downscale, 1.0F, 9.0F, width + 1.0F, 1.0F, GSIRenderHelper.MIN_Z_OFFSET*3, overlayGreen, overlayBlue, overlayAlpha, overlayRed);
        }
        */
    }

    public void addCursorToGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo, CursorPoint cursor){
        float downscale = glyphInfo.glyph.downscale(context.fontType, glyphInfo.style);
        float cursorWidth = 1.0F * downscale;
        if(cursor.isLeading()){
            GSIRenderHelper.renderColouredRect(context.renderContext, false, (float) glyphInfo.quad.getX() - cursorWidth, (float) glyphInfo.quad.getY(), (float) glyphInfo.quad.getX(), (float) glyphInfo.quad.getMaxY(), glyphInfo.style.textColour.getRed(), glyphInfo.style.textColour.getGreen(), glyphInfo.style.textColour.getBlue(), glyphInfo.style.textColour.getAlpha());
        }else {
            GSIRenderHelper.renderColouredRect(context.renderContext, false, (float) glyphInfo.quad.getMaxX(), (float) glyphInfo.quad.getY(), (float) glyphInfo.quad.getMaxX() + cursorWidth, (float) glyphInfo.quad.getMaxY(), glyphInfo.style.textColour.getRed(), glyphInfo.style.textColour.getGreen(), glyphInfo.style.textColour.getBlue(), glyphInfo.style.textColour.getAlpha());
        }
     }

    public void addHighlightToGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo){
        context.renderContext.matrix.translate(0, 0, -0.01);
        GSIRenderHelper.renderColouredRect(context.renderContext, false, glyphInfo.quad, ScreenUtils.transparent_hovered_button.rgba);
        context.renderContext.matrix.translate(0, 0, 0.01);
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
