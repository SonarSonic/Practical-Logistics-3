package sonar.logistics.client.gsi.context;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.*;
import net.minecraft.util.Direction;
import net.minecraftforge.client.extensions.IForgeVertexBuilder;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.gsi.render.BatchedItemStackRenderer;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

public class ScaleableRenderContext {

    public GSI gsi;
    public BatchedItemStackRenderer itemStackRenderer = new BatchedItemStackRenderer();
    public float partialTicks;
    public MatrixStack matrix;
    public IRenderTypeBuffer buffer;
    public int light;
    public int overlay;

    private Matrix4f lightingMatrix;
    public boolean isGui;

    public int bakedLight;

    public ScaleableRenderContext(GSI gsi, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay, Matrix4f lightingMatrix, boolean isGui){
        this.gsi = gsi;
        this.partialTicks = partialTicks;
        this.matrix = matrix;
        this.buffer = buffer;
        this.light = light;
        this.overlay = overlay;

        this.lightingMatrix = lightingMatrix;
        this.isGui = isGui;

        this.bakedLight = ScaleableRenderHelper.getBakedLight(light);
    }

    public void preRender() {
        itemStackRenderer.preRender(this);
    }

    public void postRender() {
        itemStackRenderer.postRender(this);
    }

    public void resetLightingMatrix(){
        if(lightingMatrix != null && !isGui){
            RenderSystem.setupLevelDiffuseLighting(lightingMatrix);
        }else if(isGui) {
            RenderSystem.setupGuiFlatDiffuseLighting();
        }
    }

    public Matrix4f getMatrix4f(){
        return matrix.getLast().getMatrix();
    }

    public Matrix3f getNormal3f(){
        return matrix.getLast().getNormal();
    }
}
