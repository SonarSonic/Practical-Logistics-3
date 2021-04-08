package sonar.logistics.client.gsi.components.layouts;

import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.util.vectors.Quad2D;

import java.util.List;

public class Layout {

    public static Layout DEFAULT = new Layout();

    public Quad2D getAvailableSize(Quad2D previous, Quad2D bounds) {
        return bounds;
    }

    public void buildComponents(Quad2D bounds, List<Component> components){
        Quad2D previous = new Quad2D(bounds.getX(), bounds.getY(), 0, 0);
        for(Component component : components){
            Quad2D next = getAvailableSize(previous, bounds);
            component.build(next);
            previous = component.getBounds().outerSize();
        }
    }

}
