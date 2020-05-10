package sonar.logistics.client.gsi.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sonar.logistics.blocks.PL3Blocks;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;

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


    public void addBatchedItemStack(ItemStack toRender, Vec3d alignment, Vec3d scaling){
        BatchedItemStack batchedItemStack = new BatchedItemStack(toRender, alignment, scaling);
        if(batchedItemStack.isGui3D){
            batched3DItemStacks.add(batchedItemStack);
        }else{
            batchedFlatItemStacks.add(batchedItemStack);
        }
    }

    public static class BatchedItemStack{

        public static Vec3d unscaledSize = new Vec3d(1,1,1);

        public ItemStack toRender;
        public IBakedModel model;
        public Vec3d alignment;
        public Vec3d scaling;
        public boolean isGui3D;

        public BatchedItemStack(ItemStack toRender, Vec3d alignment, Vec3d maxSizing){
            this.toRender = toRender;
            this.model = itemRenderer.getItemModelWithOverrides(toRender, null, null);
            this.scaling = DisplayVectorHelper.scaleFromUnscaledSize(unscaledSize, maxSizing, 1);
            this.alignment = alignment.add((maxSizing.getX()/2 - scaling.getX()/2) + scaling.x/2, (maxSizing.getY()/2 - scaling.getY()/2) + scaling.y/2, 0);
            this.isGui3D = model.isGui3d();
        }

    }

}
