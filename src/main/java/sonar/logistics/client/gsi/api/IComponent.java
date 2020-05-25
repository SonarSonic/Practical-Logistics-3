package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ComponentStyling;
import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IComponent {

    /**the GSI this component is hosted by, used primarily with interactions*/
    GSI getGSI();

    /**should always be called before using the component, and is typically called when it is added to the GSI*/
    void setGSI(GSI gsi);

    ///

    /**the bounds are used for finding the position / size of the component
     * the bounds will always be relative to the GSI, which means each component can be rendered independently of any sub contaienrs*/
    @Nonnull
    ComponentBounds getBounds();

    /**the components styling defines default text colours / background colours / margin widths etc.*/
    @Nonnull
    ComponentStyling getStyling();

    void setBounds(ComponentBounds bounds);

    void setStyling(ComponentStyling styling);

    ///

    /**this method "builds" the layout for the component based on the bounds passed to it, it should always be called before interacting with the component
     * the bounds are passed by the "host" - typically a GSI but could also be passed by containers like GridContainer / FloatingContainer
     * this method should set the ComponentBounds, keeping the x, y alignment of the passed "host's" bounds*/
    void build(Quad2D bounds);

    /**this method will be called by the "host" and is used for rendering the component within the bounds setup*/
    void render(GSIRenderContext context);

    ///

    /**this is used if the component is itself a "host"
     * sub components should be rendered by component that declares them, not the declaring component's "host" */
    @Nullable
    default List<IComponent> getSubComponents(){
         return null;
     }

}
