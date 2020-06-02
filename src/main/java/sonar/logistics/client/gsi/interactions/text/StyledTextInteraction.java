package sonar.logistics.client.gsi.interactions.text;

import sonar.logistics.client.gsi.api.ITextComponent;
import sonar.logistics.client.gsi.components.text.render.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.render.*;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gui.EnumLineBreakGlyph;
import sonar.logistics.client.gui.EnumLineStyling;
import sonar.logistics.client.gui.GSIDesignSettings;

public class StyledTextInteraction<C extends ITextComponent> extends StandardTextInteraction<C> implements IGlyphRenderer {

    public StyledTextInteraction(C component) {
        super(component);
    }

    @Override
    public GlyphStyle getGlyphStyle() {
        return GSIDesignSettings.glyphStyle;
    }

    @Override
    public void onSettingChanged(Object setting, Object settingObj) {
        if(setting instanceof GlyphStyleAttributes){
            applyAttribute((GlyphStyleAttributes)setting, settingObj);
        }
        if(setting instanceof EnumLineStyling){
            applyLineStyle((LineStyle) settingObj);
        }
        if(setting instanceof EnumLineBreakGlyph){
            applyLineBreakGlyph((EnumLineBreakGlyph) setting);
        }
    }

    public void applyLineBreakGlyph(EnumLineBreakGlyph settings){
        GlyphRenderInfo glyphInfo = pages.text.glyphs.isEmpty() ? null : pages.getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
        if(glyphInfo != null){
            LineBreakGlyph glyph = (LineBreakGlyph) glyphInfo.glyph;
            pages.text.glyphs.set(glyphInfo.index, GSIDesignSettings.getLineBreakGlyph(glyph.pageBreak, glyph.lineStyle));
        }else{
            //if we didn't find a line break glyph anywhere then the text is all one line, so we can add one to the start
            pages.text.addGlyph(GSIDesignSettings.getLineBreakGlyph(false, new LineStyle()), 0);
            moveCursorRight(cursor);
        }
        component.onStylingChanged();
    }

    public void applyLineStyle(LineStyle style){
        //TODO IF THERE IS A SELECTION, WE NEED TO DO THIS TO ALL SELECTED LINES!
        GlyphRenderInfo glyphInfo = pages.getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
        if(glyphInfo != null){
            LineBreakGlyph glyph = (LineBreakGlyph) glyphInfo.glyph;
            glyph.lineStyle = style.copy();
        }else{
            //if we didn't find a line break glyph anywhere then the text is all one line, so we can add one to the start
            pages.text.addGlyph(GSIDesignSettings.getLineBreakGlyph(false, style.copy()), 0);
            moveCursorRight(cursor);
        }
        component.onStylingChanged();
    }

    public void clearFormatting(){
        applyLineStyle(new LineStyle());
        pages.text.clearAttributes(getSelectionStartIndex(), getSelectionEndIndex());
        onCursorMoved();
        component.onStylingChanged();
    }

    public void applyAttribute(GlyphStyleAttributes attribute, Object attributeObj){
        pages.text.applyAttribute(attribute, attributeObj, getSelectionStartIndex(), getSelectionEndIndex());
        component.onStylingChanged();
    }

    //updates the current GlyphStyle and LineStyle from the cursor's new position
    public void onCursorMoved(){
        super.onCursorMoved();
        if(selectionEnd == null && !pages.styledGlyphs.isEmpty()){
            ///we use the char index, as this will always be on the line the cursor is rendered on, the insertion index could refer to prev/next line
            StyledTextLine line = pages.getStyledLine(getSafeIndex(cursor.getCharIndex()));
            GlyphRenderInfo info = pages.getGlyphInfo(getSafeIndex(cursor.getCharIndex()));
            if(line != null && info != null) {
                GSIDesignSettings.glyphStyle = info.glyph.getStyle();
                GSIDesignSettings.lineStyle = line.lineStyle;
                GSIDesignSettings.screen.onCursorStyleChanged();
            }
            int cursorPage = pages.getPageNumber(cursor.getCharIndex());
            if(cursorPage != -1 && cursorPage != pages.page){
                pages.page = cursorPage;
            }
        }
    }


}