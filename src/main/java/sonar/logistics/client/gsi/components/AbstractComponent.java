package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.api.IComponentHost;
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

    public IComponentHost host;
    public ComponentBounds bounds = new ScaleableBounds();
    public ComponentStyling styling = new ComponentStyling();

    public AbstractComponent(){}

    @Override
    public IComponentHost getHost() {
        return host;
    }

    @Override
    public void setHost(IComponentHost host) {
        this.host = host;
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
        GSIRenderHelper.renderColouredRect(context, true, bounds.maxBounds(), styling.bgdColour.rgba);
        GSIRenderHelper.pushLayerOffset(context, 1);
        GSIRenderHelper.renderBorders(context, bounds, styling);
        GSIRenderHelper.pushLayerOffset(context, 1);
    }


}
