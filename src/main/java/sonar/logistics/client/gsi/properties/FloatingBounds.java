package sonar.logistics.client.gsi.properties;

import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;

//bounds which position the object based on percentage but maintain it's size
public class FloatingBounds extends ScaleableBounds {

    public FloatingBounds() {
        super();
    }

    public FloatingBounds(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void build(Quad2D bounds, @Nullable ComponentStyling properties){
        super.build(bounds, properties);
        this.size = Quad2D.getQuadFromPercentage(bounds, percentages).setSizing(percentages.width, percentages.height);
        this.renderSize = properties == null ? size.copy() : properties.getRenderSizing(size);
    }
}
