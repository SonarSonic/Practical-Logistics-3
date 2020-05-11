package sonar.logistics.client.gsi.properties;

import sonar.logistics.client.vectors.Quad2D;

public class ScaleableBounds {

    // stored value of percentages for the size of x, y, width, height
    private Quad2D percentages = Quad2D.DEFAULT_PERCENTAGES;

    private Quad2D size;
    private Quad2D renderSize;

    public void setAlignmentPercentages(Quad2D percentages){
        this.percentages = percentages;
    }

    public void build(Quad2D bounds){
        this.size = Quad2D.getQuadFromPercentage(bounds, percentages);
        this.renderSize = size.copy();
    }

    public void build(Quad2D bounds, ScaleableStyling properties){
        this.size = Quad2D.getQuadFromPercentage(bounds, percentages);
        this.renderSize = properties.getRenderSizing(size);
    }

    public Quad2D getBounds(){
        return size;
    }

    public Quad2D getRenderBounds(){
        return renderSize;
    }

}