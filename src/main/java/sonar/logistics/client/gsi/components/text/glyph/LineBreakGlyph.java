package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

public class LineBreakGlyph implements IGlyphType {

    public boolean page;
    public LineStyle styling;

    public LineBreakGlyph(boolean page, LineStyle styling){
        this.page = page;
        this.styling = styling;
    }

    @Override
    public float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling) {
        return 0;
    }

    @Override
    public float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling) {
        ///the line break glyph needs default height to enable empty lines.
        return fontType.getElementScaling()  * downscale(fontType, parentStyling);
    }

    @Override
    public void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo){ }

    @Override
    public boolean isVisible(){
        return false;
    }
}
