package sonar.logistics.client.gsi.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;
import sonar.logistics.common.multiparts.displays.old.info.elements.base.ElementAlignment;

import java.util.ArrayList;
import java.util.List;

public class BatchedItemStackRenderer {

    public static ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public List<BatchedItemStack> batched3DItemStacks = new ArrayList<>();
    public List<BatchedItemStack> batchedFlatItemStacks = new ArrayList<>();

    public void preRender(ScaleableRenderContext context){
        batched3DItemStacks.clear();
        batchedFlatItemStacks.clear();
    }

    public void postRender(ScaleableRenderContext context){

        if(!batched3DItemStacks.isEmpty()) {
            renderBatchedItemStacks(context, batched3DItemStacks, true, ScaleableRenderHelper.ITEM_OFFSET,0.002F);
        }
        if(!batchedFlatItemStacks.isEmpty()){
            renderBatchedItemStacks(context, batchedFlatItemStacks, false, ScaleableRenderHelper.ITEM_OFFSET,0.001F);
        }
    }

    public static void renderBatchedItemStacks(ScaleableRenderContext context, List<BatchedItemStack> batchedItemStacks, boolean isGui3D, float zOffset, float zScale){
        if(context.isGui){
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableAlphaTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }


        if(isGui3D) {
            RenderHelper.setupGui3DDiffuseLighting();
        }else{
            RenderHelper.setupGuiFlatDiffuseLighting();
        }

        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());

        for (BatchedItemStack stack : batchedItemStacks) {
            context.matrix.push();
            context.matrix.translate(stack.alignment.getX(), stack.alignment.getY(), zOffset);
            context.matrix.scale(1, -1, context.isGui ? 1 : -1); ////
            context.matrix.scale((float) stack.scaling.x, (float) stack.scaling.y, zScale);
            context.matrix.getLast().getNormal().set(Matrix3f.makeScaleMatrix(1, -1, 1)); //scale the normals
            itemRenderer.renderItem(stack.toRender, ItemCameraTransforms.TransformType.GUI, false, context.matrix, buffer, context.light, OverlayTexture.NO_OVERLAY, stack.model);
            context.matrix.pop();
        }

        buffer.finish();
        context.resetLightingMatrix();

        if(context.isGui) {
            //RenderSystem.enableDepthTest();

            RenderSystem.disableAlphaTest();
            RenderSystem.disableRescaleNormal();
        }
    }


    public void addBatchedItemStack(ItemStack toRender, Quad2D bounds){
        BatchedItemStack batchedItemStack = new BatchedItemStack(toRender, bounds);
        if(batchedItemStack.isGui3D){
            batched3DItemStacks.add(batchedItemStack);
        }else{
            batchedFlatItemStacks.add(batchedItemStack);
        }
    }

    public static class BatchedItemStack{

        public static Vector2D ITEM_RATIO = new Vector2D(1,1);

        public ItemStack toRender;
        public IBakedModel model;
        public Vector2D alignment;
        public Vector2D scaling;
        public boolean isGui3D;

        public BatchedItemStack(ItemStack toRender, Quad2D bounds){
            this.toRender = toRender;
            this.model = itemRenderer.getItemModelWithOverrides(toRender, null, null);
            this.scaling = Vector2D.getSizingFromRatio(bounds.getSizing(), ITEM_RATIO);
            this.alignment = bounds.getAlignment().add(scaling.x/2, scaling.y/2).add(Vector2D.align(scaling, bounds.getSizing(), ElementAlignment.CENTERED, ElementAlignment.CENTERED));
            this.isGui3D = model.isGui3d();
        }

    }

}
