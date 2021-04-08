package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;

public interface ITrigger<O extends Object> {

    /**
     * the trigger to send
     */
    void trigger(O source, GSIInteractionHandler handler);

    default boolean isActive(O source, GSIInteractionHandler handler){
        return false;
    }
}
