package sonar.logistics.client.design.gui;

import sonar.logistics.client.gsi.components.text.glyph.BulletPointGlyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

import java.util.function.BiFunction;

public enum EnumLineBreakGlyph {

    DEFAULT_BREAK(LineBreakGlyph::new),
    ROUND_BULLET_POINT((pageBreak, lineStyle) -> new BulletPointGlyph(pageBreak, lineStyle, '\u2219')),
    DIAMOND_BULLET_POINT((pageBreak, lineStyle) -> new BulletPointGlyph(pageBreak, lineStyle, '\u2666')),
    TICK_BULLET_POINT((pageBreak, lineStyle) -> new BulletPointGlyph(pageBreak, lineStyle, '\u2713')),
    CROSS_BULLET_POINT((pageBreak, lineStyle) -> new BulletPointGlyph(pageBreak, lineStyle, '\u2715'));

    private BiFunction<Boolean, LineStyle, LineBreakGlyph> create;

    EnumLineBreakGlyph(BiFunction<Boolean, LineStyle, LineBreakGlyph> create){
        this.create = create;
    }

    public LineBreakGlyph create(Boolean pageBreak, LineStyle style){
        return create.apply(pageBreak, style);
    }

}
