package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.vectors.Quad2D;

public interface IScaleable {

    ScaleableBounds getAlignment();

    void build(Quad2D bounds);

}
