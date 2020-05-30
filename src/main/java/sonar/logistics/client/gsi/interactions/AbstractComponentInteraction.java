package sonar.logistics.client.gsi.interactions;

import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.api.IComponentHost;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.vectors.Vector2D;

public class AbstractComponentInteraction<C extends IComponent> implements IInteractionListener {

    public C component;

    public AbstractComponentInteraction(C component){
        this.component = component;
    }

    @Override
    public boolean isMouseOver() {
        return component.getBounds().maxBounds().contains(getMousePos());
    }

    ///

    public IComponentHost getHost() {
        return component.getHost();
    }

    public GSI getGSI() {
        return component.getGSI();
    }

    public GSIInteractionHandler getInteractionHandler(){
        return getGSI().interactionHandler;
    }

    public Vector2D getMousePos(){
        return getInteractionHandler().mousePos;
    }

}
