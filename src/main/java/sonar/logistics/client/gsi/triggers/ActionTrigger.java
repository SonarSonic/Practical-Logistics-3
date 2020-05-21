package sonar.logistics.client.gsi.triggers;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;

public class ActionTrigger implements ITrigger {

    public int triggerID;

    public ActionTrigger(int triggerID){
        this.triggerID = triggerID;
    }

    @Override
    public void trigger(Object source, DisplayInteractionHandler handler) {
        handler.trigger(source, triggerID);
    }

    @Override
    public boolean isActive(Object source, DisplayInteractionHandler handler) {
        return handler.isActive(source, triggerID);
    }
}
