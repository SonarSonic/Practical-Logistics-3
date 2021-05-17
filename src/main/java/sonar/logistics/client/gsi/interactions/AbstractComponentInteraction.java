package sonar.logistics.client.gsi.interactions;

import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.components.text.IComponentHost;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.interactions.api.IInteractionHandler;
import sonar.logistics.util.vectors.Vector2D;

public class AbstractComponentInteraction<C extends Component> implements IInteractionHandler {

    public C component;

    public AbstractComponentInteraction(C component){
        this.component = component;
    }

    @Override
    public boolean isMouseOver() {
        return component.getBounds().outerSize().contains(getMousePos());
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
