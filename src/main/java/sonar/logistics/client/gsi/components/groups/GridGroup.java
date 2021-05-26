package sonar.logistics.client.gsi.components.groups;

import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.util.vectors.Quad2F;
import sonar.logistics.util.vectors.Vector2F;

public class GridGroup extends AbstractGroup {

    public int columns, rows;
    public boolean setCellSize;

    public Vector2F cellSize;

    public GridGroup() {
        super();
    }

    @Override
    public boolean isMouseOver() {
        return getBounds().outerSize().contains(getInteractionHandler().mousePos);
    }

    public GridGroup setGridSize(int columns, int rows){
        this.columns = columns;
        this.rows = rows;
        return this;
    }

    public GridGroup setCellSize(Vector2F cellSize){
        this.cellSize = cellSize;
        this.setCellSize = true;
        return this;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        for(int r = 0; r < rows; r ++) {
            for(int c = 0; c < columns; c ++) {
                int pos = (r*columns) + c;
                Component component = subComponents.size() > pos ? subComponents.get(pos) : null;
                if(component != null){
                    context.matrix.push();
                    GSIRenderHelper.renderComponent(context, component);
                    context.matrix.pop();
                }
            }
        }
    }

    @Override
    public void build(Quad2F bounds) {
        super.build(bounds);
        if(setCellSize) {
            this.rows = (int)Math.floor(this.bounds.innerSize().getHeight()/ cellSize.getY());
            this.columns = (int)Math.floor(this.bounds.innerSize().getWidth()/ cellSize.getX());
        }else {
            this.cellSize = this.bounds.innerSize().getSizing().mul(1F / columns, 1F / rows);
        }

        /////set the sizing for each component.
        for(int r = 0; r < rows; r ++) {
            for (int c = 0; c < columns; c++) {
                int pos = (r*columns) + c;
                Component component = subComponents.size() > pos ? subComponents.get(pos) : null;
                if(component != null){
                    Vector2F componentAlignment = this.bounds.innerSize().getAlignment().add(c * cellSize.getX(), r * cellSize.getY());
                    component.build(new Quad2F(componentAlignment, cellSize));
                }
            }
        }
    }
}
