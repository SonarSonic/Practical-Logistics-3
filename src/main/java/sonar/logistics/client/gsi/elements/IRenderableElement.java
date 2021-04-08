package sonar.logistics.client.gsi.elements;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.util.vectors.Quad2D;

import javax.annotation.Nullable;

public interface IRenderableElement {

    default boolean isUniformScaling(GSIRenderContext context){
        return false;
    }

    default boolean canRender(GSIRenderContext context){
        return true;
    }

    void render(GSIRenderContext context, Quad2D bounds);

    @Nullable
    default Vec3d getUnscaledSize(){
        return null; ///the element can fill any scaling.
    }

}
