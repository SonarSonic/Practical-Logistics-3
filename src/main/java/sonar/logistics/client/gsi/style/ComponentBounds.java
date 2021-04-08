package sonar.logistics.client.gsi.style;

import sonar.logistics.util.vectors.Quad2D;

public class ComponentBounds {

    //the last host bounds this component was built with.
    public Quad2D hostBounds;

    public Quad2D outerSize;
    public Quad2D innerSize;

    public ComponentBounds(){}

    public void build(Quad2D bounds, ComponentStyling properties){
        this.hostBounds = bounds;
        this.outerSize = StyleHelper.getComponentOuterSize(bounds, properties);
        this.innerSize = StyleHelper.getComponentInnerSize(outerSize, properties);
    }

    ///

    public Quad2D getHostBounds(){
        return hostBounds;
    }

    public void setHostBounds(Quad2D bounds){
        this.hostBounds = bounds;
    }

    ///

    public Quad2D outerSize(){
        return outerSize;
    }

    public Quad2D innerSize(){
        return innerSize;
    }

}