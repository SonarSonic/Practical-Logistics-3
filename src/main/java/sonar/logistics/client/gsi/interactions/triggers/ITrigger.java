package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;

public interface ITrigger {

    void trigger(Object source, GSIInteractionHandler handler);

    default boolean isActive(Object source, GSIInteractionHandler handler){
        return false;
    }
}
