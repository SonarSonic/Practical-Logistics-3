package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;

public interface ITrigger<O extends Object> {

    void trigger(O source, GSIInteractionHandler handler);

    default boolean isActive(O source, GSIInteractionHandler handler){
        return false;
    }
}
