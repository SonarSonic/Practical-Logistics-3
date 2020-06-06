package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.api.IComponentHost;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.style.ComponentStyling;
import sonar.logistics.client.gsi.style.ComponentBounds;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class AbstractComponent implements IComponent {

    public IComponentHost host;
    public ComponentBounds bounds = new ComponentBounds();
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
    public void rebuild(){
        if(getBounds().getHostBounds() != null) {
            build(getBounds().getHostBounds());
        }
    }

    @Override
    public void render(GSIRenderContext context) {
        GSIRenderHelper.renderComponentBackground(context, bounds, styling);
        GSIRenderHelper.renderComponentBorder(context, bounds, styling);
    }

    ///


}
