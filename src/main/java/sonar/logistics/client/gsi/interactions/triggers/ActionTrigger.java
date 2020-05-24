package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;

public class ActionTrigger implements ITrigger {

    public int triggerID;

    public ActionTrigger(int triggerID){
        this.triggerID = triggerID;
    }

    @Override
    public void trigger(Object source, GSIInteractionHandler handler) {
        handler.trigger(source, triggerID);
    }

    @Override
    public boolean isActive(Object source, GSIInteractionHandler handler) {
        return handler.isActive(source, triggerID);
    }
}
