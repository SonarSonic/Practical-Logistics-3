package sonar.logistics.client.gsi.api;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.context.DisplayClickContext;
import sonar.logistics.client.gsi.context.DisplayInteractionContext;

import javax.annotation.Nullable;
import java.util.List;

public interface IScaleableComponent extends IScaleable, IRenderable {

    @Nullable
    default List<IScaleableComponent> getSubComponents(){
         return null;
     }

    default boolean canInteract(DisplayInteractionContext context) {
        return getAlignment().getBounds().contains(context.displayHit);
    }

    default boolean onClicked(DisplayClickContext context){
        return false;
    }

    default boolean onHovered(DisplayInteractionContext context){
        return false;
    }

}
