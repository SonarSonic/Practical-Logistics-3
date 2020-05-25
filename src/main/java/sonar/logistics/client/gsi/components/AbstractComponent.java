package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gsi.properties.ComponentStyling;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

import javax.annotation.Nonnull;

public abstract class AbstractComponent implements IComponent {

    public GSI gsi;
    public ComponentBounds bounds = new ScaleableBounds();
    public ComponentStyling styling = new ComponentStyling();

    public AbstractComponent(){}

    @Override
    public GSI getGSI() {
        return gsi;
    }

    @Override
    public void setGSI(GSI gsi) {
        this.gsi = gsi;
    }

    public GSIInteractionHandler getInteractionHandler(){
        return getGSI().interactionHandler;
    }

    ///

    @Nonnull
    @Override
    public ComponentBounds getBounds() {
        return bounds;
    }

    @Nonnull
    @Override
    public ComponentStyling getStyling() {
        return styling;
    }

    ///

    @Override
    public void setBounds(ComponentBounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public void setStyling(ComponentStyling styling){
        this.styling = styling;
    }

    ///

    @Override
    public void build(Quad2D bounds) {
        this.bounds.build(bounds, styling);
    }

    @Override
    public void render(GSIRenderContext context) {
        GSIRenderHelper.renderBorders(context, bounds, styling);
    }

    ///

    public Vector2D getMousePos(){
        return getInteractionHandler().mousePos;
    }

    public Vector2D getRelativeMousePos(){
        return getMousePos().copy().sub(getBounds().renderBounds().getAlignment());
    }

}
