package sonar.logistics.util;

import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public class PL3Properties {

    public static final DirectionProperty ORIENTATION = DirectionProperty.create("facing", Direction.values());
    public static final DirectionProperty ROTATION = DirectionProperty.create("rotation", Direction.values());

    public static AxisAlignedBB getStandardBox(Direction dir, double width, double heightMin, double heightMax) {
        double w = (1 - width) / 2;
        switch (dir) {
            case DOWN:
                return new AxisAlignedBB(w, heightMin, w, 1 - w, heightMax, 1 - w);
            case EAST:
                return new AxisAlignedBB(1 - heightMax, w, w, 1 - heightMin, 1 - w, 1 - w);
            case NORTH:
                return new AxisAlignedBB(w, w, heightMin, 1 - w, 1 - w, heightMax);
            case SOUTH:
                return new AxisAlignedBB(w, w, 1 - heightMax, 1 - w, 1 - w, 1 - heightMin);
            case UP:
                return new AxisAlignedBB(w, 1 - heightMax, w, 1 - w, 1 - heightMin, 1 - w);
            case WEST:
                return new AxisAlignedBB(heightMin, w, w, heightMax, 1 - w, 1 - w);
            default:
                return new AxisAlignedBB(w, heightMin, w, 1 - w, heightMax, 1 - w);
        }
    }

}
