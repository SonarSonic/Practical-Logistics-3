package sonar.logistics.client.gsi.scaleables;

import sonar.logistics.client.gsi.api.IScaleable;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.vectors.Quad2D;

public abstract class AbstractScaleable implements IScaleable {

    public ScaleableBounds alignment = new ScaleableBounds();

    public AbstractScaleable() {}

    public void setAlignment(ScaleableBounds alignment){
        this.alignment = alignment;
    }

    @Override
    public void build(Quad2D bounds) {
        this.alignment.build(bounds);
    }

    @Override
    public ScaleableBounds getAlignment() {
        return alignment;
    }

}
