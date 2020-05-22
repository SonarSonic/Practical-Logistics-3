package sonar.logistics.client.gsi.properties;

import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;

public abstract class ComponentBounds {

    public abstract void build(Quad2D bounds, @Nullable ComponentStyling properties);

    public abstract Quad2D maxBounds();

    public abstract Quad2D renderBounds();
}
