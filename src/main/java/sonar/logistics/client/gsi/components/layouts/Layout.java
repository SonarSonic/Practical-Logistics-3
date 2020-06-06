package sonar.logistics.client.gsi.components.layouts;

import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.vectors.Quad2D;

import java.util.List;

public class Layout {

    public static Layout DEFAULT = new Layout();

    public Quad2D getNextQuad(Quad2D previous, Quad2D bounds) {
        return bounds;
    }

    public void buildComponents(Quad2D bounds, List<IComponent> components){
        Quad2D previous = new Quad2D(bounds.getX(), bounds.getY(), 0, 0);
        for(IComponent component : components){
            Quad2D next = getNextQuad(previous, bounds);
            component.build(next);
            previous = next;
        }
    }

}
