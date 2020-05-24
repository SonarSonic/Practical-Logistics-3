package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ComponentStyling;
import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;
import java.util.List;

public interface IComponent {

    GSI getGSI();

    void setGSI(GSI gsi);

    ///

    ComponentBounds getBounds();

    ComponentStyling getStyling();

    void setBounds(ComponentBounds bounds);

    void setStyling(ComponentStyling styling);

    ///

    void build(Quad2D bounds);

    void render(GSIRenderContext context);

    ///

    default boolean canRender(GSIRenderContext context){
        return true;
    }

    @Nullable
    default List<IComponent> getSubComponents(){
         return null;
     }

}
