package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.vectors.Quad2D;

public interface IScaleable {

    ComponentBounds getBounds();

    void setBounds(ComponentBounds bounds);

    void build(Quad2D bounds);

}
