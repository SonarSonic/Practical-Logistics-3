package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

public class LineBreakGlyph extends Glyph {

    public boolean pageBreak;
    public LineStyle lineStyle;

    public LineBreakGlyph(boolean pageBreak, LineStyle lineStyle){
        this.pageBreak = pageBreak;
        this.lineStyle = lineStyle;
    }

    @Override
    public float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling) {
        return 0;
    }

    @Override
    public float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling) {
        ///the line break glyph needs default height to enable empty lines.
        //TODO FIX ME - IF THE LINE SCALING IS LESS THAT DEFAULT THE LINE BREAK GLYPH WILL MESS UP LINE POSITION, MAYBE WE NEED TO PASS OR SET STYLES ON LINE GLYPHS.
        return fontType.getElementScaling()  * downscale(fontType, parentStyling);
    }

    @Override
    public void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo){ }

    @Override
    public boolean isVisible(){
        return false;
    }
}
