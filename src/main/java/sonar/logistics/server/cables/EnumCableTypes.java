package sonar.logistics.server.cables;

import net.minecraft.util.Direction;
import sonar.logistics.server.caches.displays.ConnectedDisplayManager;
import sonar.logistics.server.caches.network.PL3NetworkManager;

import javax.annotation.Nonnull;

public enum EnumCableTypes {

    NETWORK_CABLE(0, null),
    LARGE_DISPLAY_SCREEN_DOWN(1, Direction.DOWN),
    LARGE_DISPLAY_SCREEN_UP(1, Direction.UP),
    LARGE_DISPLAY_SCREEN_NORTH(1, Direction.NORTH),
    LARGE_DISPLAY_SCREEN_SOUTH(1, Direction.SOUTH),
    LARGE_DISPLAY_SCREEN_WEST(1, Direction.WEST),
    LARGE_DISPLAY_SCREEN_EAST(1, Direction.EAST);

    public int subType;
    public Direction dir;

    EnumCableTypes(int subType, Direction dir){
        this.subType = subType;
        this.dir = dir;
    }

    public static EnumCableTypes getLargeDisplayType(Direction dir){
        for(EnumCableTypes type : values()){
            if(type.subType == 1 && type.dir == dir){
                return type;
            }
        }
        return null;
    }

    @Nonnull
    public ICableHelper getCableHelper(){
        switch (subType){
            case 0:
                return PL3NetworkManager.INSTANCE;
            case 1:
                return ConnectedDisplayManager.INSTANCE;
        }
        return null;
    }

    @Nonnull
    public ICableListener getCableListener(){
        switch (subType){
            case 0:
                return PL3NetworkManager.INSTANCE;
            case 1:
                return ConnectedDisplayManager.INSTANCE;
        }
        return null;
    }

}
