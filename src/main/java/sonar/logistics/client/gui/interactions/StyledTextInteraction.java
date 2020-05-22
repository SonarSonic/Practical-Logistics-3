package sonar.logistics.client.gui.interactions;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import sonar.logistics.client.gsi.api.ITextComponent;
import sonar.logistics.client.gsi.components.text.api.CursorPoint;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.glyph.CharGlyph;
import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.render.*;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleHolder;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.gui.EnumLineBreakGlyph;
import sonar.logistics.client.gui.EnumLineStyling;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gui.interactions.hotkeys.HotKeyFunctions;
import sonar.logistics.client.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.vectors.Vector2D;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class StyledTextInteraction extends DefaultTextInteraction implements IGlyphRenderer {

    public StyledTextInteraction(GSIViewportWidget viewport, ITextComponent textComponent) {
        super(viewport, textComponent);
        this.pages.specialGlyphRenderer = this;
    }

    @Override
    public GlyphStyle getGlyphStyle() {
        return GSIDesignSettings.glyphStyle;
    }

    @Override
    public void onGlyphStyleChanged(GlyphStyleAttributes attribute, Object attributeObj) {
        super.onGlyphStyleChanged(attribute, attributeObj);
        applyAttribute(attribute, attributeObj);
    }

    @Override
    public void onLineStyleChanged(EnumLineStyling styling) {
        super.onLineStyleChanged(styling);
        applyLineStyle(GSIDesignSettings.lineStyle);
    }

    @Override
    public void onLineBreakGlyphChanged(EnumLineBreakGlyph settings) {
        super.onLineBreakGlyphChanged(settings);
        applyLineBreakGlyph(settings);
    }

    public void applyLineBreakGlyph(EnumLineBreakGlyph settings){
        GlyphRenderInfo glyphInfo = pages.text.glyphs.isEmpty() ? null : pages.getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
        if(glyphInfo != null){
            LineBreakGlyph glyph = (LineBreakGlyph) glyphInfo.glyph;
            pages.text.glyphs.set(glyphInfo.index, GSIDesignSettings.getLineBreakGlyph(glyph.pageBreak, glyph.lineStyle));
            viewport.gsi.build();
        }else{
            //if we didn't find a line break glyph anywhere then the text is all one line, so we can add one to the start
            pages.text.addGlyph(GSIDesignSettings.getLineBreakGlyph(false, new LineStyle()), 0);
            viewport.gsi.build();
            moveCursorRight(cursor);
        }
    }

    public void applyLineStyle(LineStyle style){
        //TODO IF THERE IS A SELECTION, WE NEED TO DO THIS TO ALL SELECTED LINES!
        GlyphRenderInfo glyphInfo = pages.getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
        if(glyphInfo != null){
            LineBreakGlyph glyph = (LineBreakGlyph) glyphInfo.glyph;
            glyph.lineStyle = style.copy();
            viewport.gsi.build();
        }else{
            //if we didn't find a line break glyph anywhere then the text is all one line, so we can add one to the start
            pages.text.addGlyph(GSIDesignSettings.getLineBreakGlyph(false, style.copy()), 0);
            viewport.gsi.build();
            moveCursorRight(cursor);
        }
    }

    public void clearFormatting(){
        applyLineStyle(new LineStyle());
        pages.text.clearAttributes(getSelectionStartIndex(), getSelectionEndIndex());
        onCursorMoved();
        viewport.gsi.build();
    }

    public void applyAttribute(GlyphStyleAttributes attribute, Object attributeObj){
        pages.text.applyAttribute(attribute, attributeObj, getSelectionStartIndex(), getSelectionEndIndex());
        viewport.gsi.build();
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