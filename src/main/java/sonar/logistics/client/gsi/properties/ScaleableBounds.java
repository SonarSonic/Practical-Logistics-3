package sonar.logistics.client.gsi.properties;

import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;

public class ScaleableBounds extends ComponentBounds {

    // stored value of percentages for the size of x, y, width, height
    private Quad2D percentages = Quad2D.DEFAULT_PERCENTAGES;

    private Quad2D size;
    private Quad2D renderSize;

    public ScaleableBounds(){}

    public ScaleableBounds(double x, double y, double width, double height){
        setBoundPercentages(new Quad2D(x, y, width, height));
    }

    public ScaleableBounds(Quad2D percentages){
        setBoundPercentages(percentages);
    }

    public void setBoundPercentages(Quad2D percentages){
        this.percentages = percentages;
    }

    public void build(Quad2D bounds, @Nullable ComponentStyling properties){
        this.size = Quad2D.getQuadFromPercentage(bounds, percentages);
        this.renderSize = properties == null ? size.copy() : properties.getRenderSizing(size);
    }

    public Quad2D maxBounds(){
        return size;
    }

    public Quad2D renderBounds(){
        return renderSize;
    }

}