package sonar.logistics.multiparts.utils;

import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum EnumMultipartSlot {
    CENTRE(null),
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    Direction direction;

    EnumMultipartSlot(@Nullable Direction direction){
        this.direction = direction;
    }

    public static EnumMultipartSlot fromDirection(@Nonnull Direction direction){
        for(EnumMultipartSlot slot : values()){
            if(slot.direction == direction){
                return slot;
            }
        }
        return CENTRE;
    }

    @Nullable
    public Direction getDirection(){
        if(direction == null){
            return null;
        }
        return direction;
    }

}
