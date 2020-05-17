package sonar.logistics.client.vectors;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import sonar.logistics.common.multiparts.displays.api.IDisplay;

import javax.annotation.Nullable;
import java.util.function.Function;

/** VECTOR FORMS
 * ROTATION VECTOR = PITCH = X, YAW = Y, ROLL = Z
 * SIZING VECTOR = WIDTH = X, HEIGHT = Y, DEPTH = Z
 * TRANSLATION VECTOR = X-Translate = X, Y-Translate = Z, X-Translate = Z
 * SCALING VECTOR = X-Scale = X, Y-Scale = Z, X-Scale = Z */
//TODO SWITCH TO MUTABLE VECTORS?
public class VectorHelper {

    public static final Vec3d X_VEC = new Vec3d(1, 0, 0);
    public static final Vec3d Y_VEC = new Vec3d(0, 1, 0);
    public static final Vec3d Z_VEC = new Vec3d(0, 0, 1);

    /**converts degrees to radians.
     * @param degrees value in degrees
     * @return value in radians */
    public static double toRadians(double degrees){
        return degrees * (Math.PI/180D);
    }

    /**converts radians to degrees.
     * @param radians value in radians
     * @return value in degrees */
    public static double toDegrees(double radians){
        return radians / (Math.PI/180D);
    }

    /** converts a integer vector to a double vector
     * @param vec vector in integers
     * @return vector in doubles */
    public static Vec3d convertVector(Vec3i vec){
        return new Vec3d(vec.getX(), vec.getY(), vec.getZ());
    }

    /** converts a integer vector to a double vector
     * @param vec vector in integers
     * @return vector in doubles */
    public static Vec3i convertVector(Vec3d vec){
        return new Vec3i(Math.floor(vec.x), Math.floor(vec.y), Math.floor(vec.z));
    }

    /** reads a {@link Vec3d} from a {@link CompoundNBT}
     * @param tagName tag name to read the vector from
     * @param nbt the tag to read the vector from
     * @return the vector from the tag */
    public static Vec3d readVec3d(CompoundNBT nbt, String tagName) {
        CompoundNBT vecTag = nbt.getCompound(tagName);
        return new Vec3d(vecTag.getDouble("x"), vecTag.getDouble("y"), vecTag.getDouble("z"));
    }

    /** writes a {@link Vec3d} to an {@link CompoundNBT}
     * @param tagName tag to save the vector under
     * @param nbt the tag to save the vector tag to
     * @return the given tag with the vector saved to it */
    public static CompoundNBT writeVec3d(CompoundNBT nbt, String tagName, Vec3d vec){
        CompoundNBT vecTag = new CompoundNBT();
        vecTag.putDouble("x", vec.x);
        vecTag.putDouble("y", vec.y);
        vecTag.putDouble("z", vec.z);
        nbt.put(tagName, vecTag);
        return nbt;
    }

    static Vec3d NULL_VECTOR = new Vec3d(0,0,0);

    public static Vec3d nullVector(){
        return NULL_VECTOR;
    }

    public static Vec3d combineVectors(Vec3d ...vec3ds){
        double x = 0, y = 0, z = 0;
        for(int i = 0; i < vec3ds.length; i++){
            x += vec3ds[i].x;
            y += vec3ds[i].y;
            z += vec3ds[i].z;
        }
        return new Vec3d(x, y, z);
    }
    /*
    /**@param scale the scale of the original vector
     * @param max the maximum size of the vector
     * @return a new vector of the percentages of fill
    public static Vec3d percentageFromScale(Vec3d scale, Vec3d max){
        double percX = (Math.min(scale.x, max.x) / Math.max(scale.x, max.x)) * 100D;
        double percY = (Math.min(scale.y, max.y) / Math.max(scale.y, max.y)) * 100D;
        double percZ = (Math.min(scale.z, max.z) / Math.max(scale.z, max.z)) * 100D;
        return new Vec3d(percX, percY, percZ);
    }

    /** @param percentage the percentage of the max vector
     * @param max the maximum size of the vector
     * @return a new vector with the exact values of fill
    public static Vec3d scaleFromPercentage(Vec3d percentage, Vec3d max) {
        double scaleX = (max.x / 100D) * percentage.x;
        double scaleY = (max.y / 100D) * percentage.y;
        double scaleZ = (max.z / 100D) * percentage.z;
        return new Vec3d(scaleX, scaleY, scaleZ);
    }
    */

    /** scales the unscaled width and height to fit the given scaling */
    @Deprecated
    public static Vec3d scaleFromUnscaledSize(Vec3d unscaled, Vec3d scaling, double percentageFill) { //FIXME returns scaling in Z, check this out.
        double actualElementScale = Math.min(scaling.x / unscaled.x, scaling.y / unscaled.y);
        double actualElementWidth = (unscaled.x * actualElementScale) * percentageFill;
        double actualElementHeight = (unscaled.y * actualElementScale) * percentageFill;
        return new Vec3d ( actualElementWidth, actualElementHeight, actualElementScale );
    }

    /** scales the unscaled width and height to fit the given scaling, maintaining uniform scaling */
    @Deprecated
    public static Vec3d scaleUnscaledSizeToFit(Vec3d unscaled, Vec3d scaling, double percentageFill) { //FIXME returns scaling in Z, check this out.
        double actualElementScale = Math.min(scaling.x / unscaled.x, scaling.y / unscaled.y) * percentageFill;
        return new Vec3d ( actualElementScale, actualElementScale, actualElementScale );
    }

    /** @param providers an iterable of objects which can provide vectors
     * @param func a method for providing the Vec3d from the given
     * @param <P> an object which can provide a vector
     * @return the maximum values for all the vectors*/
    public static <P> Vec3d getPositiveMaxVector(Iterable<P> providers, Function<P, Vec3d> func){
        double maxX = 0, maxY = 0, maxZ = 0;
        for (P p : providers) {
            Vec3d scaling = func.apply(p);
            maxX = Math.max(scaling.x, maxX);
            maxY = Math.max(scaling.y, maxY);
            maxZ = Math.max(scaling.z, maxZ);
        }
        return new Vec3d(maxX, maxY, maxZ);
    }


    /*
    public static void alignWithin(Vec3d max, Vec3d scale, ElementAlignment xAlign, ElementAlignment yAlign, ElementAlignment zAlign) {
        Vec3d alignArray = alignArrayWithin(max, scale, xAlign, yAlign, zAlign);
        align(alignArray);
    }

    public static void align(Vec3d align) {
        RenderSystem.translated(align.x, align.y, align.z);
    }

    */


    /** @param face the direction to offset
     * @return the offset vector */
    public static Vec3d getFaceOffset(Direction face, double scale){
        return convertVector(face.getDirectionVec()).scale(scale);
    }

    /** returns the default rotation vector of a screen facing a particular direction
     * @param f the direction the screen is facing
     * @return the screens rotation, in the form of (pitch, yaw, roll)*/
    public static Vec3d getScreenRotation(Direction f){
        double pitch = f.getYOffset()*90;
        double yaw = f.getAxis().isHorizontal() ? f.getOpposite().getHorizontalAngle() : (f==Direction.UP ? Direction.NORTH.getOpposite(): Direction.NORTH).getHorizontalAngle();
        double roll = 0;

        return new Vec3d(pitch, yaw, roll);
    }

    /** returns a normalised look vector from the given rotations
     * @param pitch the pitch of the vector in degrees
     * @param yaw the yaw in degrees
     * @return the normalised vector */
    public static Vec3d getLookVector(double pitch, double yaw){
        return getLookVectorRadians(toRadians(pitch), -toRadians(yaw));
    }

    /** returns a normalised look vector from the given rotations
     * @param pitch the pitch of the vector in radians
     * @param yaw the yaw in radians
     * @return the normalised vector */
    private static Vec3d getLookVectorRadians(double pitch, double yaw){
        return new Vec3d(Math.cos(pitch) * Math.sin(yaw), -Math.sin(pitch), Math.cos(pitch) * Math.cos(yaw));
    }

    /** calculates if the player is facing the correct side of the screen
     * @param playerV the players look vector
     * @param screenV the screens look vector
     * @return if the player is facing the screen */
    public static boolean isFacingScreen(Vec3d playerV, Vec3d screenV){
        return playerV.dotProduct(screenV) > 0;
    }

    /** calculates the player's distance from the screen
     * @param lookOrigin the player's eye position, relative to the world
     * @param screenOrigin the centre of the screen, relative to the world
     * @param playerV the player's look vector
     * @param screenV the screens's rotation vector
     * @return the exact distance to the screen from the player */
    public static double getDistanceToScreen(Vec3d lookOrigin, Vec3d screenOrigin, Vec3d playerV, Vec3d screenV){
        return (-((lookOrigin.subtract(screenOrigin)).dotProduct(screenV)))/(playerV.dotProduct(screenV));
    }

    /** used to find the exact position of intersection in the world
     * @param lookOrigin the player's eye position
     * @param playerV the players look vector
     * @param distance the exact distance to the screen.
     * @return the point of intersection, the exact position in the world. */
    public static Vec3d getIntersection(Vec3d lookOrigin, Vec3d playerV, double distance){
        return lookOrigin.add(playerV.scale(distance));
    }

    /** calculates the screens horizontal vector
     * @param screenV the screens's rotation vector
     * @param rollV the screens's roll vector
     * @return the horizontal vector)*/
    public static Vec3d getHorizontalVector(Vec3d screenV, Vec3d rollV){
        return screenV.crossProduct(rollV).normalize();
    }

    /** calculates the screens vertical vector
     * @param screenV the screens's rotation vector
     * @param hozV the screen's horizontal vector
     * @return the vectical vector */
    public static Vec3d getVerticalVector(Vec3d screenV, Vec3d hozV){
        return screenV.crossProduct(hozV).normalize();
    }

    /** calculates the screens roll vector
     * @param screenV the screens's rotation vector
     * @param roll the screens roll in radians
     * @param pitch the screens pitch in radians
     * @return the screens roll vector*/
    public static Vec3d getRollVector(Vec3d screenV, double roll, double pitch){
        return Y_VEC.scale(Math.cos(roll)).subtract(screenV.scale(Math.sin(pitch)).scale(1-Math.cos(roll))).add(screenV.crossProduct(Y_VEC.scale(Math.sin(roll))));
    }

    /** calculates the screens horizontal vector if the pitch equals -90 or +90
     * @param yaw the screens yaw in radians
     * @return the screens horizontal vector*/
    public static Vec3d getHorizontalVectorSpecialCase(double yaw){
        return X_VEC.scale(Math.cos(yaw)).subtract(Z_VEC.scale(Math.sin(yaw)));
    }

    /** calculates the screens vertical vector if the pitch equals -90 or +90
     * or the horizontal vector is equivalent to Vec3d.ZERO
     * @param yaw the screens yaw in radians
     * @return the screens vertical vector*/
    public static Vec3d getVerticalVectorSpecialCase(double yaw){
        return X_VEC.scale(Math.sin(yaw)).add(Z_VEC.scale(Math.cos(yaw)));
    }

    /** calculates the screens horizontal and vertical vectors, taking into account the special case to avoid {@link ArithmeticException}
     * @param rotation the display
     * @param screenV the screen vector
     * @return returns the screen vectors in the form (horizontal, vectical)*/
    public static Vec3d[] getScreenVectors(Vec3d rotation, Vec3d screenV){
        Vec3d horizontal, vertical;
        if(rotation.x == 90 || rotation.x == -90){

            double actualYaw = -toRadians(rotation.y + (rotation.x == 90 ? + rotation.z : - rotation.z));

            horizontal = getHorizontalVectorSpecialCase(actualYaw);
            vertical = getVerticalVectorSpecialCase(actualYaw);
            if(rotation.x == -90){
               vertical = vertical.scale(-1);
            }
        }else{
            horizontal = getHorizontalVector(screenV, getRollVector(screenV, toRadians(rotation.z), toRadians(rotation.x)).scale(-1));
            vertical = horizontal.equals(Vec3d.ZERO) ? getVerticalVectorSpecialCase(-toRadians(rotation.y)) : getVerticalVector(screenV, horizontal);
        }
        return new Vec3d[]{horizontal, vertical};
    }

    /** calculates the exact position of the top left corner of the screen, relative to the world
     * @param origin the centre of the screen, relative to the world
     * @param horizontal the screens horizontal vector
     * @param vertical the screens vertical vector
     * @param screenWidth the exact screen width
     * @param screenHeight the exact screen height
     * @return the vector of the top left corner*/
    public static Vec3d getTopLeft(Vec3d origin, Vec3d horizontal, Vec3d vertical, double screenWidth, double screenHeight){
        return origin.subtract(horizontal.scale(screenWidth/2)).add(vertical.scale(screenHeight/2));
    }

    /** calculates the exact position of the top right corner of the screen, relative to the world
     * @param origin the centre of the screen, relative to the world
     * @param horizontal the screens horizontal vector
     * @param vertical the screens vertical vector
     * @param screenWidth the exact screen width
     * @param screenHeight the exact screen height
     * @return the vector of the top right corner*/
    public static Vec3d getTopRight(Vec3d origin, Vec3d horizontal, Vec3d vertical, double screenWidth, double screenHeight){
        return origin.add(horizontal.scale(screenWidth/2)).add(vertical.scale(screenHeight/2));
    }

    /** calculates the exact position of the bottom left corner of the screen, relative to the world
     * @param origin the centre of the screen, relative to the world
     * @param horizontal the screens horizontal vector
     * @param vertical the screens vertical vector
     * @param screenWidth the exact screen width
     * @param screenHeight the exact screen height
     * @return the vector of the bottom left corner*/
    public static Vec3d getBottomLeft(Vec3d origin, Vec3d horizontal, Vec3d vertical, double screenWidth, double screenHeight){
        return origin.subtract(horizontal.scale(screenWidth/2)).subtract(vertical.scale(screenHeight/2));
    }

    /** calculates the exact position of the bottom right corner of the screen, relative to the world
     * @param origin the centre of the screen, relative to the world
     * @param horizontal the screens horizontal vector
     * @param vertical the screens vertical vector
     * @param screenWidth the exact screen width
     * @param screenHeight the exact screen height
     * @return the vector of the bottom right corner*/
    public static Vec3d getBottomRight(Vec3d origin, Vec3d horizontal, Vec3d vertical, double screenWidth, double screenHeight){
        return origin.add(horizontal.scale(screenWidth/2)).subtract(vertical.scale(screenHeight/2));
    }

    /** calculates the exact position clicked on the display, can be null
     * @param sizing the screen's scaling
     * @param origin the centre of the screen, relative to the world
     * @param intersect the exact intersect of the players look vector on the screen, relative to the world
     * @param horizontal  the screens horizontal vector
     * @param vertical the screens vertical vector
     * @return the exact click position, taking into account the origin*/
    @Nullable
    public static Vector2D getClickedPosition(Quad2D sizing, Vec3d origin, Vec3d intersect, Vec3d horizontal, Vec3d vertical){
        Vec3d pos = intersect.subtract(origin);
        double intersect_hoz = pos.dotProduct(horizontal);
        double intersect_ver = pos.dotProduct(vertical);

        if(-sizing.getWidth()/2D < intersect_hoz && intersect_hoz < sizing.getWidth()/2D && -sizing.getHeight()/2D < intersect_ver && intersect_ver < sizing.getHeight()/2D){
            return new Vector2D(sizing.getWidth() - (pos.dotProduct(horizontal)+sizing.getWidth()/2D), sizing.getHeight() - (pos.dotProduct(vertical)+sizing.getHeight()/2D));
        }
        return null;
    }


    private static double distance;
    private static Vec3d playerV, screenV, lookOrigin, origin, intersect, horizontal, vertical;

    /** calculates the exact position clicked on the display, can be null
     * @param from the player / or other entity
     * @param to the screen
     * @param maxDist the maximum block reach of the player
     * @return the exact click position, taking into account the origin*/
    @Nullable
    public static Vector2D getEntityLook(Entity from, IDisplay to, double maxDist){
        if(from == null || to == null){
            return null;
        }
        playerV = getLookVector(from.rotationPitch, from.rotationYaw);
        screenV = getLookVector(to.getScreenRotation().x, to.getScreenRotation().y);
        if(isFacingScreen(playerV, screenV)){
            lookOrigin = from.getEyePosition(1); //TODO TEST
            origin = to.getScreenOrigin();
            distance = getDistanceToScreen(lookOrigin, origin, playerV, screenV);
            if(Math.abs(distance) < maxDist){
               intersect = getIntersection(lookOrigin, playerV, distance);
               Vec3d[] vectors = getScreenVectors(to.getScreenRotation(), screenV);
               horizontal = vectors[0]; vertical = vectors[1];
               return getClickedPosition(to.getGSIBounds(), origin, intersect, horizontal, vertical);
            }
        }
        return null;
    }
    /*
    @Nullable
    public static DisplayClickContext createClickContext(DisplayInteractionType type, PlayerEntity player, IDisplay display){
        return createClickContext(type, player, display, 8);
    }

    @Nullable
    public static DisplayClickContext createClickContext(DisplayInteractionType type, PlayerEntity player, IDisplay display, int maxDist){

        return null;
    }

    @Nullable
    public static DisplayInteractionContext createHoverContext(PlayerEntity player, IDisplay display){
        return createHoverContext(player, display, 8);
    }

    @Nullable
    public static DisplayInteractionContext createHoverContext(PlayerEntity player, IDisplay display, int maxDist){
        Vector2D hoverPosition = getEntityLook(player, display, maxDist);
        if(hoverPosition != null) {
            DisplayInteractionContext hoverContext = new DisplayInteractionContext(display.getGSI(), player, false);
            hoverContext.setDisplayClick(hoverPosition);
            return hoverContext;
        }
        return null;
    }

    /*
    @Nullable
    public static DisplayScreenLook createLook(PlayerEntity player, IDisplay display){
        DisplayScreenLook look = new DisplayScreenLook();
        double[] lookPosition = getDisplayLook(player, display, 8);
        if(lookPosition != null) {
            look.identity = display.getInfoContainerID();
            look.setLookPosition(lookPosition);
            return look;
        }
        return null;
    }

    public static DisplayScreenClick createFakeClick(CommonGSI gsi, double clickX, double clickY, boolean doubleClick, int key) {
        DisplayScreenClick fakeClick = new DisplayScreenClick();
        fakeClick.gsi = gsi;
        fakeClick.type = key == 0 ? BlockInteractionType.LEFT : BlockInteractionType.RIGHT;
        fakeClick.clickX = clickX;
        fakeClick.clickY = clickY;
        fakeClick.intersect = convertVector(gsi.getDisplay().getActualDisplay().getCoords().getBlockPos());
        fakeClick.identity = gsi.identity;
        fakeClick.doubleClick = doubleClick;
        fakeClick.fakeGuiClick = true;
        return fakeClick;
    }
    */

    ////// INTERACTION \\\\\
/*
    public boolean canClickContainer(DisplayElementContainer c, double x, double y) {
        double startX = c.getContainerTranslation().x;
        double startY = c.getContainerTranslation().y;
        double endX = startX + c.getContainerMaxScaling().x;
        double endY = startY + c.getContainerMaxScaling().y;
        return GSIInteractionHelper.checkClick(x, y, new double[] { startX, startY, endX, endY });
    }

    public Tuple<IDisplayElement, double[]> getElementFromXY(DisplayElementContainer c, double x, double y) {
        double offsetX = x - c.getContainerTranslation().x;
        double offsetY = y - c.getContainerTranslation().y;
        return getClickBoxes(c, offsetX, offsetY);
    }

    public Tuple<IDisplayElement, double[]> getClickBoxes(DisplayElementContainer c, double x, double y) {
        return getClickBoxes(c, x, y, c.getAlignmentTranslation());
    }

    public Tuple<IDisplayElement, double[]> getClickBoxes(DisplayElementContainer c, double x, double y, Vec3d align) {
        for (IDisplayElement e : c.elements) {
            if (!(e instanceof IElementStorageHolder)) {
                Vec3d alignArray = c.getAlignmentTranslation(e);

                double startX = align.x + alignArray.x;
                double startY = align.y + alignArray.y;
                double endX = align.x + alignArray.x + e.getActualScaling().x;
                double endY = align.y + alignArray.y + e.getActualScaling().y;
                double[] eBox = new double[] { startX, startY, endX, endY };

                if (GSIInteractionHelper.checkClick(x, y, eBox)) {
                    double subClickX = x - startX;
                    double subClickY = y - startY;
                    return new Tuple(e, new double[] { subClickX, subClickY });
                }
            }
        }
        for (IElementStorageHolder h : c.elements.getSubHolders()) {
            Tuple<IDisplayElement, double[]> clicked = h.getClickBoxes(x, y);
            if (clicked != null) {
                return clicked;
            }
        }
        return null;
    }

    public void lock(DisplayElementContainer c) {
        // when the display is locked we store the exact scale of the element
        c.containerSavedTranslation = DisplayVectorHelper.scaleFromPercentage(c.containerSavedTranslation, c.gsi.getDisplaySizing());
        c.containerSavedSizing = DisplayVectorHelper.scaleFromPercentage(c.containerSavedSizing, c.gsi.getDisplaySizing());
        c.resetRenderValues();
        c.locked = true;
    }

    public void unlock(DisplayElementContainer c) {
        // when the display is unlocked we store a percentage of the element in relation to the display
        c.containerSavedTranslation = DisplayVectorHelper.percentageFromScale(c.containerSavedTranslation, c.gsi.getDisplaySizing());
        c.containerSavedSizing = DisplayVectorHelper.percentageFromScale(c.containerSavedSizing, c.gsi.getDisplaySizing());
        c.resetRenderValues();
        c.locked = false;
    }
    */
}