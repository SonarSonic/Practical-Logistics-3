package sonar.logistics.client.gsi.containers;

import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

public class GridContainer extends AbstractContainer {

    public int columns, rows;
    public boolean setCellSize;

    public Vector2D cellSize;

    public GridContainer() {
        super();
    }

    public void setGridSize(int columns, int rows){
        this.columns = columns;
        this.rows = rows;
    }

    public void setCellSize(Vector2D cellSize){
        this.cellSize = cellSize;
        this.setCellSize = true;
    }

    @Override
    public void render(ScaleableRenderContext context) {
        super.render(context);
        for(int r = 0; r < rows; r ++) {
            for(int c = 0; c < columns; c ++) {
                int pos = (r*columns) + c;
                IScaleableComponent component = subComponents.size() > pos ? subComponents.get(pos) : null;
                if(component != null && component.canRender(context)){
                    context.matrix.push();
                    component.render(context);
                    context.matrix.pop();
                }
            }
        }
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        if(setCellSize) {
            this.rows = (int)Math.floor(alignment.getRenderBounds().getHeight()/ cellSize.getY());
            this.columns = (int)Math.floor(alignment.getRenderBounds().getWidth()/ cellSize.getX());
        }else {
            this.cellSize = this.alignment.getRenderBounds().getSizing().mul(1D / columns, 1D / rows);
        }

        /////set the sizing for each component.
        for(int r = 0; r < rows; r ++) {
            for (int c = 0; c < columns; c++) {
                int pos = (r*columns) + c;
                IScaleableComponent component = subComponents.size() > pos ? subComponents.get(pos) : null;
                if(component != null){
                    Vector2D componentAlignment = alignment.getRenderBounds().getAlignment().add(c * cellSize.getX(), r * cellSize.getY());
                    component.build(new Quad2D(componentAlignment, cellSize));
                }
            }
        }
    }
}
