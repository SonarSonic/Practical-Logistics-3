package sonar.logistics.client.design.gui.interactions;

import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.design.gui.EnumLineStyling;
import sonar.logistics.client.design.gui.GSIDesignSettings;
import sonar.logistics.client.design.gui.interactions.hotkeys.HotKeyFunctions;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.gsi.components.text.api.CursorPoint;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.glyph.CharGlyph;
import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.render.*;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.vectors.Vector2D;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

//TODO RENDER OFF PAGE GLYPHS???
public class DefaultTextInteraction extends AbstractViewportInteraction implements IGlyphRenderer {

    @Nonnull
    public CursorPoint cursor = new CursorPoint(false, 0);
    public CursorPoint selectionEnd = null;
    public StyledTextPages pages;

    public DefaultTextInteraction(GSIViewportWidget viewport, StyledTextPages pages) {
        super(viewport);
        this.pages = pages;
        this.pages.specialGlyphRenderer = this;
    }

    /////

    public boolean isDrawingHighlights = false;

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        isDrawingHighlights = false;
    }

    //IGlyphRenderer method, used for rendering cursors / selections etc.
    @Override
    public void renderGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        if(!glyphInfo.glyph.isVisible()){
            if(glyphInfo.glyph instanceof LineBreakGlyph){
                StyledTextRenderer.renderCharGlyph(context, glyphInfo, new CharGlyph('#'));
            }
        }

        glyphInfo.glyph.render(context, glyphInfo);

        if(selectionEnd == null || selectionEnd.getInsertionIndex() == cursor.getInsertionIndex()){
            if(glyphInfo.index == cursor.getCharIndex() && GSIDesignSettings.canRenderCursor()){
                float downscale = glyphInfo.glyph.downscale(context.fontType, glyphInfo.style);
                StyledTextRenderer.addCursorToGlyph(context, glyphInfo, downscale, (float)(cursor.isLeading() ? 0 : glyphInfo.quad.width / downscale));
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

    /////

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean dragged = super.mouseClicked(mouseX, mouseY, button);
        if (button == 0) {
            cursor = getTextCursorFromMouse(mouseX, mouseY);
            selectionEnd = null;
            onCursorMoved();
        }
        return dragged;
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        return HotKeyFunctions.triggerHotKey(this, key, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char aChar, int modifiers) {
        CharGlyph glyph = new CharGlyph(aChar);
        glyph.styleHolder.setFromStyle(GSIDesignSettings.glyphStyle);
        addGlyph(glyph);
        return true;
    }

    ////

    @Override
    public void onDragStarted(double mouseX, double mouseY, int button) {
        super.onDragStarted(mouseX, mouseY, button);
        selectionEnd = null;
    }

    @Override
    public void onDragged(double mouseX, double mouseY, int button) {
        super.onDragged(mouseX, mouseY, button);
        selectionEnd = getTextCursorFromMouse(mouseX, mouseY);
    }

    ////

    public void addGlyph(Glyph glyph){
        deleteSelection();
        pages.text.glyphs.add(getSafeInsertionIndex(), glyph);
        viewport.gsi.rebuild();
        cursor.setLeading(false);
        cursor.moveRight(1);
        onCursorMoved();
    }

    public void deleteGlyph(boolean previous){
        if(deleteSelection()) {
            return;
        }
        int delete = cursor.getInsertionIndex() - (previous ? 1 : 0);
        if(delete == -1 || delete > pages.text.glyphs.size() -1){
            return;
        }
        pages.text.glyphs.remove(delete);
        viewport.gsi.rebuild();
        cursor.setIndex(cursor.isLeading() ? delete : delete - 1);
        onCursorMoved();
    }

    public boolean deleteSelection(){
        if(selectionEnd == null){
            return false;
        }
        int startIndex = Math.min(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
        int endIndex = Math.max(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());

        if(pages.text.deleteGlyphs(startIndex, endIndex)){
            viewport.gsi.rebuild();
            selectionEnd = null;
            return true;
        }
        return false;
    }

    //// LINE STYLING / GLYPH STYLING

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

    public void applyLineStyle(LineStyle style){
        GlyphRenderInfo glyphInfo = pages.getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
        if(glyphInfo != null){
            pages.text.glyphs.set(glyphInfo.index, new LineBreakGlyph(((LineBreakGlyph)glyphInfo.glyph).page, style.copy()));
            viewport.gsi.rebuild();
        }else{
            //if we didn't find a line break glyph anywhere then the text is all one line, so we can add one to the start
            pages.text.addLineBreak(style, 0);
            viewport.gsi.rebuild();
            cursor.moveRight(1);
            onCursorMoved();
        }
    }

    public void applyAttribute(GlyphStyleAttributes attribute, Object attributeObj){
        int startIndex, endIndex;
        if(selectionEnd == null){
            GlyphRenderInfo prev = pages.getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
            GlyphRenderInfo next = pages.getNextGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);

            startIndex = prev == null ? 0 : prev.index + 1;
            endIndex = next == null ? pages.styledGlyphs.size() - 1 : next.index - 1;
        }else{
            startIndex = Math.min(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
            endIndex = Math.max(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
        }
        pages.text.applyAttribute(attribute, attributeObj, startIndex, endIndex);
        viewport.gsi.rebuild();
    }

    //// CURSOR MOVEMENT

    //should be called before moving the cursor, n.b. this is normally done by HotKeys
    public void clearSelection(){
        selectionEnd = null;
    }

    //should be called before moving the selection cursor, n.b. this is normally done by HotKeys
    public void checkSelection(){
        if(selectionEnd == null){
            selectionEnd = cursor;
        }
    }

    ////

    //moves the cursor to the glyph inline vertically on the next line down
    public void moveCursorDown(CursorPoint cursor){
        GlyphRenderInfo glyphRenderInfo = pages.getGlyphInfo(cursor.getCharIndex());
        if(glyphRenderInfo != null) {
            StyledTextLine line = pages.getNextStyledLine(cursor.getCharIndex());
            updateCursorFromXHit(cursor, line, cursor.isLeading() ? glyphRenderInfo.quad.getX() : glyphRenderInfo.quad.getMaxX());
        }
    }

    //moves the cursor to the glyph inline vertically on the next line up
    public void moveCursorUp(CursorPoint cursor){
        GlyphRenderInfo glyphRenderInfo = pages.getGlyphInfo(cursor.getCharIndex());
        if(glyphRenderInfo != null) {
            StyledTextLine line = pages.getPrevStyledLine(cursor.getCharIndex());
            updateCursorFromXHit(cursor, line, cursor.isLeading() ? glyphRenderInfo.quad.getX() : glyphRenderInfo.quad.getMaxX());
        }
    }

    ////

    //moves the cursor to the previous visible glyph
    public void moveCursorLeft(CursorPoint cursor){
        GlyphRenderInfo glyphInfo = pages.getPrevGlyphInfo(cursor.getInsertionIndex(), StyledTextPages.FILTER_VISIBLE);
        setCursorTo(cursor, glyphInfo, true);
    }

    //moves the cursor to the next visible glyph
    public void moveCursorRight(CursorPoint cursor){
        GlyphRenderInfo glyphInfo = pages.getNextGlyphInfo(cursor.getInsertionIndex(), StyledTextPages.FILTER_VISIBLE);
        setCursorTo(cursor, glyphInfo, false);
    }

    ////

    //moves the cursor to the first visible glyph in the current line
    public void moveCursorToStart(CursorPoint cursor){
        StyledTextLine line = pages.getStyledLine(cursor.getCharIndex());
        if(line != null) {
            GlyphRenderInfo glyphInfo = pages.getNextGlyphInfo(line.getStartIndex() - 1, StyledTextPages.FILTER_VISIBLE);
            setCursorTo(cursor, glyphInfo, true);
        }
    }

    //moves the cursor to the last visible glyph in the current line
    public void moveCursorToEnd(CursorPoint cursor){
        StyledTextLine line = pages.getStyledLine(cursor.getCharIndex());
        if(line != null) {
            GlyphRenderInfo glyphInfo = pages.getPrevGlyphInfo(line.getEndIndex() + 1, StyledTextPages.FILTER_VISIBLE);
            setCursorTo(cursor, glyphInfo, false);
        }
    }

    /////

    @Nonnull
    public CursorPoint getTextCursorFromMouse(double mouseX, double mouseY){
        Vector2D displayHit = viewport.getHitVecFromMouse(mouseX, mouseY);
        List<StyledTextLine> page = pages.getCurrentPage();

        //if the page is empty we move to the previous one, unless it's the first
        if(page.isEmpty()){
            if(pages.page > 0){
                pages.page--;
                return getTextCursorFromMouse(mouseX, mouseY);
            }
            return new CursorPoint(false, 0);
        }

        //first we find the line the mouses' y hit lines up with
        StyledTextLine line = null;
        for(StyledTextLine l : pages.getCurrentPage()){
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

        CursorPoint cursor = new CursorPoint(false, 0);
        updateCursorFromXHit(cursor, line, displayHit.x);
        return cursor;
    }

    ////

    //moves the given cursor to the nearest glyph to the given x hit within the line
    public void updateCursorFromXHit(CursorPoint cursor, StyledTextLine line, double xHit){

        ///if the line contains the text hit a specific glyph will be found
        if(line.renderSize.containsX(xHit)){
            GlyphRenderInfo glyphHit = pages.getGlyphHit(line, g -> g.quad.containsX(xHit) && StyledTextPages.FILTER_VISIBLE.apply(g));
            if(glyphHit != null) {
                cursor.setLeading(glyphHit.quad.getCentreX() > xHit);
                cursor.setIndex(glyphHit.index);
                return;
            }
        }

        ///if no glyph has been found we will instead set the cursor to either the first or last visible glyph, if not glyphs are visible, it will be set to the end of the line.
        cursor.setIndex(line.getStartIndex());
        if(xHit < line.renderSize.getX()) {
            moveCursorToStart(cursor);
        }else {
            moveCursorToEnd(cursor);
        }

    }

    ////

    //returns a valid insertion index, within the bounds of the string
    public int getSafeInsertionIndex(){
        return Math.max(0, Math.min(pages.styledGlyphs.size(), cursor.getInsertionIndex()));
    }

    //returns a value within the bounds of the glyphs
    public int getSafeIndex(int index){
        return Math.max(0, Math.min(pages.styledGlyphs.size()-1, index));
    }

    //method for simplicity, makes sure onCursorMoved() is called.
    public void setCursorTo(CursorPoint cursor, @Nullable GlyphRenderInfo glyphRenderInfo, boolean isLeading){
        if(glyphRenderInfo != null){
            cursor.setIndex(glyphRenderInfo.index);
            cursor.setLeading(isLeading);
            onCursorMoved();
        }
    }

    //updates the current GlyphStyle and LineStyle from the cursor's new position
    public void onCursorMoved(){
        if(selectionEnd == null && !pages.styledGlyphs.isEmpty()){
            StyledTextLine line = pages.getStyledLine(getSafeIndex(cursor.getInsertionIndex()));
            GlyphRenderInfo info = pages.getGlyphInfo(getSafeIndex(cursor.getInsertionIndex()));
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