package sonar.logistics.client.gsi.context;

import net.minecraft.entity.player.PlayerEntity;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IInteractionListener;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.vectors.Vector2D;

import javax.annotation.Nullable;

public class DisplayInteractionHandler {

    public GSI gsi;
    public PlayerEntity player;
    public boolean isUsingGui;

    public Vector2D mousePos;
    public IScaleableComponent hovered;

    public DisplayInteractionHandler(GSI gsi, PlayerEntity player, boolean isUsingGui){
        this.gsi = gsi;
        this.player = player;
        this.isUsingGui = isUsingGui;
    }

    public void update(@Nullable  Vector2D mousePos){ //click position relative to the display
        if(mousePos == null || !mousePos.equals(this.mousePos)) {
            this.mousePos = mousePos;
            this.hovered = mousePos == null ? null : gsi.getInteractedComponent(this);
        }
    }

    public boolean hasShiftDown(){
        return player.isSneaking();
    }

    public boolean hasControlDown() {
        return false;
    }

    public boolean hasAltDown(){
        return false;
    }

    public boolean toggle(IInteractionListener listener, int triggerId){
        return gsi.toggle(listener, triggerId);
    }

    public boolean isActive(IInteractionListener listener, int triggerId){
        return gsi.isActive(listener, triggerId);
    }

}
