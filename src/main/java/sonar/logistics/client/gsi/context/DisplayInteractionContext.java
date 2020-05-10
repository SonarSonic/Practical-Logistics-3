package sonar.logistics.client.gsi.context;

import net.minecraft.entity.player.PlayerEntity;
import sonar.logistics.client.gsi.GSI;

public class DisplayInteractionContext {

    public GSI gsi;
    public PlayerEntity player;
    public boolean isUsingGui;

    public double displayX, displayY;
    public double componentX, componentY;

    public DisplayInteractionContext(GSI gsi, PlayerEntity player, boolean isUsingGui){
        this.gsi = gsi;
        this.player = player;
        this.isUsingGui = isUsingGui;
    }

    public void setDisplayClick(double intersectX, double intersectY){ //click position relative to the display
        this.displayX = intersectX;
        this.displayY = intersectY;
    }

    public void setComponentClick(double offsetX, double offsetY){ //click position relative to the component
        this.componentX = displayX - offsetX;
        this.componentY = displayY - offsetY;
    }

    public boolean isHover(){
        return true;
    }

    public boolean isClick(){
        return false;
    }

}
