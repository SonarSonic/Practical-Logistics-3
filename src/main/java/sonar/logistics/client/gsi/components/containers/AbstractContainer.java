package sonar.logistics.client.gsi.components.containers;

import com.google.common.collect.Lists;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.components.AbstractComponent;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractContainer extends AbstractComponent implements IComponent {

    public List<IComponent> subComponents = Lists.newArrayList();

    public AbstractContainer() {}

    @Override
    public void setGSI(GSI gsi) {
        super.setGSI(gsi);
        subComponents.forEach(c -> c.setGSI(gsi));
    }

    @Nullable
    @Override
    public List<IComponent> getSubComponents() {
        return subComponents;
    }

    public void addComponent(IComponent component){
        subComponents.add(component);
    }

    public void removeComponent(IComponent component){
        subComponents.remove(component);
    }
}
