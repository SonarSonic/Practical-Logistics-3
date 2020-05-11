package sonar.logistics.client.gsi.context;

import net.minecraft.entity.player.PlayerEntity;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

public class DisplayInteractionContext {

    public GSI gsi;
    public PlayerEntity player;
    public boolean isUsingGui;

    public Vector2D displayHit;
    public Vector2D componentHit;

    public DisplayInteractionContext(GSI gsi, PlayerEntity player, boolean isUsingGui){
        this.gsi = gsi;
        this.player = player;
        this.isUsingGui = isUsingGui;
    }

    public void setDisplayClick(double x, double y){
        setDisplayClick(new Vector2D(x, y));
    }

    public void setDisplayClick(Vector2D displayHit){ //click position relative to the display
        this.displayHit = displayHit;
    }

    public void offsetComponentHit(double offsetX, double offsetY){ //click position relative to the component
        this.componentHit = displayHit.copy().sub(offsetX, offsetY);
    }

    public void offsetComponentHit(Quad2D bounds){ //click position relative to the component
        this.componentHit = displayHit.copy().sub(bounds.getX(), bounds.getY());
    }

    public boolean isHover(){
        return true;
    }

    public boolean isClick(){
        return false;
    }

}
