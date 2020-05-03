package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.StyledTextRenderer;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

public class CharGlyph implements IGlyphType {

    public char aChar;

    public CharGlyph(char aChar){
        this.aChar = aChar;
    }

    public char getChar(){
        return aChar;
    }

    @Override
    public float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling) {
        return StyledTextRenderer.getGlyphWidth(fontType, parentStyling, this) * downscale(fontType, parentStyling);
    }

    @Override
    public float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling) {
        return StyledTextRenderer.getGlyphHeight(fontType, parentStyling, this) * downscale(fontType, parentStyling);
    }

    @Override
    public float render(StyledTextRenderer.GlyphRenderContext context) {
        return StyledTextRenderer.renderCharGlyph(context, this);
    }

    @Override
    public boolean isSpace(){
        return aChar == ' ';
    }

    @Override
    public String toString() {
        return "Char Glyph: " + aChar;
    }
}
