package sonar.logistics.common.multiparts.displays;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.vectors.VectorHelper;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.common.multiparts.base.IMultipartRenderer;
import sonar.logistics.common.multiparts.base.MultipartEntry;

public class DisplayScreenRenderer implements IMultipartRenderer<DisplayScreenTile> {

    @Override
    public void render(TileEntityRendererDispatcher renderDispatcher, DisplayScreenTile tile, MultipartEntry entry, float partialTicks, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight, Matrix4f lightingMatrix) {

        matrix.pop();
        Matrix4f worldMatrix = matrix.getLast().getMatrix().copy();
        matrix.push();

        Vec3d vec3d = renderDispatcher.renderInfo.getProjectedView();
        double d0 = vec3d.getX();
        double d1 = vec3d.getY();
        double d2 = vec3d.getZ();

        matrix.translate((double)tile.getPos().getX() - d0, (double)tile.getPos().getY() - d1, (double)tile.getPos().getZ() - d2);

        Quad2D sizing = tile.getGSIBounds();
        Vec3d origin = new Vec3d(0.5, 0.5, 0.5);
        origin = origin.add(VectorHelper.getFaceOffset(tile.getFacing(), 0.5));


        ///// START DISPLAY RENDERING \\\\\

        Direction dir = tile.getFacing();
        Quaternion rotation = dir.getRotation();
        matrix.push();
        matrix.translate(0.5, 0.5, 0.5); ///// ALIGN TO THE CENTRE OF THE BLOCK
        matrix.rotate(rotation); ///// ROTATE THE RENDERER
        matrix.rotate(new Quaternion(90, 0, 0, true));
        matrix.translate(-sizing.getWidth()/2, -sizing.getHeight()/2, -0.5); ///// SCREEN OFFSET



        ///// START GSI RENDERING \\\\\

        GSIRenderContext renderContext = new GSIRenderContext(tile.getGSI(), partialTicks, matrix, renderer, light, overlayLight, worldMatrix, dir, false);
        tile.getGSI().interactionHandler.updateMouseFromDisplay(Minecraft.getInstance().player, tile);
        tile.getGSI().render(renderContext);
        ///// END GSI RENDERING \\\\\


        matrix.pop();

        ///// END DISPLAY RENDERING \\\\\


    }
}
