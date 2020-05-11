package sonar.logistics.client.gsi.scaleables;

import sonar.logistics.client.gsi.api.IScaleable;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.vectors.Quad2D;

public abstract class AbstractScaleable implements IScaleable {

    public ScaleableBounds bounds = new ScaleableBounds();

    public AbstractScaleable() {}

    public void setBounds(ScaleableBounds bounds){
        this.bounds = bounds;
    }

    @Override
    public void build(Quad2D bounds) {
        this.bounds.build(bounds);
    }

    @Override
    public ScaleableBounds getBounds() {
        return bounds;
    }

}
