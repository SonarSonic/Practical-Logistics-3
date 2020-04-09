package sonar.logistics.multiparts.displays.api;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;

public interface IDisplay {

    Direction getFacing();

    BlockPos getPos();

    /**returns the screens scaling vector in the form WIDTH / HEIGHT / DEPTH
     * this must be implemented on every screen*/
    Vec3d getScreenSizing();

    /**returns the screens rotational vector in the form PITCH / YAW / ROLL
     * by default this is calculated using the screens {@link #getFacing()}*/
    default Vec3d getScreenRotation(){
        return DisplayVectorHelper.getScreenRotation(getFacing());
    }

    /**the screens origin ( the center of the screen / it's point of rotation )
     * by default this uses {@link #getFacing()} to offset the display*/
    default Vec3d getScreenOrigin(){
        return getRenderOrigin().add(DisplayVectorHelper.convertVector(getPos()));
    }

    /**gets the render origin*/
    default Vec3d getRenderOrigin(){
        Vec3d origin = new Vec3d(0.5, 0.5, 0.5); // place vector in the centre of the block pos
        origin = origin.add(DisplayVectorHelper.getFaceOffset(getFacing(), 0.5)); // offset by the direction of the screen
        return origin;
    }

    /**gets the transform required to start the rendering at the top left of the screen*/
    default Vec3d getRenderOffset(){
        Vec3d size = getScreenSizing();
        return new Vec3d(-size.x/2, -size.y/2, 0);
    }

    default void onGSIInvalidate(){}

    default void onGSIValidate(){}
}
