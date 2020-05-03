package sonar.logistics.client.gsi.components;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;
import sonar.logistics.multiparts.displays.old.info.elements.base.ElementAlignment;

import java.util.List;

public class GridComponent extends AbstractStyledScaleable implements IScaleableComponent {

    public List<IRenderableElement> renderableElements;

    public int columns, rows;
    public boolean setCellSize;

    public boolean forceUniform;
    public Vec3d uniformScaling, cellSizing, uniformAlignment;
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

    public void setCellSize(Vec3d cellSizing){
        this.cellSizing = cellSizing;
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
                        element.render(context, new Vec3d((c * cellSizing.getX()) + uniformAlignment.getX(), (r * cellSizing.getY()) + uniformAlignment.getY(), 0), uniformScaling);
                    }else{
                        element.render(context, new Vec3d((c * cellSizing.getX()), (r * cellSizing.getY()), 0), cellSizing);
                    }
                    context.matrix.pop();
                }
            }
        }
    }

    @Override
    public void build(Vec3d alignment, Vec3d maxSizing) {
        super.build(alignment, maxSizing);

        if(setCellSize) {
            this.rows = (int)Math.floor(this.alignment.getRenderSizing().getY()/cellSizing.getY());
            this.columns = (int)Math.floor(this.alignment.getRenderSizing().getX()/cellSizing.getX());
        }else {
            this.cellSizing = this.alignment.getRenderSizing().mul(1D / columns, 1D / rows, 0);
        }

        double uniformScale = Math.min(cellSizing.x, cellSizing.y);
        this.uniformScaling = new Vec3d(uniformScale, uniformScale, 0);
        this.uniformAlignment = DisplayVectorHelper.alignArrayWithin(uniformScaling, cellSizing, xAlign, yAlign, ElementAlignment.LEFT);
    }
}
