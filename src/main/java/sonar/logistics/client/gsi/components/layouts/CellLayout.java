package sonar.logistics.client.gsi.components.layouts;

import sonar.logistics.util.vectors.Quad2D;

public class CellLayout extends Layout {

    public Quad2D cellSize;

    public CellLayout(Quad2D cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    public Quad2D getAvailableSize(Quad2D previous, Quad2D bounds) {
        if(bounds.canFit(previous.getMaxX() - bounds.getX() + cellSize.getWidth(), previous.getMaxY() - bounds.getY() + cellSize.getWidth())){
            return cellSize.copy().translate(previous.getMaxX(), previous.getY());
        }
        return cellSize.copy().translate(bounds.getX(), previous.getMaxY());
    }
}
