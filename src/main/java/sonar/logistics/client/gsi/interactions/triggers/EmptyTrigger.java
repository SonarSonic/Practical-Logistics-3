package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;

public class EmptyTrigger implements ITrigger {

    public static final EmptyTrigger INSTANCE = new EmptyTrigger();

    @Override
    public void trigger(Object source, GSIInteractionHandler handler) {

    }

    @Override
    public boolean isActive(Object source, GSIInteractionHandler handler) {
        return false;
    }
}
