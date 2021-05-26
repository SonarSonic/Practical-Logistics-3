package sonar.logistics.client.gsi.style;

import sonar.logistics.util.vectors.Quad2F;

public class ComponentBounds {

    //the last host bounds this component was built with.
    public Quad2F hostBounds;

    public Quad2F outerSize;
    public Quad2F innerSize;

    public ComponentBounds(){}

    public void build(Quad2F bounds, ComponentStyling properties){
        this.hostBounds = bounds;
        this.outerSize = StyleHelper.getComponentOuterSize(bounds, properties);
        this.innerSize = StyleHelper.getComponentInnerSize(outerSize, properties);
    }

    ///

    public Quad2F getHostBounds(){
        return hostBounds;
    }

    public void setHostBounds(Quad2F bounds){
        this.hostBounds = bounds;
    }

    ///

    public Quad2F outerSize(){
        return outerSize;
    }

    public Quad2F innerSize(){
        return innerSize;
    }

}