package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.StyledTextRenderer;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

public class StyleGlyph implements IGlyphType {

    GlyphStyle styling;

    public StyleGlyph(GlyphStyle styling){
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
        context.changeStyling(styling);
        return 0;
    }

    public GlyphStyle alterStyle(GlyphStyle parentStyling) {
        return styling.copy();
    }
}
