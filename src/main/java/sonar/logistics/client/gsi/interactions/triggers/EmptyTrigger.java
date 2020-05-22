package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;

public class EmptyTrigger implements ITrigger {

    public static final EmptyTrigger INSTANCE = new EmptyTrigger();

    @Override
    public void trigger(Object source, DisplayInteractionHandler handler) {

    }

    @Override
    public boolean isActive(Object source, DisplayInteractionHandler handler) {
        return false;
    }
}
