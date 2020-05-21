package sonar.logistics.client.gsi.scaleables;

import sonar.logistics.client.gsi.api.IScaleable;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.vectors.Quad2D;

public abstract class AbstractScaleable implements IScaleable {

    public ComponentBounds bounds = new ScaleableBounds();

    public AbstractScaleable() {}

    @Override
    public void setBounds(ComponentBounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public void build(Quad2D bounds) {
        this.bounds.build(bounds, null);
    }

    @Override
    public ComponentBounds getBounds() {
        return bounds;
    }

}
