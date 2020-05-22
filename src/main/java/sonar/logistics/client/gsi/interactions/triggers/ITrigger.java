package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;

public interface ITrigger {

    void trigger(Object source, DisplayInteractionHandler handler);

    default boolean isActive(Object source, DisplayInteractionHandler handler){
        return false;
    }
}
