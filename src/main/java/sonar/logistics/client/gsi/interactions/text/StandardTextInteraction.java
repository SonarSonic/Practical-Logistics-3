package sonar.logistics.client.gsi.interactions.text;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import sonar.logistics.client.gsi.api.ITextComponent;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.interactions.AbstractComponentInteraction;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gsi.components.text.render.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.glyph.CharGlyph;
import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.render.*;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleHolder;
import sonar.logistics.client.gsi.properties.ColourProperty;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class StandardTextInteraction<C extends ITextComponent> extends AbstractComponentInteraction<C> implements IGlyphRenderer {

    @Nonnull
    public CursorPoint cursor;
    public CursorPoint selectionEnd;
    public StyledTextPages pages;

    public StandardTextInteraction(C component) {
        super(component);
        this.cursor = new CursorPoint(false, 0);
        this.selectionEnd = null;
        this.pages = component.pages();
        this.pages.specialGlyphRenderer = this;
    }


    public GlyphStyle getGlyphStyle(){
        return new GlyphStyle();
    }

    /////IGlyphRenderer - used for rendering cursors / selections etc.

    @Override
    public void renderGlyph(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        glyphInfo.glyph.render(context, glyphInfo);
    }

    @Override
    public void renderEffects(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        StyledTextRenderer.INSTANCE.addStylingEffects(context, glyphInfo);

        if(component.isFocusedComponent()) {
            if (selectionEnd == null || selectionEnd.getInsertionIndex() == cursor.getInsertionIndex()) {
                if (glyphInfo.index == cursor.getCharIndex() && GSIDesignSettings.canRenderCursor()) {
                    StyledTextRenderer.INSTANCE.addCursorToGlyph(context, glyphInfo, cursor);
                }
            } else {
                CursorPoint startPoint = selectionEnd.getInsertionIndex() < cursor.getInsertionIndex() ? selectionEnd : cursor;
                CursorPoint endPoint = selectionEnd.getInsertionIndex() > cursor.getInsertionIndex() ? selectionEnd : cursor;

                if (startPoint.getInsertionIndex() <= glyphInfo.index && endPoint.getInsertionIndex() > glyphInfo.index) {
                    StyledTextRenderer.INSTANCE.addHighlightToGlyph(context, glyphInfo);
                }
            }
        }
    }

    /////

    @Override
    public boolean mouseClicked(int button) {
        if (button == 0) {
            cursor = getCursorFromMouse();
            selectionEnd = null;
            onCursorMoved();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return TextHotKeyFunctions.triggerHotKey(this, getInteractionHandler(), keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int modifiers) {
        addGlyph(new CharGlyph(c), getGlyphStyle());
        return true;
    }

    @Override
    public boolean changeFocus(boolean focused) {
        if(!focused){
            this.cursor = new CursorPoint(false, 0);
            this.selectionEnd = null;
        }
        return true;
    }

    ////


    @Override
    public boolean canStartDrag(int button) {
        return isDragButton(button);
    }

    @Override
    public void onDragStarted(int button) {
        selectionEnd = null;
    }

    @Override
    public boolean mouseDragged() {
        selectionEnd = getCursorFromMouse();
        return true;
    }

    ////

    public void addGlyph(Glyph glyph, GlyphStyle style){
        deleteSelection();

        glyph.styleHolder = new GlyphStyleHolder();
        glyph.styleHolder.setFromStyle(style);

        if(!component.canAddGlyph(glyph)){
            return;
        }

        int insert = getSafeGlyphInsertionIndex();
        pages.text.glyphs.add(insert, glyph);
        getGSI().build();
        moveCursorTo(cursor, insert, false);
        component.onTextChanged();
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
        getGSI().build();
        cursor.setIndex(cursor.isLeading() ? delete : delete - 1);
        onCursorMoved();
        component.onTextChanged();
    }

    public boolean deleteSelection(){
        if(selectionEnd == null){
            return false;
        }
        int startIndex = Math.min(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());
        int endIndex = Math.max(cursor.getInsertionIndex(), selectionEnd.getInsertionIndex());

        clearSelection();
        if(pages.text.deleteGlyphs(startIndex, endIndex)){
            getGSI().build();
            selectionEnd = null;
            component.onTextChanged();
            return true;
        }
        return false;
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

    public void enter(){
        addGlyph(GSIDesignSettings.getLineBreakGlyph(false, GSIDesignSettings.lineStyle.copy()), getGlyphStyle());
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
    public CursorPoint getCursorFromMouse(){
        List<StyledTextLine> page = pages.getCurrentPage();

        //if the page is empty we move to the previous one, unless it's the first
        if(page.isEmpty()){
            if(pages.page > 0){
                pages.page--;
                return getCursorFromMouse();
            }
            return new CursorPoint(false, 0);
        }

        //first we find the line the mouses' y hit lines up with
        StyledTextLine line = null;
        for(StyledTextLine l : pages.getCurrentPage()){
            if(l.renderSize.containsY(getInteractionHandler().mousePos.y)){
                line = l;
                break;
            }
        }


        //if we didn't find the line, we will set the cursor to either the first or last line instead
        if(line == null){
            StyledTextLine firstLine = page.get(0);
            if(getInteractionHandler().mousePos.y < firstLine.renderSize.getY()){
                line = firstLine;
            }else{
                line = page.get(page.size() -1 );
            }
        }

        CursorPoint cursor = new CursorPoint(false, 0);
        updateCursorFromXHit(cursor, line, getInteractionHandler().mousePos.x);
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
            int cursorPage = pages.getPageNumber(cursor.getCharIndex());
            if(cursorPage != -1 && cursorPage != pages.page){
                pages.page = cursorPage;
            }
        }
    }

}