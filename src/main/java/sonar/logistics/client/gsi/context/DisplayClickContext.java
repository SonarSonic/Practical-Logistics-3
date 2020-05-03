package sonar.logistics.client.gsi.context;

import net.minecraft.entity.player.PlayerEntity;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.BlockInteractionType;

public class DisplayClickContext extends DisplayInteractionContext {

    public BlockInteractionType type;
    public byte clickCounter = 1;

    public DisplayClickContext(BlockInteractionType type, GSI gsi, PlayerEntity player, boolean isUsingGui){
        super(gsi, player, isUsingGui);
        this.gsi = gsi;
        this.player = player;
        this.type = type;
        this.isUsingGui = isUsingGui;
    }

    public void setClickCounter(byte count){
        this.clickCounter = count;
    }

    @Override
    public boolean isHover(){
        return false;
    }

    @Override
    public boolean isClick(){
        return true;
    }

}
