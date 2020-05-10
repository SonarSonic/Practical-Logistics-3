package sonar.logistics.client.gsi.context;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.*;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.render.BatchedItemStackRenderer;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

public class ScaleableRenderContext {

    public GSI gsi;
    public BatchedItemStackRenderer itemStackRenderer = new BatchedItemStackRenderer();
    public float partialTicks;
    public MatrixStack matrix;
    private IRenderTypeBuffer worldBuffer;
    private IRenderTypeBuffer.Impl tessellatorBuffer;
    public int light;
    public int overlay;

    private Matrix4f lightingMatrix;
    public boolean isGui;

    public int bakedLight;

    //// GUI RENDER CONTEXT \\\\
    public ScaleableRenderContext(GSI gsi, float partialTicks, MatrixStack matrix){
        this.gsi = gsi;
        this.partialTicks = partialTicks;
        this.matrix = matrix;
        this.worldBuffer = null;
        this.light = ScaleableRenderHelper.FULL_LIGHT;
        this.overlay = 0;
        this.lightingMatrix = null;
        this.isGui = true;
        this.bakedLight = ScaleableRenderHelper.getBakedLight(light);
    }

    //// WORLD RENDER CONTEXT \\\\
    public ScaleableRenderContext(GSI gsi, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay, Matrix4f lightingMatrix, boolean isGui){
        this.gsi = gsi;
        this.partialTicks = partialTicks;
        this.matrix = matrix;
        this.worldBuffer = buffer;
        this.light = light;
        this.overlay = overlay;

        this.lightingMatrix = lightingMatrix;
        this.isGui = isGui;

        this.bakedLight = ScaleableRenderHelper.getBakedLight(light);
    }

    public IRenderTypeBuffer getWorldBuffer(){
        return worldBuffer != null ? worldBuffer : tessellatorBuffer;
    }

    public IRenderTypeBuffer.Impl getTessellatorBuffer(){
        return tessellatorBuffer;
    }

    public void preRender() {
        tessellatorBuffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        itemStackRenderer.preRender(this);
    }

    public void postRender() {
        tessellatorBuffer.finish();
        itemStackRenderer.postRender(this); //this uses & finishes the tessellator buffer so should always be called after.
    }

    public void resetLightingMatrix(){
        if(lightingMatrix != null && !isGui){
            RenderSystem.setupLevelDiffuseLighting(lightingMatrix);
        }else if(isGui) {
            RenderSystem.setupGui3DDiffuseLighting();
        }
    }

    public Matrix4f getMatrix4f(){
        return matrix.getLast().getMatrix();
    }

    public Matrix3f getNormal3f(){
        return matrix.getLast().getNormal();
    }
}
