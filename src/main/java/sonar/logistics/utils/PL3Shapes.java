package sonar.logistics.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class PL3Shapes {

    //// DATA CABLE
    public static final VoxelShape DATA_CABLE_CENTRE_VOXEL = VoxelShapes.create(getBBFromPixels(6, 6, 6, 10, 10, 10));

    public static final AxisAlignedBB DATA_CABLE_CONNECTOR_BB_SHAPE = getBBFromPixels(7, 0, 7, 9, 6, 9);
    public static final VoxelShape[] DATA_CABLE_CONNECTOR_ROTATED_VOXELS = getRotatedVoxels(DATA_CABLE_CONNECTOR_BB_SHAPE, Direction.values());

    //// MINI DISPLAY SCREEN
    public static final AxisAlignedBB MINI_DISPLAY_BB_SHAPE = getBBFromPixels(4, 0, 4, 12, 1, 12);
    public static final VoxelShape[] MINI_DISPLAY_ROTATED_VOXELS = getRotatedVoxels(MINI_DISPLAY_BB_SHAPE, Direction.values());
    public static final Vec3d MINI_DISPLAY_SCREEN_SCALING = getVec3DFromPixels(6, 6,  0);

    //// DISPLAY SCREEN
    public static final AxisAlignedBB DISPLAY_BB_SHAPE = getBBFromPixels(0, 0, 4, 16, 1, 12);
    public static final VoxelShape[] DISPLAY_ROTATED_VOXELS = getRotatedVoxels(DISPLAY_BB_SHAPE, Direction.values());
    public static final Vec3d DISPLAY_SCREEN_SCALING = getVec3DFromPixels(14, 6,  0);

    //// LARGE DISPLAY SCREEN
    public static final AxisAlignedBB LARGE_DISPLAY_BB_SHAPE = getBBFromPixels(0, 0, 0, 16, 1, 16);
    public static final VoxelShape[] LARGE_DISPLAY_ROTATED_VOXELS = getRotatedVoxels(LARGE_DISPLAY_BB_SHAPE, Direction.values());
    public static final Vec3d LARGE_DISPLAY_SCREEN_SCALING = getVec3DFromPixels(14, 14,  0);


    public static final double PIXEL = 0.0625;

    public static Vec3d getVec3DFromPixels(double x, double y, double z){
        return new Vec3d(x * PIXEL, y * PIXEL, z * PIXEL);
    }

    public static AxisAlignedBB getBBFromPixels(int minX, int minY, int minZ, int maxX, int maxY, int maxZ){
        return new AxisAlignedBB(minX*PIXEL, minY*PIXEL, minZ*PIXEL, maxX*PIXEL, maxY*PIXEL, maxZ*PIXEL);
    }

    public static VoxelShape[] getRotatedVoxels(AxisAlignedBB downShape, Direction[] dirs){
        VoxelShape[] rotated = new VoxelShape[dirs.length];
        for(int i = 0; i < dirs.length; i++){
            rotated[i] = VoxelShapes.create(rotate(downShape, dirs[i]));
        }
        return rotated;
    }

    public static AxisAlignedBB rotate(AxisAlignedBB shape, Direction dir) {
        return rotateToDirection(shape.offset(-0.5, -0.5, -0.5), dir).offset(0.5, 0.5, 0.5);
    }

    private static AxisAlignedBB rotateToDirection(AxisAlignedBB boundingBox, Direction side) {
        switch (side) {
            case DOWN:
                return boundingBox;
            case UP:
                return new AxisAlignedBB(boundingBox.minX, -boundingBox.minY, -boundingBox.minZ, boundingBox.maxX, -boundingBox.maxY, -boundingBox.maxZ);
            case NORTH:
                return new AxisAlignedBB(boundingBox.minX, -boundingBox.minZ, boundingBox.minY, boundingBox.maxX, -boundingBox.maxZ, boundingBox.maxY);
            case SOUTH:
                return new AxisAlignedBB(-boundingBox.minX, boundingBox.minZ, -boundingBox.minY, -boundingBox.maxX, boundingBox.maxZ, -boundingBox.maxY);
            case WEST:
                return new AxisAlignedBB(boundingBox.minY, -boundingBox.minZ, -boundingBox.minX, boundingBox.maxY, -boundingBox.maxZ, -boundingBox.maxX);
            case EAST:
                return new AxisAlignedBB(-boundingBox.minY, boundingBox.minZ, boundingBox.minX, -boundingBox.maxY, boundingBox.maxZ, boundingBox.maxX);
        }
        return boundingBox;
    }

    public static AxisAlignedBB rotate(AxisAlignedBB box, Rotation rotation) {
        switch (rotation) {
            case NONE:
                return box;
            case CLOCKWISE_90:
                return new AxisAlignedBB(-box.minZ, box.minY, box.minX, -box.maxZ, box.maxY, box.maxX);
            case CLOCKWISE_180:
                return new AxisAlignedBB(-box.minX, box.minY, -box.minZ, -box.maxX, box.maxY, -box.maxZ);
            case COUNTERCLOCKWISE_90:
                return new AxisAlignedBB(box.minZ, box.minY, -box.minX, box.maxZ, box.maxY, -box.maxX);
        }
        return box;
    }




}
