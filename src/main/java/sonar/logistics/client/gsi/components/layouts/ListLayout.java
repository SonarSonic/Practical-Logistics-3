package sonar.logistics.client.gsi.components.layouts;

import sonar.logistics.client.vectors.Quad2D;

public class ListLayout extends Layout {

    public static ListLayout INSTANCE = new ListLayout();

    @Override
    public Quad2D getNextQuad(Quad2D previous, Quad2D bounds) {
        return new Quad2D(bounds.getX(), previous.getY(), bounds.getWidth(), bounds.getHeight() - previous.getY());
    }
}
