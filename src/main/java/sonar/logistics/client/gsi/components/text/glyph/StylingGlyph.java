package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

@Deprecated
public class StylingGlyph implements IGlyphType {

    public GlyphStyle style;

    public StylingGlyph(GlyphStyle style) {
        this.style = style;
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
    public void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {}

    public void alterStyle(GlyphStyle parentStyling){
        //TODO
    }
}