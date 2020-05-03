package sonar.logistics.client.gsi.containers;

import com.google.common.collect.Lists;
import sonar.logistics.client.gsi.api.IScaleable;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.properties.ScaleableAlignment;
import sonar.logistics.client.gsi.properties.ScaleableStyling;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractContainer extends AbstractStyledScaleable implements IScaleableComponent {

    public List<IScaleableComponent> subComponents = Lists.newArrayList();

    public AbstractContainer() {}

    @Nullable
    @Override
    public List<IScaleableComponent> getSubComponents() {
        return subComponents;
    }

    public void addComponent(IScaleableComponent component){
        subComponents.add(component);
    }

    public void removeComponent(IScaleableComponent component){
        subComponents.remove(component);
    }
}
