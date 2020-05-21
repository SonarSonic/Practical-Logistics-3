package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

public class BulletPointGlyph extends LineBreakGlyph {

    public static char ROUND_BULLET_POINT = '\u2219';
    public static char DIAMOND_BULLET_POINT = '\u2666';
    public static char TICK_BULLET_POINT = '\u2713';
    public static char CROSS_BULLET_POINT = '\u2715';

    public CharGlyph bulletPointChar;

    public BulletPointGlyph(boolean pageBreak, LineStyle styling, char bulletPointChar) {
        super(pageBreak, styling);
        this.bulletPointChar = new CharGlyph(bulletPointChar);
    }

    @Override
    public float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling) {
        return fontType.getMinimumSpaceSize() * downscale(fontType, parentStyling) + bulletPointChar.getRenderWidth(fontType, parentStyling);
    }

    @Override
    public float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling) {
        return bulletPointChar.getRenderHeight(fontType, parentStyling);
    }

    @Override
    public void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        super.render(context, glyphInfo);
        context.renderContext.matrix.translate(context.fontType.getMinimumSpaceSize() * downscale(context.fontType, glyphInfo.style), 0, 0);
        bulletPointChar.render(context, glyphInfo);
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
