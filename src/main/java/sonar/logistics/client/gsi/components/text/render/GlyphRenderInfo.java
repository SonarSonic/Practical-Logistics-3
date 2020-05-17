package sonar.logistics.client.gsi.components.text.render;

import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.vectors.Quad2D;

public class GlyphRenderInfo {

    public int index;
    public Glyph glyph;
    public GlyphStyle style;
    public Quad2D quad;

    public GlyphRenderInfo(int index, Glyph glyph, GlyphStyle style, double renderWidth, double renderHeight){
        this.index = index;
        this.glyph = glyph;
        this.quad = new Quad2D().setSizing(renderWidth, renderHeight);
        this.style = style;
    }

}
