package sonar.logistics.client.design.gui.interactions;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import sonar.logistics.client.design.gui.GSIDesignSettings;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.design.gui.interactions.hotkeys.HotKeyFunctions;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.components.text.StyledTextComponent;
import sonar.logistics.client.gsi.components.text.StyledTextRenderer;
import sonar.logistics.client.gsi.components.text.StyledTextWrapper;
import sonar.logistics.client.gsi.components.text.api.IGlyphRenderer;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.glyph.StyleGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.context.DisplayInteractionContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//TODO CURSOR ON THE START OF A LINE.. FIX ME!
public class ViewportTextInteraction extends ViewportAbstractInteraction implements IGlyphRenderer {

    public DraggedSelection draggedSelection = null;
    public List<Tuple<CursorPoint, CursorPoint>> selections = new ArrayList<>();
    public CursorPoint cursor = new CursorPoint(null, 0);
    public StyledTextComponent textComponent;

    public ViewportTextInteraction(GSIViewportWidget viewport) {
        super(viewport);
    }

    @Override
    public GSIDesignSettings.ViewportInteractSetting getViewportSetting() {
        return GSIDesignSettings.ViewportInteractSetting.EDIT_TEXT;
    }

    @Override
    public void renderScissored(int mouseX, int mouseY, float partialTicks) {
        super.renderScissored(mouseX, mouseY, partialTicks);
        if(draggedSelection != null && draggedSelection.isValid) {
            ScreenUtils.fillDouble(viewport.getRenderOffsetX() + draggedSelection.startX * viewport.scaling, viewport.getRenderOffsetY() + draggedSelection.startY * viewport.scaling, viewport.getRenderOffsetX() + draggedSelection.endX * viewport.scaling, viewport.getRenderOffsetY() + draggedSelection.endY * viewport.scaling, ScreenUtils.transparent_green_button.rgba);
        }

    }

    public boolean isDrawingHighlights = false;

    @Override
    public float renderGlyph(IGlyphType glyph, StyledTextRenderer.GlyphRenderContext context) {
        float renderOffset = glyph.render(context);

        Optional<Tuple<CursorPoint, CursorPoint>> start = selections.stream().filter(a -> a.getA().glyph == glyph).findFirst();
        Optional<Tuple<CursorPoint, CursorPoint>> end = selections.stream().filter(a -> a.getB().glyph == glyph).findFirst();

        if(start.isPresent()){
            isDrawingHighlights = true;
        }

        if(isDrawingHighlights){
            float downscale = glyph.downscale(context.fontType, context.parentStyling);
            StyledTextRenderer.addHighlightToGlyph(context, downscale, renderOffset / downscale);
        }

        if(end.isPresent()){
            isDrawingHighlights = false;
        }

        if(cursor != null && glyph == cursor.glyph){
            if(GSIDesignSettings.canRenderCursor()){
                ///render cursor.
                float downscale = glyph.downscale(context.fontType, context.parentStyling);
                StyledTextRenderer.addCursorToGlyph(context, downscale, renderOffset / downscale);
            }
        }
        return renderOffset;
    }

    @Nullable
    public Tuple<IGlyphType, GlyphStyle> getGlyphAt(double mouseX, double mouseY){
        if(textComponent == null){
            return null;
        }
        DisplayInteractionContext context = new DisplayInteractionContext(viewport.gsi, Minecraft.getInstance().player, true);
        context.setDisplayClick((mouseX - viewport.getRenderOffsetX()) / viewport.scaling, (mouseY - viewport.getRenderOffsetY()) / viewport.scaling);
        context.offsetComponentHit(textComponent.getBounds().renderBounds().getX(), textComponent.getBounds().renderBounds().getY());
        StyledTextWrapper.CachedGlyphLine interactedLine = textComponent.getInteractedLine(context);
        return textComponent.getInteractedGlyph(context, interactedLine);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean dragged = super.mouseClicked(mouseX, mouseY, button);

        if(viewport.isMouseOverViewport(mouseX, mouseY)) {
            if (button == 0) {
                DisplayInteractionContext context = new DisplayInteractionContext(viewport.gsi, Minecraft.getInstance().player, true);
                context.setDisplayClick((mouseX - viewport.getRenderOffsetX()) / viewport.scaling, (mouseY - viewport.getRenderOffsetY()) / viewport.scaling);
                IScaleableComponent selectedComponent = viewport.gsi.getInteractedComponent(context);

                if(selectedComponent instanceof StyledTextComponent){
                    textComponent = (StyledTextComponent) selectedComponent;
                    textComponent.specialGlyphRenderer = this;
                    context.offsetComponentHit(textComponent.getBounds().renderBounds().getX(), textComponent.getBounds().renderBounds().getY());
                    StyledTextWrapper.CachedGlyphLine interactedLine = textComponent.getInteractedLine(context);
                    Tuple<IGlyphType, GlyphStyle> interactedGlyph = textComponent.getInteractedGlyph(context, interactedLine);
                    if(interactedLine != null){
                        cursor = new CursorPoint(interactedGlyph.getA(), 0);
                    }

                }
            }
        }
        return dragged;
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if(textComponent == null){
            return false;
        }
        return HotKeyFunctions.triggerHotKey(this, key, scanCode, modifiers);
    }

    //// DRAGGING METHODS \\\\\

    @Override
    public void onDragStarted(double mouseX, double mouseY, int button) {
        super.onDragStarted(mouseX, mouseY, button);
        if(textComponent != null) {
            draggedSelection = new DraggedSelection((mouseX - viewport.getRenderOffsetX()) / viewport.scaling, (mouseY - viewport.getRenderOffsetY()) / viewport.scaling);
        }
    }

    @Override
    public void onDragFinished(double mouseX, double mouseY, int button) {
        super.onDragFinished(mouseX, mouseY, button);
        if(textComponent != null && draggedSelection != null) {
            draggedSelection.flipNegatives();
            Tuple<CursorPoint, CursorPoint> selection = getSelectionFromDrag(draggedSelection);
            if (selection != null) {
                selections.add(selection);
            }
            draggedSelection = null;
        }
    }

    @Override
    public void onDragged(double mouseX, double mouseY, int button) {
        super.onDragged(mouseX, mouseY, button);
        if(textComponent != null && draggedSelection != null) {
            draggedSelection.setDragEnd((mouseX - viewport.getRenderOffsetX()) / viewport.scaling, (mouseY - viewport.getRenderOffsetY()) / viewport.scaling);
        }
    }


    public Tuple<CursorPoint, CursorPoint> getSelectionFromDrag(DraggedSelection draggedSelection){

        List<StyledTextWrapper.CachedGlyphLine> page = getCurrentPage();
        double offsetX = textComponent.getBounds().renderBounds().getX();
        double offsetY = textComponent.getBounds().renderBounds().getY();

        IGlyphType start = null;
        IGlyphType end = null;

        for(StyledTextWrapper.CachedGlyphLine line : page){

            GlyphStyle style = line.parentStyling;
            float totalWidth = 0;

            for(IGlyphType glyph : line.glyphs){
                if(glyph instanceof StyleGlyph){
                    style = ((StyleGlyph) glyph).alterStyle(style.copy());
                    continue;
                }

                float renderWidth = glyph.isSpace() && line.justifySpaceSize != 0 ? line.justifySpaceSize : glyph.getRenderWidth(textComponent.fontType, style) * line.lineScaling;
                float renderHeight = glyph.isSpace() ? line.lineHeight : glyph.getRenderHeight(textComponent.fontType, style) * line.lineScaling;

                float startX = totalWidth;
                float endX = totalWidth + renderWidth;
                float startY = line.offsetY;
                float endY = line.offsetY + renderHeight;
                totalWidth+=renderWidth;
                if(startX >= draggedSelection.startX - offsetX && endX <= draggedSelection.endX - offsetX && startY >= draggedSelection.startY - offsetY && endY <= draggedSelection.endY - offsetY){
                    start = start == null ? glyph : start;
                    end = glyph;
                }
            }
        }
        if(start != null && end != start){
            return new Tuple<>(new CursorPoint(start, 1), new CursorPoint(end, 1));
        }
        return null;
    }

    ///// HELPER METHODS \\\\\

    public List<StyledTextWrapper.CachedGlyphLine> getCurrentPage(){
        if(textComponent != null &&  textComponent.textWrapper.cachedGlyphPages.size() > textComponent.page){
            return textComponent.textWrapper.cachedGlyphPages.get(textComponent.page);
        }
        return new ArrayList<>();
    }


    ///// CURSOR MOVEMENT \\\\\

    public void moveToNextPage(CursorPoint cursor){
        if(textComponent != null && textComponent.page + 1 < textComponent.pageCount){
            textComponent.page ++;
            List<StyledTextWrapper.CachedGlyphLine> lines = getCurrentPage();
            StyledTextWrapper.CachedGlyphLine line = lines.get(0);
            cursor.glyph = line.glyphs.get(0);
        }
    }

    public void moveToPrevPage(CursorPoint cursor){
        if(textComponent != null && textComponent.page - 1 >= 0){
            textComponent.page --;
            List<StyledTextWrapper.CachedGlyphLine> lines = getCurrentPage();
            StyledTextWrapper.CachedGlyphLine line = lines.get(lines.size()-1);
            cursor.glyph = line.glyphs.get(line.glyphs.size()-1);
        }
    }

    public void moveCursorVertically(CursorPoint cursor, int move){
        List<StyledTextWrapper.CachedGlyphLine> lines = getCurrentPage();
        StyledTextWrapper.CachedGlyphLine line = cursor.getCursorCachedLine(getCurrentPage());
        if(line == null){
            return;
        }
        int newIndex = lines.indexOf(line) + move;
        if(newIndex < 0 ){
            moveToPrevPage(cursor);
            return;
        }
        if(newIndex >= lines.size()){
            moveToNextPage(cursor);
            return;
        }
        int glyphIndex = line.glyphs.indexOf(cursor.glyph);
        line = lines.get(newIndex);
        cursor.glyph = line.glyphs.get(Math.min(line.glyphs.size()-1, glyphIndex));
    }

    public void moveCursorHorizontally(CursorPoint cursor, int move){
        StyledTextWrapper.CachedGlyphLine line = cursor.getCursorCachedLine(getCurrentPage());
        if(line != null) {
            int newIndex = line.glyphs.indexOf(cursor.glyph) + move;
            if(newIndex < 0){
                moveCursorVertically(cursor, -1);
                moveCursorToEnd(cursor);
                return;
            }
            if(newIndex >= line.glyphs.size()){
                moveCursorVertically(cursor, 1);
                moveCursorToStart(cursor);
                return;
            }
            cursor.glyph = line.glyphs.get(newIndex);
        }
    }


    public void moveCursorToStart(CursorPoint cursor){
        StyledTextWrapper.CachedGlyphLine line = cursor.getCursorCachedLine(getCurrentPage());
        if(line != null) {
            cursor.glyph = line.glyphs.get(0);
        }
    }


    public void moveCursorToEnd(CursorPoint cursor){
        StyledTextWrapper.CachedGlyphLine line = cursor.getCursorCachedLine(getCurrentPage());
        if(line != null) {
            cursor.glyph = line.glyphs.get(line.glyphs.size() - 1);
        }
    }

    ///// CURSOR CLASS \\\\\

    public static class CursorPoint {

        @Nullable
        IGlyphType glyph;
        int anchorType; //0 normal cursor - 1 = selection start, 2 = selection end
        boolean isLeading; //isLeading = before the glyph otherwise isTrailing = after the glyph

        public CursorPoint(IGlyphType glyph, int anchorType){
            this.glyph = glyph;
            this.anchorType = anchorType;
        }

        public StyledTextWrapper.CachedGlyphLine getCursorCachedLine(List<StyledTextWrapper.CachedGlyphLine> page){
            if(glyph == null){
                return null;
            }
            for (StyledTextWrapper.CachedGlyphLine line : page) {
                if(line.glyphs.contains(glyph)){
                    return line;
                }
            }
            return null;
        }

    }

    ///// DRAGGING CLASS \\\\\

    public static class DraggedSelection {

        double startX, startY;
        double endX, endY;
        boolean isValid = false;

        public DraggedSelection(double startX, double startY){
            this.startX = startX;
            this.startY = startY;
            this.endX = -1;
            this.endY = -1;
        }

        public void setDragEnd(double endX, double endY){
            this.endX = endX;
            this.endY = endY;
            this.isValid = true;
        }

        public void flipNegatives(){
            double value = 0;
            if(startX > endX){
                value = startX;
                startX = endX;
                endX = value;
            }
            if(startY > endY){
                value = startY;
                startY = endY;
                endY = value;
            }
        }

    }

}
