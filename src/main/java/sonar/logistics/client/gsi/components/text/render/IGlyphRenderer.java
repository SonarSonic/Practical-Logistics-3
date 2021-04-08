package sonar.logistics.client.gsi.components.text.render;

/**
 * Client side only: defines the way Glyph's should be rendered, generally this is just the defaults defined in {@link StyledTextRenderer}
 * However, it is overrriden when the text is being edited {@link sonar.logistics.client.gsi.interactions.text.StandardTextInteraction}
 */
public interface IGlyphRenderer {

    void renderGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo);

    void renderEffects(GlyphRenderContext context, GlyphRenderInfo glyphInfo);
}
