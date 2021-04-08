package sonar.logistics.client.gsi.components.groups;

import com.google.common.collect.Lists;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.components.text.IComponentHost;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.interactions.api.INestedInteractionListener;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

///a group without build or render defined
public class AbstractGroup extends Component implements IComponentHost, INestedInteractionListener {

    protected List<Component> subComponents = Lists.newArrayList();
    protected List<IInteractionListener> interactions = new ArrayList<>();

    public boolean isVisible = true;

    public AbstractGroup() {}

    @Override
    public boolean isMouseOver(){
        return isVisible && interactions.stream().anyMatch(IInteractionListener::isMouseOver);
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
        if(component instanceof  IInteractionListener){
            interactions.add((IInteractionListener)component);
        }
        return component;
    }

    public <C extends Component> C removeComponent(C component){
        component.setHost(null);
        subComponents.remove(component);
        if(component instanceof IInteractionListener){
            interactions.remove(component);
        }
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

    public void onOpened(){

    }

    public void onClosed(){

    }

    @Override
    public void tick() {
        super.tick();
        subComponents.forEach(Component::tick);
    }

    ///

    private IInteractionListener focused = null;

    @Override
    public List<IInteractionListener> getChildren() {
        return isVisible ? interactions : new ArrayList<>();
    }

    @Nullable
    @Override
    public Optional<IInteractionListener> getFocusedListener() {
        return Optional.ofNullable(isVisible ? focused : null);
    }

    @Override
    public void setFocused(IInteractionListener listener) {
        focused = listener;
    }

    ///

    private boolean isDragging = false;

    @Override
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    @Override
    public GSI getGSI() {
        return host.getGSI();
    }
}
