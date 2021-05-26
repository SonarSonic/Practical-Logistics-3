package sonar.logistics.client.gsi.components.layouts;

import sonar.logistics.util.vectors.Quad2F;

public class ListLayout extends Layout {

    public static ListLayout INSTANCE = new ListLayout();

    @Override
    public Quad2F getAvailableSize(Quad2F previous, Quad2F bounds) {
        return new Quad2F(bounds.getX(), previous.getMaxY(), bounds.getWidth(), bounds.getHeight() - (previous.getMaxY() - bounds.getY()));
    }
}
