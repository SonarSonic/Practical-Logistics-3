package sonar.logistics.client.gsi.components.text;

import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.components.text.glyph.*;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleHolder;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyledTextString {

    public List<Glyph> glyphs;

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

    public void addGlyph(Glyph glyph){
        addGlyph(glyph, glyphs.size());
    }

    public void addGlyph(Glyph glyph, int index){
        glyphs.add(index, glyph);
    }

    /////

    public boolean deleteGlyph(int index){
        glyphs.remove(index);
        return true;
    }

    public boolean deleteGlyphs(int startIndex, int endIndex){
        List<Glyph> toDelete = new ArrayList<>();
        for(int i = startIndex ; i < endIndex; i ++) {
            Glyph glyph = glyphs.get(i);
            toDelete.add(glyph);
        }
        glyphs.removeAll(toDelete);
        return !toDelete.isEmpty();
    }

    public void clearAttributes(int startIndex, int endIndex){
        for(int i = startIndex ; i < endIndex; i ++) {
            Glyph glyph = glyphs.get(i);
            glyph.styleHolder = null;
        }
    }

    public void resetAttribute(GlyphStyleAttributes attribute, int startIndex, int endIndex){
        applyAttribute(attribute, attribute.getDefault(), startIndex, endIndex);
    }

    public void applyAttribute(GlyphStyleAttributes attribute, Object attributeObj, int startIndex, int endIndex){
        for(int i = startIndex ; i <= Math.min(endIndex, glyphs.size() - 1); i ++){
            Glyph glyph = glyphs.get(i);

            if(glyph.styleHolder == null){
                glyph.styleHolder = new GlyphStyleHolder();
            }

            glyph.styleHolder.setAttribute(attribute, attributeObj);

            if(glyph.styleHolder.isDefault()){
                glyph.styleHolder = null;
            }
        }
    }

    ////

    public String getRawString(){
        StringBuilder builder = new StringBuilder();
        for(Glyph g : glyphs){
            if(g instanceof CharGlyph){
                builder.append(((CharGlyph) g).aChar);
            }
        }
        return builder.toString();
    }

    public int getIntegerFromText(){
        if(glyphs.isEmpty()){
            return 0;
        }
        String rawString = getRawString();
        return Integer.parseInt(rawString);
    }

}
