package sonar.logistics.client.gsi.components.text.string;

import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.components.text.api.IGlyphString;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.glyph.CharGlyph;
import sonar.logistics.client.gsi.components.text.glyph.ElementGlyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.glyph.StyleGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

import java.util.ArrayList;
import java.util.List;

public class GlyphString implements IGlyphString {

    public List<IGlyphType> glyphs;

    public GlyphString(){
        this.glyphs = new ArrayList<>();
    }

    public void addString(String text){
        for(char c : text.toCharArray()){
            glyphs.add(new CharGlyph(c));
        }
    }

    public void addElement(IRenderableElement element){
        glyphs.add(new ElementGlyph(element));
    }

    public void addStyling(GlyphStyle style){
        glyphs.add(new StyleGlyph(style.copy()));
    }

    public void addLineBreak(LineStyle styling){
        glyphs.add(new LineBreakGlyph(false, styling));
    }

    public void addPageBreak(LineStyle styling){
        glyphs.add(new LineBreakGlyph(false, styling));
    }

    @Override
    public List<IGlyphType> getGlyphs() {
        return glyphs;
    }

}
