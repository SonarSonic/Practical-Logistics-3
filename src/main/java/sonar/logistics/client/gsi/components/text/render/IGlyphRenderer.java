package sonar.logistics.client.gsi.components.text.render;

import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;

public interface IGlyphRenderer {

    void renderGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo);

    void renderEffects(GlyphRenderContext context, GlyphRenderInfo glyphInfo);
}
