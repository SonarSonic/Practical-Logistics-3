package sonar.logistics.client.gsi.components.layouts;

import sonar.logistics.util.vectors.Quad2D;

public class ListLayout extends Layout {

    public static ListLayout INSTANCE = new ListLayout();

    @Override
    public Quad2D getAvailableSize(Quad2D previous, Quad2D bounds) {
        return new Quad2D(bounds.getX(), previous.getMaxY(), bounds.getWidth(), bounds.getHeight() - (previous.getMaxY() - bounds.getY()));
    }
}
