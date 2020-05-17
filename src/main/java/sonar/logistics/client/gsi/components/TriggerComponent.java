package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.api.IInteractionListener;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;

//a simple component which can trigger something, note by default it has no rendering.
public class TriggerComponent extends AbstractStyledScaleable implements IInteractionListener {

    public int triggerId;

    public TriggerComponent(int triggerId){
        this.triggerId = triggerId;
    }

    @Override
    public boolean mouseClicked(DisplayInteractionHandler handler, int button) {
        handler.toggle(this, triggerId);
        return true;
    }

    @Override
    public boolean isMouseOver(DisplayInteractionHandler context) {
        return bounds.renderBounds().contains(context.mousePos);
    }

    @Override
    public IInteractionListener getInteraction(DisplayInteractionHandler context) {
        return this;
    }
}
