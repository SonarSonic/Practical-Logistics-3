package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;
import sonar.logistics.multiparts.displays.old.info.elements.base.ElementAlignment;

import java.util.List;

public class GridComponent extends AbstractStyledScaleable implements IScaleableComponent {

    public List<IRenderableElement> renderableElements;

    public int columns, rows;
    public boolean setCellSize;

    public boolean forceUniform;
    public Vector2D uniformScaling, cellSize, uniformAlignment;
    public ElementAlignment xAlign = ElementAlignment.CENTERED, yAlign = ElementAlignment.CENTERED;

    public GridComponent() {
        super();
    }

    public void setRenderableElements(List<IRenderableElement> renderableElements){
        this.renderableElements = renderableElements;
    }

    public void setGridSize(int columns, int rows){
        this.columns = columns;
        this.rows = rows;
    }

    public void setCellSize(Vector2D cellSizing){
        this.cellSize = cellSizing;
        this.setCellSize = true;
    }

    public void setForceUniform(boolean forceUniform){
        this.forceUniform = forceUniform;
    }

    @Override
    public void render(ScaleableRenderContext context) {
        super.render(context);
        for(int r = 0; r < rows; r ++) {
            for(int c = 0; c < columns; c ++) {
                int pos = (r * columns) + c;
                IRenderableElement element = renderableElements.size() > pos ? renderableElements.get(pos) : null;
                if(element != null && element.canRender(context)){
                    context.matrix.push();
                    if(forceUniform || element.isUniformScaling(context)) {
                        element.render(context, new Quad2D((c * cellSize.getX()) + uniformAlignment.getX(), (r * cellSize.getY()) + uniformAlignment.getY(), uniformScaling.getX(), uniformScaling.getY()));
                    }else{
                        element.render(context, new Quad2D(c * cellSize.getX(), r * cellSize.getY(), cellSize.getX(), cellSize.getY()));
                    }
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

        double uniformScale = Math.min(cellSize.x, cellSize.y);
        this.uniformScaling = new Vector2D(uniformScale, uniformScale);
        this.uniformAlignment = Vector2D.align(uniformScaling, cellSize, xAlign, yAlign);
    }
}
