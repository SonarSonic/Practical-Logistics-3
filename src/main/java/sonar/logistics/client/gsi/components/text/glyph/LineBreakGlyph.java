package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.StyledTextRenderer;
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
        return 0;
    }

    @Override
    public float render(StyledTextRenderer.GlyphRenderContext context) {
        return 0;
    }
}
