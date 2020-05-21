package sonar.logistics.client.gsi.properties;

import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;

public class AbsoluteBounds extends ComponentBounds {

    public Quad2D size;
    public Quad2D renderSize;

    public AbsoluteBounds(double x, double y, double width, double height){
        this.size = new Quad2D(x, y, width, height);
    }

    public AbsoluteBounds(Quad2D size){
        this.size = size;
    }

    @Override
    public void build(Quad2D bounds, @Nullable ScaleableStyling properties) {
        this.renderSize = properties == null ? size.copy() : properties.getRenderSizing(size);
    }

    @Override
    public Quad2D maxBounds() {
        return size;
    }

    @Override
    public Quad2D renderBounds() {
        return renderSize;
    }
}
