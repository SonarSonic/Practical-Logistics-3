package sonar.logistics.client.gsi.api;

import jdk.internal.jline.internal.Nullable;
import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;

public interface IRenderableElement {

    default boolean isUniformScaling(ScaleableRenderContext context){
        return false;
    }

    default boolean canRender(ScaleableRenderContext context){
        return true;
    }

    void render(ScaleableRenderContext context, Vec3d alignment, Vec3d scaling);

    @Nullable
    default Vec3d getUnscaledSize(){
        return null; ///the element can fill any scaling.
    }

}