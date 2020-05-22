package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gsi.properties.ComponentStyling;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.vectors.Quad2D;

public abstract class AbstractComponent implements IComponent {

    public ComponentBounds bounds = new ScaleableBounds();
    public ComponentStyling styling = new ComponentStyling();

    public AbstractComponent(){}

    @Override
    public ComponentBounds getBounds() {
        return bounds;
    }

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
    public void render(GSIRenderContext context, DisplayInteractionHandler handler) {
        GSIRenderHelper.renderBorders(context, bounds, styling);
    }
}
