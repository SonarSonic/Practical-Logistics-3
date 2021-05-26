package sonar.logistics.client.gsi.components.text.render;

import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.util.vectors.Quad2F;

public class GlyphRenderInfo {

    public int index;
    public Glyph glyph;
    public GlyphStyle style;
    public Quad2F quad;

    public GlyphRenderInfo(int index, Glyph glyph, GlyphStyle style, float renderWidth, float renderHeight){
        this.index = index;
        this.glyph = glyph;
        this.quad = new Quad2F().setSizing(renderWidth, renderHeight);
        this.style = style;
    }

}
