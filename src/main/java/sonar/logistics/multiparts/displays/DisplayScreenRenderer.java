package sonar.logistics.multiparts.displays;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import sonar.logistics.blocks.PL3Blocks;
import sonar.logistics.items.PL3Items;
import sonar.logistics.multiparts.base.IMultipartRenderer;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.displays.old.gsi.containers.IScaleable;
import sonar.logistics.multiparts.displays.old.gsi.containers.ItemStackElement;
import sonar.logistics.multiparts.displays.old.gsi.containers.ScaleableContainer;

public class DisplayScreenRenderer implements IMultipartRenderer<DisplayScreenTile> {

    @Override
    public void render(TileEntityRendererDispatcher renderDispatcher, DisplayScreenTile tile, MultipartEntry entry, float partialTicks, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight) {

        //// MOVE INTO HOST RENDERING !!!!!
        matrix.pop();
        Matrix4f worldMatrix = matrix.getLast().getMatrix().copy();
        matrix.push();

        Vec3d vec3d = renderDispatcher.renderInfo.getProjectedView();
        double d0 = vec3d.getX();
        double d1 = vec3d.getY();
        double d2 = vec3d.getZ();

        matrix.translate((double)tile.getPos().getX() - d0, (double)tile.getPos().getY() - d1, (double)tile.getPos().getZ() - d2);



        Vec3d sizing = tile.getScreenSizing();
        Vec3d origin = new Vec3d(0.5, 0.5, 0.5);
        origin = origin.add(DisplayVectorHelper.getFaceOffset(tile.getFacing(), 0.5));
        Vec3d fullScalePercent = new Vec3d(100,100,100);

        ScaleableContainer container = new ScaleableContainer(new IScaleable() {
            @Override
            public Vec3d getTranslation() {
                return new Vec3d(0,0,0);
            }

            @Override
            public Vec3d getSizing() {
                return sizing;
            }

            @Override
            public void updateScaleable() {

            }
        });
        container.setSizingFromPercentage(fullScalePercent, fullScalePercent);
        container.updateScaleable();

        ItemStackElement element = new ItemStackElement(container);
        element.setSizingFromPercentage(fullScalePercent, fullScalePercent);
        element.updateScaleable();



        ///// START DISPLAY RENDERING \\\\\

        Direction dir = tile.getFacing();
        matrix.push();



        matrix.translate(0.5, 0.5, 0.5); ///// ALIGN TO THE CENTRE OF THE BLOCK
        matrix.rotate(dir.getRotation()); ///// ROTATE THE RENDERER
        matrix.rotate(new Quaternion(90, 0, 0, true));
        matrix.translate(-sizing.x/2, -sizing.y/2, -0.505); ///// SCREEN OFFSET


        ///// START ELEMENT RENDERING \\\\\

        ////items
        /*
        matrix.push();
        matrix.translate(element.elementScaling.x/2, element.elementScaling.y/2, 0.05);
        matrix.scale(1, -1, -1); ////
        matrix.scale((float)element.elementScaling.x, (float)element.elementScaling.y, 0.0001f);

        ItemStack stack = new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        IBakedModel bakedmodel = itemRenderer.getItemModelWithOverrides(stack, null, null);
        IRenderTypeBuffer.Impl itemRenderBuffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

        if (bakedmodel.isGui3d()) {
            RenderHelper.setupGui3DDiffuseLighting();
        }else{
            RenderHelper.setupGuiFlatDiffuseLighting();
        }
        matrix.getLast().getNormal().set(Matrix3f.makeScaleMatrix(1, -1, -1)); //scale the lighting too.
        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GUI, false, matrix, itemRenderBuffer, light , OverlayTexture.NO_OVERLAY, bakedmodel);// GUI LIGHT: 15728880
        itemRenderBuffer.finish();
        RenderSystem.setupLevelDiffuseLighting(worldMatrix);

        matrix.pop();
        */

        ///progress bars

        //DisplayRenderHelper.colouredRect(matrix.getLast(), renderer, light, overlayLight, 0,0F, (float)element.getSizing().x, (float)element.getSizing().y, 255, 255, 255, 255);

        DisplayRenderHelper.fluidRect(new FluidStack(Fluids.LAVA, 500), matrix.getLast(), renderer, light, overlayLight, 0,0, (float)element.getSizing().x, (float)element.getSizing().y);

        ///// END ELEMENT RENDERING \\\\\


        matrix.pop();

        ///// END DISPLAY RENDERING \\\\\


    }
}
