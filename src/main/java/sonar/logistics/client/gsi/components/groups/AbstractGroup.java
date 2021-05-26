package sonar.logistics.client.gsi.components.groups;

import com.google.common.collect.Lists;
import sonar.logistics.client.gsi.components.text.IComponentHost;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.interactions.api.IInteractionHandler;

import javax.annotation.Nullable;
import java.util.List;

///a group without build or render defined
public class AbstractGroup extends Component implements IComponentHost, IInteractionHandler {

    protected List<Component> subComponents = Lists.newArrayList();

    public AbstractGroup() {}

    @Override
    public boolean isMouseOver(){
        return isVisible && subComponents.stream().anyMatch(Component::isMouseOver);
    }

    @Override
    public void setHost(IComponentHost host) {
        super.setHost(host);
        subComponents.forEach(c -> c.setHost(this));
    }

    @Nullable
    @Override
    public List<Component> getSubComponents() {
        return subComponents;
    }

    public <C extends Component> C addComponent(C component){
        component.setHost(this);
        subComponents.add(component);
        getGSI().nodeGraph.addNode(component);
        return component;
    }

    public <C extends Component> C removeComponent(C component){
        component.setHost(null);
        subComponents.remove(component);
        getGSI().nodeGraph.removeNode(component);
        return component;
    }

    public void toggleVisibility(){
        this.isVisible = !isVisible;
        if(isVisible){
            onOpened();
        }else{
            onClosed();
        }
    }

    public void onOpened(){}

    public void onClosed(){}

    @Override
    public void tick() {
        super.tick();
        subComponents.forEach(Component::tick);
    }
}
