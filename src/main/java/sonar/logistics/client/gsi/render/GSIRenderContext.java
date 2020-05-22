package sonar.logistics.client.gsi.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3i;
import sonar.logistics.client.gsi.GSI;

//the render context is used to bridge the gap between gui rendering and world rendering, allowing both to share the same render code.
public class GSIRenderContext {

    public GSI gsi;
    public BatchedItemRenderer itemStackRenderer = new BatchedItemRenderer();
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
    public GSIRenderContext(GSI gsi, float partialTicks, MatrixStack matrix){
        this.gsi = gsi;
        this.partialTicks = partialTicks;
        this.matrix = matrix;
        this.worldBuffer = null;
        this.light = GSIRenderHelper.FULL_LIGHT;
        this.overlay = OverlayTexture.NO_OVERLAY;

        this.lightingMatrix = null;
        this.isGui = true;

        this.bakedLight = GSIRenderHelper.getBakedLight(light);
        this.setNormalFromDir(Direction.DOWN);
    }

    //// WORLD RENDER CONTEXT \\\\
    public GSIRenderContext(GSI gsi, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay, Matrix4f lightingMatrix, Direction dir, boolean isGui){
        this.gsi = gsi;
        this.partialTicks = partialTicks;
        this.matrix = matrix;
        this.worldBuffer = buffer;
        this.light = light;
        this.overlay = overlay;

        this.lightingMatrix = lightingMatrix;
        this.isGui = isGui;


        this.bakedLight = GSIRenderHelper.getBakedLight(light);
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
    private int pushed = 0;

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
        pushed ++;
        if(pushed == 1){
            renderBuffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        }
        return renderBuffer;
    }

    public void finishRenderBuffer(boolean batched){
        if(batched && worldBuffer != null){
            return;
        }
        pushed--;
        if(pushed == 0) {
            renderBuffer.finish();
        }
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
