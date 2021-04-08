package sonar.logistics.client.gsi.components.groups;

import com.google.common.collect.Lists;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.api.IComponentHost;
import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.interactions.api.INestedInteractionListener;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

///a group without build or render defined
public class AbstractGroup extends AbstractComponent implements IComponent, IComponentHost, INestedInteractionListener {

    protected List<IComponent> subComponents = Lists.newArrayList();
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
    public List<IComponent> getSubComponents() {
        return subComponents;
    }


    public <C extends IComponent> C addComponent(C component){
        component.setHost(this);
        subComponents.add(component);
        if(component instanceof  IInteractionListener){
            interactions.add((IInteractionListener)component);
        }
        return component;
    }

    public <C extends IComponent> C removeComponent(C component){
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
        subComponents.forEach(IComponent::tick);
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
