package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IScaleableComponent extends IScaleable, IRenderable {

    @Nullable
    default List<IScaleableComponent> getSubComponents(){
         return null;
     }

    @Nonnull
    default IInteractionListener getInteraction(DisplayInteractionHandler context){ return DefaultInteraction.INSTANCE; }

}
