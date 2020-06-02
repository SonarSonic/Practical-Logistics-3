package sonar.logistics.client.gsi.properties;

import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;

public abstract class ComponentBounds {

    //used in ray tracing higher z layer will be clicked first etc.
    private int zLayer = 0;
    public Quad2D hostBounds;

    public void build(Quad2D bounds, @Nullable ComponentStyling properties){
        this.hostBounds = bounds;
    }

    public Quad2D getHostBounds(){
        return hostBounds;
    }

    public void setHostBounds(Quad2D bounds){
        this.hostBounds = bounds;
    }

    public abstract Quad2D maxBounds();

    public abstract Quad2D renderBounds();

    public int getZLayer(){
        return zLayer;
    }

    public void setZLayer(int zLayer) {
        this.zLayer = zLayer;
    }
}
