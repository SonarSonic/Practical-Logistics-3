package sonar.logistics.client.gsi.context;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3i;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.render.BatchedItemStackRenderer;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

public class ScaleableRenderContext {

    public GSI gsi;
    public BatchedItemStackRenderer itemStackRenderer = new BatchedItemStackRenderer();
    public float partialTicks;
    public MatrixStack matrix;
    private IRenderTypeBuffer worldBuffer;
    public int light;
    public int overlay;

    private Matrix4f lightingMatrix;
    public boolean isGui;

    public int bakedLight;
    public Vector3f normal = new Vector3f(0, 0, 0);

    //// GUI RENDER CONTEXT \\\\
    public ScaleableRenderContext(GSI gsi, float partialTicks, MatrixStack matrix){
        this.gsi = gsi;
        this.partialTicks = partialTicks;
        this.matrix = matrix;
        this.worldBuffer = null;
        this.light = ScaleableRenderHelper.FULL_LIGHT;
        this.overlay = OverlayTexture.NO_OVERLAY;

        this.lightingMatrix = null;
        this.isGui = true;

        this.bakedLight = ScaleableRenderHelper.getBakedLight(light);
        this.setNormalFromDir(Direction.DOWN);
    }

    //// WORLD RENDER CONTEXT \\\\
    public ScaleableRenderContext(GSI gsi, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay, Matrix4f lightingMatrix, Direction dir, boolean isGui){
        this.gsi = gsi;
        this.partialTicks = partialTicks;
        this.matrix = matrix;
        this.worldBuffer = buffer;
        this.light = light;
        this.overlay = overlay;

        this.lightingMatrix = lightingMatrix;
        this.isGui = isGui;


        this.bakedLight = ScaleableRenderHelper.getBakedLight(light);
        this.setNormalFromDir(dir.getOpposite());

    }

    public void preRender() {
        itemStackRenderer.preRender(this);
    }

    public void postRender() {
        itemStackRenderer.postRender(this);
    }

    ////

    private IRenderTypeBuffer.Impl renderBuffer;

    public IRenderTypeBuffer getRenderBuffer(boolean batched){
        if(batched && worldBuffer != null){
            return worldBuffer;
        }
        return renderBuffer;
    }

    public IRenderTypeBuffer startRenderBuffer(boolean batched){
        if(batched && worldBuffer != null){
            return worldBuffer;
        }
        return renderBuffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
    }

    public void finishRenderBuffer(boolean batched){
        if(batched && worldBuffer != null){
            return;
        }
        renderBuffer.finish();
    }

    ////


    public void resetLightingMatrix(){
        if(lightingMatrix != null && !isGui){
            RenderSystem.setupLevelDiffuseLighting(lightingMatrix);
        }else if(isGui) {
            RenderSystem.setupGui3DDiffuseLighting();
        }
    }

    public void setNormalFromDir(Direction dir){
        Vec3i faceNormal = dir.getDirectionVec();
        normal = new Vector3f((float)faceNormal.getX(), (float)faceNormal.getY(), (float)faceNormal.getZ());
    }

    public void scaleNormals(float x, float y, float z){
        matrix.getLast().getNormal().set(Matrix3f.makeScaleMatrix(x, y, z));
    }

    public Matrix4f getMatrix4f(){
        return matrix.getLast().getMatrix();
    }

    public Matrix3f getNormal3f(){
        return matrix.getLast().getNormal();
    }

    public Vector3f getNormal(){
        Vector3f transformed = normal.copy();
        transformed.transform(getNormal3f());
        return transformed;
    }

}
