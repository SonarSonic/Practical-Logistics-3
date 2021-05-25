package sonar.logistics.client.gsi.interactions.resize;

import sonar.logistics.util.vectors.Quad2D;

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


}