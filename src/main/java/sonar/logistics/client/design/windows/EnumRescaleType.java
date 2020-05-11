package sonar.logistics.client.design.windows;

import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;

public enum EnumRescaleType {
    TOP_LEFT_RESCALE(0.0F, 0.0F),
    TOP_MIDDLE_RESCALE(0.5F, 0.0F),
    TOP_RIGHT_RESCALE(1.0F, 0.0F),
    MIDDLE_LEFT_RESCALE(0.0F, 0.5F),
    MIDDLE_RIGHT_RESCALE(1.0F, 0.5F),
    BOTTOM_LEFT_RESCALE(0.0F, 1.0F),
    BOTTOM_MIDDLE_RESCALE(0.5F, 1.0F),
    BOTTOM_RIGHT_RESCALE(1.0F, 1.0F),
    MOVE(-1, -1);

    public float xPos, yPos; // position on each axis from 0% to 100%

    EnumRescaleType(float xPos, float yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public double oppositeX(){
        return xPos == 1.0F ? 0.0F : xPos == 0.0F ? 1.0F : 0.5F;
    }

    public double oppositeY(){
        return yPos == 1.0F ? 0.0F : yPos == 0.0F ? 1.0F : 0.5F;
    }

    public Quad2D moveWindow(Quad2D window, Quad2D bounds, double dragX, double dragY, boolean isShifting){
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

        return new Quad2D(newX, newY, window.width, window.height);
    }


    public Quad2D rescaleWindow(Quad2D window, Quad2D bounds, double dragX, double dragY, boolean isShifting){
        if(this == MOVE){ // moving this method is for neatness but may also be useful later
            return moveWindow(window, bounds, dragX, dragY, isShifting);
        }

        double newX = window.x, newY = window.y, newW = window.width, newH = window.height;

        double pivotX = oppositeX() * window.width; //the position of the opposite pivoting corner relative to window.x
        double pivotY = oppositeY() * window.height; //the position of the opposite pivoting corner relative to window.y

        double draggedX = xPos * window.width + dragX; //the position of the mouse relative to the window.x
        double draggedY = yPos * window.height + dragY; //the position of the mouse relative to the window.y

        if(window.x + draggedX < bounds.x){ //fit draged x within bounds
            draggedX = bounds.x - window.x;
        }else if(window.x + draggedX > bounds.x + bounds.width){
            draggedX = bounds.x + bounds.width - window.x;
        }

        if(window.y + draggedY < bounds.y){ //fit draged y within bounds
            draggedY = bounds.y - window.y;
        }else if(window.y + draggedY > bounds.y + bounds.height){
            draggedY = bounds.y + bounds.height - window.y;
        }

        if(isShifting && xPos != 0.5 && yPos != 0.5){ // uniform rescaling, only used on corners
            double multiplier = Math.min(Math.abs(draggedX - pivotX) / window.width, Math.abs(draggedY - pivotY) / window.height);
            draggedX = pivotX + (draggedX > pivotX ? window.width * multiplier : window.width * -multiplier);
            draggedY = pivotY + (draggedY > pivotY ? window.height * multiplier : window.height * -multiplier);
        }

        if(xPos != 0.5) { //middle rescale boxes only change one axis
            newX += pivotX;
            newW = draggedX - pivotX;
        }

        if(yPos != 0.5) { //middle rescale boxes only change one axis
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

        return new Quad2D(newX, newY, newW, newH);
    }

    public double[] getClickBox(Quad2D window, float boxSize){
        if(this == MOVE){
            return new double[]{window.x, window.y, window.x + window.width, window.y + window.height};
        }
        return new double[]{window.x + xPos * window.width - boxSize/2F, window.y + yPos * window.height - boxSize/2F, window.x + xPos * window.width + boxSize/2F, window.y + yPos * window.height + boxSize/2F};
    }

    public boolean isMouseOver(Quad2D window, double mouseX, double mouseY, float boxSize){
        double[] boxPos = getClickBox(window, boxSize);
        return mouseX >= boxPos[0] && mouseY >= boxPos[1] && mouseX < boxPos[2] && mouseY < boxPos[3];
    }

    @Nullable
    public static EnumRescaleType getRescaleTypeFromMouse(Quad2D window, double mouseX, double mouseY, float boxSize){
        for(EnumRescaleType box : values()){
            if(box.isMouseOver(window, mouseX, mouseY, boxSize)){
                return box;
            }
        }
        return null;
    }

}