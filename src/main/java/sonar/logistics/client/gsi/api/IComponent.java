package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.interactions.DefaultInteraction;
import sonar.logistics.client.gsi.interactions.IInteractionListener;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ComponentStyling;
import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IComponent {

    ComponentBounds getBounds();

    ComponentStyling getStyling();

    void setBounds(ComponentBounds bounds);

    void setStyling(ComponentStyling styling);

    ///

    void build(Quad2D bounds);

    void render(GSIRenderContext context, DisplayInteractionHandler handler);

    ///

    default boolean canRender(GSIRenderContext context, DisplayInteractionHandler handler){
        return true;
    }

    @Nullable
    default List<IComponent> getSubComponents(){
         return null;
     }

    @Nonnull
    default IInteractionListener getInteraction(DisplayInteractionHandler context){ return DefaultInteraction.INSTANCE; }

}
