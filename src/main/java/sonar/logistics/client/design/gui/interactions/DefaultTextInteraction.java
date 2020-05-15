package sonar.logistics.client.design.gui.interactions;

import sonar.logistics.client.design.gui.EnumGlyphStyling;
import sonar.logistics.client.design.gui.EnumLineStyling;
import sonar.logistics.client.design.gui.GSIDesignSettings;
import sonar.logistics.client.design.gui.interactions.hotkeys.HotKeyFunctions;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.gsi.components.text.*;
import sonar.logistics.client.gsi.components.text.api.CursorPoint;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.glyph.AttributeGlyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.render.*;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.vectors.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DefaultTextInteraction extends AbstractViewportInteraction implements IGlyphRenderer {

    public CursorPoint cursor = null;
    public CursorPoint selectionEnd = null;

    public DefaultTextInteraction(GSIViewportWidget viewport) {
        super(viewport);
    }

    /////

    public StyledTextComponent text(){
        return (StyledTextComponent)viewport.selectedComponent;
    }

    public StyledTextPages pages(){
        return text().textWrapper.styledTextPages;
    }

    public boolean isDrawingHighlights = false;

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        isDrawingHighlights = false;
    }

    @Override
    public void renderGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        glyphInfo.glyph.render(context, glyphInfo);

        if(cursor == null || selectionEnd == null || selectionEnd.getInsertionIndex() == cursor.getInsertionIndex()){
            if(cursor != null && glyphInfo.index == cursor.index && GSIDesignSettings.canRenderCursor()){
                float downscale = glyphInfo.glyph.downscale(context.fontType, glyphInfo.style);
                StyledTextRenderer.addCursorToGlyph(context, glyphInfo, downscale, (float)(cursor.isLeading ? 0 : glyphInfo.quad.width / downscale));
            }
        }else{
            CursorPoint startPoint = selectionEnd.getInsertionIndex() < cursor.getInsertionIndex() ? selectionEnd : cursor;
            CursorPoint endPoint = selectionEnd.getInsertionIndex() > cursor.getInsertionIndex() ? selectionEnd : cursor;

            if(startPoint.getInsertionIndex() <= glyphInfo.index && endPoint.getInsertionIndex() > glyphInfo.index){
                float downscale = glyphInfo.glyph.downscale(context.fontType, glyphInfo.style);
                StyledTextRenderer.addHighlightToGlyph(context, glyphInfo, downscale, (float)(glyphInfo.quad.width / downscale));
            }
        }
    }

    public CursorPoint getTextCursorFromMouse(double mouseX, double mouseY){
        Vector2D displayHit = viewport.getHitVecFromMouse(mouseX, mouseY);
        List<StyledTextLine> page = getCurrentPage();
        if(page.isEmpty()){
            return null; //TODO ADD BASIC GLYPH / SHOULD ONE LINE ALWAYS EXIST, OR WE DO THIS ON TYPING?
        }

        IGlyphType hitGlyph = null;
        boolean isLeading = false;
        int index = 0;

        //first we find the line the mouses' y hit lines up with
        StyledTextLine line = null;
        for(StyledTextLine l : getCurrentPage()){
            if(l.renderSize.containsY(displayHit.y)){
                line = l;
                break;
            }
        }

        //if we didn't find the line, we will set the cursor to either the first or last line instead
        if(line == null){
            StyledTextLine firstLine = page.get(0);
            if(displayHit.y < firstLine.renderSize.getY()){
                line = firstLine;
            }else{
                line = page.get(page.size() -1 );
            }
        }

        ///if the line contains the text hit a specific glyph will be found
        if(line.renderSize.containsX(displayHit.x)){
            GlyphRenderInfo glyphHit = text().getGlyphHit(line, g -> g.quad.containsX(displayHit.x) && g.quad.width != 0 && g.glyph.isVisible());
            if(glyphHit != null) {
                hitGlyph = glyphHit.glyph;
                isLeading = glyphHit.quad.getCentreX() > displayHit.x;
                index = glyphHit.index;
            }
        }

        ///if no glyph has been found we will instead set the cursor to either the first or last visible glyph, if not glyphs are visible, it will be set to the end of the line.
        if(hitGlyph == null){
            /* TODO
            List<IGlyphType> visible = line.glyphInfo.stream().filter(IGlyphType::isVisible).collect(Collectors.toList());
            if(visible.isEmpty()){
                index += line.glyphs.size()-1;
                index += line.index;
            }else if(textHit.x < line.bounds.getX()) {
                isLeading = true;
                index += line.glyphs.indexOf(hitGlyph);
                index += line.index;
            }else {
                isLeading = false;
                index += line.glyphs.indexOf(hitGlyph);
                index += line.index;
            }

             */
        }


        return new CursorPoint(isLeading, index);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean dragged = super.mouseClicked(mouseX, mouseY, button);
        if (button == 0) {
            cursor = getTextCursorFromMouse(mouseX, mouseY);
            selectionEnd = null;
        }
        return dragged;
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        return HotKeyFunctions.triggerHotKey(this, key, scanCode, modifiers);
    }

    //// DRAGGING METHODS \\\\\

    @Override
    public void onDragStarted(double mouseX, double mouseY, int button) {
        super.onDragStarted(mouseX, mouseY, button);
        selectionEnd = null;
    }

    @Override
    public void onDragFinished(double mouseX, double mouseY, int button) {
        super.onDragFinished(mouseX, mouseY, button);

    }

    @Override
    public void onDragged(double mouseX, double mouseY, int button) {
        super.onDragged(mouseX, mouseY, button);
        if(cursor != null) {
            selectionEnd = getTextCursorFromMouse(mouseX, mouseY);
        }
    }

    ///// HELPER METHODS \\\\\

    public List<StyledTextLine> getCurrentPage(){
        return text().textWrapper.styledTextPages.getCurrentPage(text().page);
    }

    ///// TEXT EDITING / TYPING \\\\\

    @Override
    public boolean charTyped(char aChar, int modifiers) {
        addChar(aChar);
        return true;
    }

    @Override
    public void onGlyphStyleChanged(EnumGlyphStyling settings) {
        super.onGlyphStyleChanged(settings);
        applyAttribute(settings.getAttributeGlyph(GSIDesignSettings.glyphStyle));
    }

    @Override
    public void onLineStyleChanged(EnumLineStyling styling) {
        super.onLineStyleChanged(styling);
        applyLineStyle(GSIDesignSettings.lineStyle);
    }

    ////

    public void addChar(char aChar){
        if(cursor == null) {
            return;
        }
        deleteSelected();
        text().glyphString.addChar(aChar, cursor.getInsertionIndex());
        viewport.gsi.rebuild();
        cursor.moveRight(1);
    }

    public void enter(){
        if(cursor == null) {
            return;
        }
        deleteSelected();
        GlyphRenderInfo glyphInfo = pages().getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
        text().glyphString.addLineBreak(glyphInfo == null ? new LineStyle() : ((LineBreakGlyph)glyphInfo.glyph).styling.copy(), cursor.getInsertionIndex());
        viewport.gsi.rebuild();
        cursor.moveRight(1);
    }

    public void backspace(){
        if(cursor == null || deleteSelected()) {
            return;
        }
        if(cursor.getInsertionIndex() - 1 >= 0) {
            text().glyphString.glyphs.remove(cursor.getInsertionIndex() - 1);
            viewport.gsi.rebuild();
            cursor.moveLeft(1);
        }
    }

    public void delete(){
        if(cursor == null || deleteSelected()) {
            return;
        }
        if(cursor.getInsertionIndex()  < text().glyphString.glyphs.size()) {
            text().glyphString.glyphs.remove(cursor.getInsertionIndex());
            viewport.gsi.rebuild();
        }
    }

    public boolean deleteSelected(){
        if(cursor != null && selectionEnd != null){
            int startIndex = Math.min(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
            int endIndex = Math.max(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
            List<GlyphRenderInfo> selected = getEditableSelection(startIndex, endIndex);
            if(!selected.isEmpty()){
                //TODO CHECK STYLING ISN'T BROKEN BY DELETION.
                selected.forEach(s -> text().glyphString.glyphs.remove(s.glyph));
                viewport.gsi.rebuild();
            }
            return true;
        }
        return false;
    }

    public void applyLineStyle(LineStyle style){
        if(cursor == null) {
            return;
        }
        GlyphRenderInfo glyphInfo = pages().getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
        if(glyphInfo != null){
            text().glyphString.glyphs.set(glyphInfo.index, new LineBreakGlyph(((LineBreakGlyph)glyphInfo.glyph).page, style.copy()));
            viewport.gsi.rebuild();
        }
    }

    public void applyGlyphStyle(GlyphStyle style){
        applyAttribute(style.getAttributes());
    }

    public void applyAttribute(AttributeGlyph ...attributes){
        if(cursor == null || selectionEnd == null){
            return;
        }
        int startIndex = Math.min(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
        int endIndex = Math.max(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
        List<GlyphRenderInfo> selected = getEditableSelection(startIndex, endIndex);
        if(!selected.isEmpty()){

            GlyphStyle startStyle = pages().getGlyphInfo(Math.max(0, startIndex - 1)).style;
            GlyphStyle endStyle = pages().getGlyphInfo(Math.min(text().glyphString.glyphs.size()-1, endIndex)).style;

            for (AttributeGlyph attribute : attributes) {
                attribute.alterString(text().glyphString, selected, startStyle, endStyle);
            }

            viewport.gsi.rebuild();
        }
    }

    public List<GlyphRenderInfo> getEditableSelection(int startIndex, int endIndex){
        if(cursor == null){
            return new ArrayList<>();
        }
        if(selectionEnd != null){
            return pages().getSelection(startIndex, endIndex + 1);
        }
        return new ArrayList<>();
    }


    ///// CURSOR MOVEMENT \\\\\

    /*
    public void moveToNextPage(CursorPoint cursor){
        if(text().page + 1 < text().pageCount){
            text().page ++;
            List<StyledTextLine> lines = getCurrentPage();
            StyledTextLine line = lines.get(0);
            cursor.glyph = line.glyphs.get(0);
        }
    }

    public void moveToPrevPage(CursorPoint cursor){
        if(text().page - 1 >= 0){
            text().page --;
            List<StyledTextLine> lines = getCurrentPage();
            StyledTextLine line = lines.get(lines.size()-1);
            cursor.glyph = line.glyphs.get(line.glyphs.size()-1);
        }
    }

     */

    //should be called before moving the cursor
    public void clearSelection(){
        selectionEnd = null;
    }

    //should be called before moving the selection cursor
    public void checkSelection(){
        if(selectionEnd == null){
            selectionEnd = cursor;
        }
    }

    public void moveCursorUp(CursorPoint cursor){
        GlyphRenderInfo glyphRenderInfo = pages().getGlyphInfo(cursor.index);
        StyledTextLine line = pages().getPrevStyledLine(cursor.index);
        if(line != null) {
            double cursorX = cursor.isLeading ? glyphRenderInfo.quad.getX() : glyphRenderInfo.quad.getMaxX();
            GlyphRenderInfo newHit = text().getGlyphHit(line, g -> g.quad.containsX(cursorX) && StyledTextPages.FILTER_VISIBLE.apply(g));
            if (newHit != null) {
                cursor.index = newHit.index;
            }
        }
    }

    public void moveCursorDown(CursorPoint cursor){
        GlyphRenderInfo glyphRenderInfo = pages().getGlyphInfo(cursor.index);
        StyledTextLine line = pages().getNextStyledLine(cursor.index);
        if(line != null){
            double cursorX = cursor.isLeading ? glyphRenderInfo.quad.getX() : glyphRenderInfo.quad.getMaxX();
            GlyphRenderInfo newHit = text().getGlyphHit(line, g -> g.quad.containsX(cursorX) && StyledTextPages.FILTER_VISIBLE.apply(g));
            if(newHit != null){
                cursor.index = newHit.index;
            }
        }
    }

    public void moveCursorLeft(CursorPoint cursor){
        if(!cursor.isLeading){
            cursor.isLeading = true;
            return;
        }
        GlyphRenderInfo glyphInfo = pages().getPrevGlyphInfo(cursor.index, StyledTextPages.FILTER_VISIBLE);
        if(glyphInfo != null){
            cursor.index = glyphInfo.index;
        }
    }

    public void moveCursorRight(CursorPoint cursor){
        if(cursor.isLeading){
            cursor.isLeading = false;
            return;
        }
        GlyphRenderInfo glyphInfo = pages().getNextGlyphInfo(cursor.index, StyledTextPages.FILTER_VISIBLE);
        if(glyphInfo != null){
            cursor.index = glyphInfo.index;
        }
    }


    public void moveCursorToStart(CursorPoint cursor){
        StyledTextLine line = pages().getStyledLine(cursor.getCharIndex());
        if(line != null) {
            GlyphRenderInfo glyphInfo = pages().getNextGlyphInfo(line.getStartIndex() - 1, StyledTextPages.FILTER_VISIBLE);
            if(glyphInfo != null) {
                cursor.index = glyphInfo.index;
                cursor.isLeading = true;
            }
        }
    }

    public void moveCursorToEnd(CursorPoint cursor){
        StyledTextLine line = pages().getStyledLine(cursor.getCharIndex());
        if(line != null) {
            GlyphRenderInfo glyphInfo = pages().getPrevGlyphInfo(line.getEndIndex() + 1, StyledTextPages.FILTER_VISIBLE);
            if(glyphInfo != null) {
                cursor.index = glyphInfo.index;
                cursor.isLeading = false;
            }
        }
    }

}