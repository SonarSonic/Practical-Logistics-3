package sonar.logistics.client.gsi.components.layouts;

import sonar.logistics.util.vectors.Quad2F;

public class CellLayout extends Layout {

    public Quad2F cellSize;

    public CellLayout(Quad2F cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    public Quad2F getAvailableSize(Quad2F previous, Quad2F bounds) {
        if(bounds.canFit(previous.getMaxX() - bounds.getX() + cellSize.getWidth(), previous.getMaxY() - bounds.getY() + cellSize.getWidth())){
            return cellSize.copy().translate(previous.getMaxX(), previous.getY());
        }
        return cellSize.copy().translate(bounds.getX(), previous.getMaxY());
    }
}
