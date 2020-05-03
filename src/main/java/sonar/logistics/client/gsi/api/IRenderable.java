package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.context.ScaleableRenderContext;

public interface IRenderable {

    default boolean canRayTrace(){
        return true;
    }

    default boolean canRender(ScaleableRenderContext context){
        return true;
    }

    void render(ScaleableRenderContext context);

}
