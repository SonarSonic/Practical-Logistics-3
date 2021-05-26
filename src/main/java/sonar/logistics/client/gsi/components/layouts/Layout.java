package sonar.logistics.client.gsi.components.layouts;

import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.util.vectors.Quad2F;

import java.util.List;

public class Layout {

    public static Layout DEFAULT = new Layout();

    public Quad2F getAvailableSize(Quad2F previous, Quad2F bounds) {
        return bounds;
    }

    public void buildComponents(Quad2F bounds, List<Component> components){
        Quad2F previous = new Quad2F(bounds.getX(), bounds.getY(), 0, 0);
        for(Component component : components){
            Quad2F next = getAvailableSize(previous, bounds);
            component.build(next);
            previous = component.getBounds().outerSize();
        }
    }

}
