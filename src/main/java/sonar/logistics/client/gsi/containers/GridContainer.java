package sonar.logistics.client.gsi.containers;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;

public class GridContainer extends AbstractContainer {

    public int columns, rows;
    public boolean setCellSize;

    public Vec3d cellSizing;

    public GridContainer() {
        super();
    }

    public void setGridSize(int columns, int rows){
        this.columns = columns;
        this.rows = rows;
    }

    public void setCellSize(Vec3d cellSizing){
        this.cellSizing = cellSizing;
        this.setCellSize = true;
    }

    @Override
    public void render(ScaleableRenderContext context) {
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
    public void build(Vec3d alignment, Vec3d maxSizing) {
        super.build(alignment, maxSizing);
        if(setCellSize) {
            this.rows = (int)Math.floor(this.alignment.getRenderSizing().getY()/cellSizing.getY());
            this.columns = (int)Math.floor(this.alignment.getRenderSizing().getX()/cellSizing.getX());
        }else {
            this.cellSizing = this.alignment.getRenderSizing().mul(1D / columns, 1D / rows, 0);
        }

        /////set the sizing for each component.
        for(int r = 0; r < rows; r ++) {
            for (int c = 0; c < columns; c++) {
                int pos = (r*columns) + c;
                IScaleableComponent component = subComponents.size() > pos ? subComponents.get(pos) : null;
                if(component != null){
                    Vec3d componentAlignment = alignment.add(new Vec3d((c * cellSizing.getX()), (r * cellSizing.getY()), 0));
                    component.build(componentAlignment, cellSizing);
                }
            }
        }
    }
}
