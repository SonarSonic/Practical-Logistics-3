package sonar.logistics.client.gsi.api;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;

public interface IRenderableElement {

    default boolean isUniformScaling(ScaleableRenderContext context){
        return false;
    }

    default boolean canRender(ScaleableRenderContext context){
        return true;
    }

    void render(ScaleableRenderContext context, Quad2D bounds);

    @Nullable
    default Vec3d getUnscaledSize(){
        return null; ///the element can fill any scaling.
    }

}
