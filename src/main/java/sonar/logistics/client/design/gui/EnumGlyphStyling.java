package sonar.logistics.client.design.gui;

import sonar.logistics.client.gsi.components.text.glyph.AttributeGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

import java.util.function.Function;

public enum EnumGlyphStyling {
    FONT_HEIGHT(style -> new AttributeGlyph.FontHeight(style.fontHeight)),
    TEXT_COLOUR(style -> new AttributeGlyph.TextColour(style.textColour)),
    BACKGROUND_COLOUR(style -> new AttributeGlyph.BackgroundColour(style.backgroundColour)),
    ACTION_ID(style -> new AttributeGlyph.ActionID(style.actionId)),
    BOLD(style -> new AttributeGlyph.Bold(style.bold)),
    UNDERLINE(style -> new AttributeGlyph.Underline(style.underlined)),
    ITALIC(style -> new AttributeGlyph.Italic(style.italic)),
    STRIKETHROUGH(style -> new AttributeGlyph.Strikethrough(style.strikethrough)),
    OBFUSCATED(style -> new AttributeGlyph.Obfuscated(style.obfuscated)),
    SHADOW(style -> new AttributeGlyph.Shadow(style.shadow));

    Function<GlyphStyle, AttributeGlyph> func;

    EnumGlyphStyling(Function<GlyphStyle, AttributeGlyph> func){
        this.func = func;
    }

    public AttributeGlyph getAttributeGlyph(GlyphStyle style){
        return func.apply(style);
    }
}
