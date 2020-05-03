package sonar.logistics.multiparts.displays;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;
import sonar.logistics.multiparts.base.IMultipartRenderer;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;

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



        ////// TEST CONSTRUCTION \\\\\\

        Vec3d sizing = tile.getScreenSizing();
        Vec3d origin = new Vec3d(0.5, 0.5, 0.5);
        origin = origin.add(DisplayVectorHelper.getFaceOffset(tile.getFacing(), 0.5));

        /////// END TEST CONSTRUCTION \\\\\\


        ///// START DISPLAY RENDERING \\\\\

        Direction dir = tile.getFacing();
        Quaternion rotation = dir.getRotation();
        matrix.push();



        matrix.translate(0.5, 0.5, 0.5); ///// ALIGN TO THE CENTRE OF THE BLOCK
        matrix.rotate(rotation); ///// ROTATE THE RENDERER
        matrix.rotate(new Quaternion(90, 0, 0, true));
        matrix.translate(-sizing.x/2, -sizing.y/2, -0.5); ///// SCREEN OFFSET



        ///// START ELEMENT RENDERING \\\\\

        ////items
        ScaleableRenderContext renderContext = new ScaleableRenderContext(tile.getGSI(), partialTicks, matrix, renderer, light, overlayLight, worldMatrix, false);
        tile.getGSI().render(renderContext);
        ///progress bars

        //DisplayRenderHelper.colouredRect(matrix.getLast(), renderer, light, overlayLight, 0,0F, (float)element.getSizing().x, (float)element.getSizing().y, 255, 255, 255, 255);

        //DisplayRenderHelper.fluidRect(new FluidStack(Fluids.LAVA, 500), matrix.getLast(), renderer, light, overlayLight, 0,0, (float)element.getSizing().x, (float)element.getSizing().y);

        ///// END ELEMENT RENDERING \\\\\


        matrix.pop();

        ///// END DISPLAY RENDERING \\\\\


    }
}
