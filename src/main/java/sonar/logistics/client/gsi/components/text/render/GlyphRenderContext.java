package sonar.logistics.client.gsi.components.text.render;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.vectors.Quad2D;

import java.util.ArrayList;
import java.util.List;

public class GlyphRenderContext {

    public ScaleableRenderContext renderContext;
    public ScaledFontType fontType;
    public Quad2D bounds;

    public StyledTextLine line;
    public float red, green, blue, alpha;

    public List<TexturedGlyph.Effect> effects;

    public GlyphRenderContext(ScaleableRenderContext renderContext, ScaledFontType fontType, Quad2D bounds){
        this.renderContext = renderContext;
        this.fontType = fontType;
        this.bounds = bounds;
    }

    public void startLine(StyledTextLine line){
        this.line = line;
        this.effects = new ArrayList<>();
    }

    public void finishLine(StyledTextLine line){
        flushEffects();
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
    public void addScaledTextureEffect(GlyphRenderInfo glyphInfo, float glyphScaling, float x0, float y0, float x1, float y1, float depth, float r, float g, float b, float a) {
        effects.add(new TexturedGlyph.Effect((float)glyphInfo.quad.x + x0*glyphScaling, (float)glyphInfo.quad.y  + y0*glyphScaling, (float)glyphInfo.quad.x + x1*glyphScaling, (float)glyphInfo.quad.y + y1*glyphScaling, depth, r, g, b, a));
    }
}