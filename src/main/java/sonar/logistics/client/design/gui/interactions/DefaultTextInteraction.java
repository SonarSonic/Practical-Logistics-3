package sonar.logistics.client.design.gui.interactions;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import sonar.logistics.client.design.gui.EnumLineBreakGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
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
import sonar.logistics.client.gsi.components.text.style.GlyphStyleHolder;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.vectors.Vector2D;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

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
               // StyledTextRenderer.INSTANCE.renderCharGlyph(context, glyphInfo, new CharGlyph('#'));
            }
        }

        glyphInfo.glyph.render(context, glyphInfo);

        if(selectionEnd == null || selectionEnd.getInsertionIndex() == cursor.getInsertionIndex()){
            if(glyphInfo.index == cursor.getCharIndex() && GSIDesignSettings.canRenderCursor()){
                float downscale = glyphInfo.glyph.downscale(context.fontType, glyphInfo.style);
                StyledTextRenderer.INSTANCE.addCursorToGlyph(context, glyphInfo, downscale, (float)(cursor.isLeading() ? 0 : glyphInfo.quad.width / downscale));
            }
        }else{
            CursorPoint startPoint = selectionEnd.getInsertionIndex() < cursor.getInsertionIndex() ? selectionEnd : cursor;
            CursorPoint endPoint = selectionEnd.getInsertionIndex() > cursor.getInsertionIndex() ? selectionEnd : cursor;

            if(startPoint.getInsertionIndex() <= glyphInfo.index && endPoint.getInsertionIndex() > glyphInfo.index){
                float downscale = glyphInfo.glyph.downscale(context.fontType, glyphInfo.style);
                StyledTextRenderer.INSTANCE.addHighlightToGlyph(context, glyphInfo, downscale, (float)(glyphInfo.quad.width / downscale));
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
        addGlyph(new CharGlyph(aChar), GSIDesignSettings.glyphStyle);
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

    public void addGlyph(Glyph glyph, GlyphStyle style){
        deleteSelection();

        glyph.styleHolder = new GlyphStyleHolder();
        glyph.styleHolder.setFromStyle(style);

        int insert = getSafeGlyphInsertionIndex();
        pages.text.glyphs.add(insert, glyph);
        viewport.gsi.build();
        moveCursorTo(cursor, insert, false);
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
        viewport.gsi.build();
        cursor.setIndex(cursor.isLeading() ? delete : delete - 1);
        onCursorMoved();
    }

    public boolean deleteSelection(){
        if(selectionEnd == null){
            return false;
        }
        int startIndex = Math.min(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
        int endIndex = Math.max(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());

        clearSelection();
        if(pages.text.deleteGlyphs(startIndex, endIndex)){
            viewport.gsi.build();
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

    public int getSelectionStartIndex(){
        if(selectionEnd == null){
            GlyphRenderInfo prev = pages.getPrevGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
            return prev == null ? 0 : prev.index; //note we set the style on the preceding line break glyph too!
        }
        return Math.min(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
    }

    public int getSelectionEndIndex(){
        if(selectionEnd == null){
            GlyphRenderInfo next = pages.getNextGlyphInfo(cursor.getInsertionIndex(), glyphRenderInfo -> glyphRenderInfo.glyph instanceof LineBreakGlyph);
            return next == null ? pages.styledGlyphs.size() : next.index - 1;
        }
        return Math.max(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex()) - 1;
    }

    //// CURSOR MOVEMENT

    //should be called before moving the cursor, n.b. this is normally done by HotKeys
    public void clearSelection(){
        selectionEnd = null;
    }

    //should be called before moving the selection cursor, n.b. this is normally done by HotKeys
    public void checkSelection(){
        if(selectionEnd == null){
            selectionEnd = new CursorPoint(cursor.isLeading(), cursor.getCharIndex());
        }
    }

    public void selectAll(){
        cursor = new CursorPoint(true, 0);
        selectionEnd = new CursorPoint(false, pages.text.glyphs.size() - 1);
    }

    public void cut(){
        copy();
        deleteSelection();
    }

    public void copy(){
        if(selectionEnd == null){
            return;
        }
        int startIndex = Math.min(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
        int endIndex = Math.max(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());

        StringBuilder builder = new StringBuilder();
        GlyphStyle style = new GlyphStyle();

        for(int i = startIndex ; i <= Math.min(endIndex, pages.text.glyphs.size() - 1); i ++){
            GlyphRenderInfo info = pages.getGlyphInfo(i);
            if(!info.style.matches(style)){
                builder.append(TextFormatting.RESET);
                if(info.style.obfuscated){
                    builder.append(TextFormatting.OBFUSCATED);
                }
                if(info.style.bold){
                    builder.append(TextFormatting.BOLD);
                }
                if(info.style.strikethrough){
                    builder.append(TextFormatting.STRIKETHROUGH);
                }
                if(info.style.underlined){
                    builder.append(TextFormatting.UNDERLINE);
                }
                if(info.style.italic){
                    builder.append(TextFormatting.ITALIC);
                }
                //COPYING COLOUR ISN'T SUPPORTED FOR NOW
            }
            style = info.style.copy();
            if(info.glyph instanceof LineBreakGlyph){
                builder.append('\n');
            }else if(info.glyph instanceof CharGlyph){
                builder.append(((CharGlyph) info.glyph).aChar);
            }
        }

        Minecraft.getInstance().keyboardListener.setClipboardString(builder.toString());
    }

    ///pastes the clipboard's string, can use Minecraft Formatting.
    public void paste(){
        String string = Minecraft.getInstance().keyboardListener.getClipboardString();

        GlyphStyle style = GSIDesignSettings.glyphStyle.copy();

        //if we have formatting char '167' we reset all TextFormatting properties.
        if(string.indexOf(167) != -1){
            style.obfuscated = false;
            style.bold = false;
            style.strikethrough = false;
            style.underlined = false;
            style.italic = false;
            style.textColour = new ColourProperty(255, 255, 255);
        }

        for(int i = 0; i < string.length(); ++i) {
            char c0 = string.charAt(i);

            if (c0 == 167 && i + 1 < string.length()) {
                TextFormatting textformatting = TextFormatting.fromFormattingCode(string.charAt(i + 1));
                if (textformatting != null) {
                    if (textformatting.isNormalStyle()) {
                        style.obfuscated = false;
                        style.bold = false;
                        style.strikethrough = false;
                        style.underlined = false;
                        style.italic = false;
                        style.textColour = new ColourProperty(255, 255, 255);
                    }
                    if (textformatting.getColor() != null) {
                        int j = textformatting.getColor();
                        style.textColour = new ColourProperty((j >> 16 & 255), (j >> 8 & 255), (j & 255));
                    } else if (textformatting == TextFormatting.OBFUSCATED) {
                        style.obfuscated = true;
                    } else if (textformatting == TextFormatting.BOLD) {
                        style.bold = true;
                    } else if (textformatting == TextFormatting.STRIKETHROUGH) {
                        style.strikethrough = true;
                    } else if (textformatting == TextFormatting.UNDERLINE) {
                        style.underlined = true;
                    } else if (textformatting == TextFormatting.ITALIC) {
                        style.italic = true;
                    }
                }
                ++i;
            }else if('\n' == c0) {
                addGlyph(GSIDesignSettings.getLineBreakGlyph(false, GSIDesignSettings.lineStyle.copy()), style);
            }else{
                addGlyph(new CharGlyph(c0), style);
            }
        }
    }

    ////

    //moves the cursor to the glyph inline vertically on the next line down
    public void moveCursorDown(CursorPoint cursor){
        GlyphRenderInfo glyphRenderInfo = pages.getGlyphInfo(cursor.getCharIndex());
        if(glyphRenderInfo != null) {
            StyledTextLine line = pages.getNextStyledLine(cursor.getCharIndex());
            updateCursorFromXHit(cursor, line, cursor.isLeading() ? glyphRenderInfo.quad.getX() : glyphRenderInfo.quad.getMaxX());
            onCursorMoved();
        }
    }

    //moves the cursor to the glyph inline vertically on the next line up
    public void moveCursorUp(CursorPoint cursor){
        GlyphRenderInfo glyphRenderInfo = pages.getGlyphInfo(cursor.getCharIndex());
        if(glyphRenderInfo != null) {
            StyledTextLine line = pages.getPrevStyledLine(cursor.getCharIndex());
            updateCursorFromXHit(cursor, line, cursor.isLeading() ? glyphRenderInfo.quad.getX() : glyphRenderInfo.quad.getMaxX());
            onCursorMoved();
        }
    }

    ////

    public void moveCursorLeft(CursorPoint cursor){
        if(!cursor.isLeading()){
            cursor.setLeading(true);
            onCursorMoved();
            return;
        }
        moveCursorTo(cursor, cursor.getInsertionIndex() - 1, true);
        onCursorMoved();
    }

    public void moveCursorRight(CursorPoint cursor){
        if(cursor.isLeading()){
            cursor.setLeading(false);
            onCursorMoved();
            return;
        }
        moveCursorTo(cursor,cursor.getCharIndex() + 1, false);
        onCursorMoved();
    }

    ////

    //moves the cursor to the first visible glyph in the current line
    public void moveCursorToStart(CursorPoint cursor){
        StyledTextLine line = pages.getStyledLine(cursor.getCharIndex());
        if(line != null) {
            moveCursorTo(cursor, line.getStartIndex(), true);
            onCursorMoved();
        }
    }

    //moves the cursor to the last visible glyph in the current line
    public void moveCursorToEnd(CursorPoint cursor){
        StyledTextLine line = pages.getStyledLine(cursor.getCharIndex());
        if(line != null) {
            moveCursorTo(cursor, line.getEndIndex(), false);
            onCursorMoved();
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

    ///doesn't call onCursorMoved
    public void moveCursorTo(CursorPoint cursor, int index, boolean isLeading){
        if(pages.styledGlyphs.isEmpty()){
            cursor.setIndex(0);
            cursor.setLeading(true);
        }else{
            int safeIndex = getSafeIndex(index);
            GlyphRenderInfo info = pages.getGlyphInfo(safeIndex);
            Objects.requireNonNull(info);
            cursor.setIndex(info.index);
            cursor.setLeading(isLeading);
        }
    }

    //returns a valid insertion index, within the bounds of the string
    public int getSafeInsertionIndex(){
        return Math.max(0, Math.min(pages.styledGlyphs.size(), cursor.getInsertionIndex()));
    }

    //if the cursor is on a line break glyph, it will place the char afterwards
    public int getSafeGlyphInsertionIndex(){
        int safeIndex = Math.max(0, Math.min(pages.styledGlyphs.size() - 1, cursor.getCharIndex()));
        if(!pages.styledGlyphs.isEmpty()){
            GlyphRenderInfo info = pages.getGlyphInfo(safeIndex);
            Objects.requireNonNull(info);
            if(info.glyph instanceof LineBreakGlyph){
                return info.index + 1;
            }
        }
        return getSafeInsertionIndex();
    }

    //returns a value within the bounds of the glyphs
    public int getSafeIndex(int index){
        return Math.max(0, Math.min(pages.styledGlyphs.size()-1, index));
    }

    //updates the current GlyphStyle and LineStyle from the cursor's new position
    public void onCursorMoved(){
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