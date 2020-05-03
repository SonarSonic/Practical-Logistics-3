package sonar.logistics.client.gsi.context;

import net.minecraft.entity.player.PlayerEntity;
import sonar.logistics.client.gsi.GSI;

public class DisplayInteractionContext {

    public GSI gsi;
    public PlayerEntity player;
    public boolean isUsingGui;

    public float intersectX, intersectY;
    public float offsetX, offsetY;

    public DisplayInteractionContext(GSI gsi, PlayerEntity player, boolean isUsingGui){
        this.gsi = gsi;
        this.player = player;
        this.isUsingGui = isUsingGui;
    }

    public void setIntersect(float intersectX, float intersectY){
        this.intersectX = intersectX;
        this.intersectY = intersectY;
    }

    public void setOffset(float offsetX, float offsetY){
        this.offsetX = intersectX - offsetX;
        this.offsetY = intersectY - offsetY;
    }

    public boolean isHover(){
        return true;
    }

    public boolean isClick(){
        return false;
    }

}
