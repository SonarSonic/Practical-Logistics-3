package sonar.logistics.client.gsi.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import sonar.logistics.util.vectors.Quad2F;
import sonar.logistics.util.vectors.Vector2F;
import sonar.logistics.client.gsi.style.ComponentAlignment;

import java.util.ArrayList;
import java.util.List;

/**
 * Batches ItemStack rendering, there is about a 30% performance increase by not changing the GL settings for every item
 * A {@link sonar.logistics.client.gsi.api.IComponent}
 */
public class BatchedItemRenderer {

    public static ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public List<BatchedItemStack> batched3DItemStacks = new ArrayList<>();
    public List<BatchedItemStack> batchedFlatItemStacks = new ArrayList<>();

    public void preRender(GSIRenderContext context){
        batched3DItemStacks.clear();
        batchedFlatItemStacks.clear();
    }

    public void postRender(GSIRenderContext context){

        if(!batched3DItemStacks.isEmpty()) {
            renderBatchedItemStacks(context, batched3DItemStacks, true, GSIRenderHelper.ITEM_OFFSET,0.002F);
        }
        if(!batchedFlatItemStacks.isEmpty()){
            renderBatchedItemStacks(context, batchedFlatItemStacks, false, GSIRenderHelper.ITEM_OFFSET,0.001F);
        }
    }

    public static void renderBatchedItemStacks(GSIRenderContext context, List<BatchedItemStack> batchedItemStacks, boolean isGui3D, float zOffset, float zScale){
        if(context.isGui){
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableAlphaTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if(isGui3D) {
            net.minecraft.client.renderer.RenderHelper.setupGui3DDiffuseLighting();
        }else{
            net.minecraft.client.renderer.RenderHelper.setupGuiFlatDiffuseLighting();
        }

        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());

        for (BatchedItemStack stack : batchedItemStacks) {
            context.matrix.push();
            context.matrix.translate(stack.alignment.getX(), stack.alignment.getY(), zOffset);
            context.matrix.scale(1, -1, -1); ////
            context.matrix.scale((float) stack.scaling.x, (float) stack.scaling.y, zScale);
            context.scaleNormals(1, -1, 1);
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


    public void addBatchedItemStack(ItemStack toRender, Quad2F bounds){
        BatchedItemStack batchedItemStack = new BatchedItemStack(toRender, bounds);
        if(batchedItemStack.isGui3D){
            batched3DItemStacks.add(batchedItemStack);
        }else{
            batchedFlatItemStacks.add(batchedItemStack);
        }
    }

    public static class BatchedItemStack{

        public static Vector2F ITEM_RATIO = new Vector2F(1,1);

        public ItemStack toRender;
        public IBakedModel model;
        public Vector2F alignment;
        public Vector2F scaling;
        public boolean isGui3D;

        public BatchedItemStack(ItemStack toRender, Quad2F bounds){
            this.toRender = toRender;
            this.model = itemRenderer.getItemModelWithOverrides(toRender, null, null);
            this.scaling = Vector2F.getSizingFromRatio(bounds.getSizing(), ITEM_RATIO);
            this.alignment = bounds.getAlignment().add(scaling.x/2, scaling.y/2).add(Vector2F.align(scaling, bounds.getSizing(), ComponentAlignment.CENTERED, ComponentAlignment.CENTERED));
            this.isGui3D = model.isGui3d();
        }

    }

}
