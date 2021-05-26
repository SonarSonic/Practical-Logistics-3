package sonar.logistics.client.gsi.interactions.resize;

import com.mojang.blaze3d.systems.RenderSystem;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.interactions.AbstractComponentInteraction;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.Unit;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.util.vectors.Quad2F;
import sonar.logistics.util.vectors.Vector2F;

import javax.annotation.Nullable;

public class ResizeInteraction extends AbstractComponentInteraction<Component>{

    private static ColourProperty normal = new ColourProperty(120, 120, 255, 255);
    private static ColourProperty highlighted = new ColourProperty(255, 255, 255, 255);
    private Vector2F dragStart;

    private EnumRescaleType currentRescaleType = null;
    private float clickBoxSize = 0.0625F/2;
    private Quad2F[] moveBoxes = new Quad2F[0];

    public ResizeInteraction(Component component) {
        super(component);
    }

    @Override
    public void renderInteraction(GSIRenderContext context) {
        super.renderInteraction(context);
        RenderSystem.disableDepthTest();
        clickBoxSize = 16 / context.gsiScaling;
        Quad2F rescaledBounds = getRescaledBounds();
        if(rescaledBounds == null){
            return; //TODO hmmm
        }

        //render the resize boxes
        EnumRescaleType currentType = currentRescaleType != null ? currentRescaleType : getRescaleTypeFromMouse(component, getInteractionHandler());


        for(Quad2F quad2D : getMoveBoxes(rescaledBounds, 4 / context.gsiScaling)){
            GSIRenderHelper.renderColouredRect(context, false, quad2D, new ColourProperty(255, 255, 255, 50));
        }

        for (EnumRescaleType type : EnumRescaleType.values()) { //render selection box
            boolean isHighlighted = currentType == type;
            if (type != EnumRescaleType.MOVE) {
                Quad2F clickBox = getClickBox(type, rescaledBounds, 12 / context.gsiScaling);
                GSIRenderHelper.renderColouredRect(context, false, clickBox, isHighlighted ? normal : highlighted);
            }
        }

        //render the new size
        /*
        if (currentRescaleType != null && dragStart != null && !dragStart.equals(getMousePos())) {
            GSIRenderHelper.renderColouredRect(context, false, rescaledBounds, ScreenUtils.transparent_green_button);
        }
         */

        RenderSystem.enableDepthTest();
    }

    @Override
    public boolean canStartDrag(int button) {
        return getRescaleTypeFromMouse(component, getInteractionHandler()) != null && isDragButton(button);
    }

    @Override
    public void onDragStarted(int button) {
        super.onDragStarted(button);
        currentRescaleType = getRescaleTypeFromMouse(component, getInteractionHandler());
        dragStart = getInteractionHandler().mousePos.copy();
    }

    @Override
    public void onDragFinished(int button) {
        super.onDragFinished(button);
        Quad2F gsiBounds = getGSI().getGSIBounds();
        Quad2F componentBounds = getRescaledBounds();
        float x = (componentBounds.x - gsiBounds.x) / gsiBounds.width;
        float y = (componentBounds.y - gsiBounds.y) / gsiBounds.height;
        float width = componentBounds.width / gsiBounds.width;
        float height = componentBounds.height / gsiBounds.height;
        component.getStyling().setSizing(x, y, width, height, Unit.PERCENT);
        component.rebuild();
        currentRescaleType = null;
        dragStart = null;
    }

    @Override
    public boolean isMouseOver() {
        return getRescaleTypeFromMouse(component, getInteractionHandler()) != null;
    }

    public Quad2F getRescaledBounds(){
        return currentRescaleType == null ? component.getBounds().outerSize() : rescaleWindow(currentRescaleType, component.getBounds().outerSize(), getGSI().getGSIBounds(), GSIDesignSettings.snapToNormalGrid(getMousePos().x - dragStart.x), GSIDesignSettings.snapToNormalGrid(getMousePos().y - dragStart.y), getInteractionHandler().hasShiftDown());
    }

    //// MOVING / RESIZING METHODS \\\\

    public static Quad2F moveWindow(Quad2F window, Quad2F bounds, double dragX, double dragY, boolean isShifting){
        if(isShifting){
            if(Math.abs(dragX) >= Math.abs(dragY)){ // snap to axis with biggest drag
                dragY = 0;
            }else{
                dragX = 0;
            }
        }
        double newX = window.x + dragX, newY = window.y + dragY;

        if(newX < bounds.x){ // fit move within x bounds
            newX = bounds.x;
        }else if(newX + window.width > bounds.x + bounds.width){
            newX = bounds.x + bounds.width - window.width;
        }

        if(newY < bounds.y){ // fit move within y bounds
            newY = bounds.y;
        }else if(newY + window.height> bounds.y + bounds.height){
            newY = bounds.y + bounds.height - window.height;
        }

        return new Quad2F(newX, newY, window.width, window.height);
    }


    public static Quad2F rescaleWindow(EnumRescaleType type, Quad2F window, Quad2F bounds, double dragX, double dragY, boolean isShifting){
        if(type == EnumRescaleType.MOVE){ // moving this method is for neatness but may also be useful later
            return moveWindow(window, bounds, dragX, dragY, isShifting);
        }

        double newX = window.x, newY = window.y, newW = window.width, newH = window.height;

        double pivotX = type.oppositeX() * window.width; //the position of the opposite pivoting corner relative to window.x
        double pivotY = type.oppositeY() * window.height; //the position of the opposite pivoting corner relative to window.y

        double draggedX = type.xPos * window.width + dragX; //the position of the mouse relative to the window.x
        double draggedY = type.yPos * window.height + dragY; //the position of the mouse relative to the window.y

        if(window.x + draggedX < bounds.x){ //fit dragged x within bounds
            draggedX = bounds.x - window.x;
        }else if(window.x + draggedX > bounds.x + bounds.width){
            draggedX = bounds.x + bounds.width - window.x;
        }

        if(window.y + draggedY < bounds.y){ //fit dragged y within bounds
            draggedY = bounds.y - window.y;
        }else if(window.y + draggedY > bounds.y + bounds.height){
            draggedY = bounds.y + bounds.height - window.y;
        }

        if(isShifting && type.xPos != 0.5 && type.yPos != 0.5){ // uniform rescaling, only used on corners
            double multiplier = Math.min(Math.abs(draggedX - pivotX) / window.width, Math.abs(draggedY - pivotY) / window.height);
            draggedX = pivotX + (draggedX > pivotX ? window.width * multiplier : window.width * -multiplier);
            draggedY = pivotY + (draggedY > pivotY ? window.height * multiplier : window.height * -multiplier);
        }

        if(type.xPos != 0.5) { //middle rescale boxes only change one axis
            newX += pivotX;
            newW = draggedX - pivotX;
        }

        if(type.yPos != 0.5) { //middle rescale boxes only change one axis
            newY += pivotY;
            newH = draggedY - pivotY;
        }

        if(newW < 0){ // flip negative width
            newX += newW;
            newW = -newW;
        }

        if(newH < 0){ // flip negative height
            newY += newH;
            newH = -newH;
        }

        return new Quad2F(newX, newY, newW, newH);
    }

    //// RESCALE TYPE BOXES \\\\

    public EnumRescaleType getRescaleTypeFromMouse(Component component, GSIInteractionHandler handler) {
        return getRescaleTypeFromMouse(component.getBounds().outerSize(), handler.mousePos.x, handler.mousePos.y, clickBoxSize);
    }

    @Nullable
    public EnumRescaleType getRescaleTypeFromMouse(Quad2F window, float mouseX, float mouseY, float boxSize){
        for(EnumRescaleType box : EnumRescaleType.values()){
            if(isMouseOver(box, window, mouseX, mouseY, boxSize)){
                return box;
            }
        }
        return null;
    }

    public boolean isMouseOver(EnumRescaleType type, Quad2F window, float mouseX, float mouseY, float boxSize){
        if(type == EnumRescaleType.MOVE){
            for(Quad2F quad2D : getMoveBoxes(window, boxSize)){
                if(quad2D.inside(mouseX, mouseY)){
                    return true;
                }
            }
        }
        return getClickBox(type, window, boxSize).inside(mouseX, mouseY);
    }

    public Quad2F[] getMoveBoxes(Quad2F window, float boxSize){
        return new Quad2F[]{
                new Quad2F(window.getX(), window.getY() - boxSize/2F, window.getWidth(), boxSize),
                new Quad2F(window.getX(), window.getMaxY() - boxSize/2F, window.getWidth(), boxSize),
                new Quad2F(window.getX() - boxSize/2F, window.getY(), boxSize, window.getHeight()),
                new Quad2F(window.getMaxX() - boxSize/2F, window.getY(), boxSize, window.getHeight())
        };
    }

    public Quad2F getClickBox(EnumRescaleType type, Quad2F window, float boxSize){
        return new Quad2F(window.x + type.xPos * window.width - boxSize/2F, window.y + type.yPos * window.height - boxSize/2F, boxSize, boxSize);
    }

}

