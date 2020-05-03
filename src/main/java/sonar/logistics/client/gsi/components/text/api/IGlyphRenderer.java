package sonar.logistics.client.gsi.components.text.api;

import sonar.logistics.client.gsi.components.text.StyledTextRenderer;

public interface IGlyphRenderer {

    float renderGlyph(IGlyphType glyph, StyledTextRenderer.GlyphRenderContext context);

}
