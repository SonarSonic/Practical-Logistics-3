package sonar.logistics.client.gsi.components.text;

import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.glyph.*;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

import java.util.ArrayList;
import java.util.List;

public class StyledTextString {

    public List<IGlyphType> glyphs;

    public StyledTextString(){
        this.glyphs = new ArrayList<>();
    }

    /////

    public void addChar(char aChar){
        addChar(aChar, glyphs.size());
    }

    public void addChar(char aChar, int index){
        glyphs.add(index, new CharGlyph(aChar));
    }

    /////

    public void addString(String text){
        addString(text, glyphs.size());
    }

    public void addString(String text, int index){
        for(char c : text.toCharArray()){
            glyphs.add(index, new CharGlyph(c));
            index++;
        }
    }

    /////

    public void addElement(IRenderableElement element){
        addElement(element, glyphs.size());
    }

    public void addElement(IRenderableElement element, int index){
        glyphs.add(index, new ElementGlyph(element));
    }

    /////

    public void addAttribute(AttributeGlyph attribute){
        addAttribute(attribute, glyphs.size());
    }

    public void addAttribute(AttributeGlyph attribute, int index){
        glyphs.add(index, attribute);
    }

    /////

    public void addStyling(GlyphStyle style){
        addStyling(style, glyphs.size());
    }

    public void addStyling(GlyphStyle style, int index){
        glyphs.add(index, new StylingGlyph(style.copy()));
    }

    /////

    public void addLineBreak(LineStyle styling){
        addLineBreak(styling, glyphs.size());
    }

    public void addLineBreak(LineStyle styling, int index){
        glyphs.add(index, new LineBreakGlyph(false, styling));
    }

    /////

    public void addPageBreak(LineStyle styling){
        addPageBreak(styling, glyphs.size());
    }

    public void addPageBreak(LineStyle styling, int index){
        glyphs.add(index, new LineBreakGlyph(false, styling));
    }

}
