package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.components.text.IComponentHost;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.interactions.api.IInteractionHandler;
import sonar.logistics.client.gsi.interactions.main.EmptyInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.style.ComponentStyling;
import sonar.logistics.client.gsi.style.ComponentBounds;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.util.vectors.Quad2D;
import sonar.logistics.util.vectors.Vector2D;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A component is a user interface widget that can be rendered in the world or in a gui
 */
public abstract class Component implements IInteractionHandler {

    public transient IComponentHost host;

    public ComponentBounds bounds = new ComponentBounds();
    public ComponentStyling styling = new ComponentStyling();

    public boolean isVisible = true;

    public Component(){}

    /**the host of the component, in most cases this will be the gsi, used primarily with interactions*/
    public IComponentHost getHost() {
        return host;
    }

    /**should always be called before using the component, and is typically called when it is added to the host*/
    public void setHost(IComponentHost host) {
        this.host = host;
    }

    ///

    /**the bounds are used for finding the position / size of the component
     * the bounds will always be relative to the GSI, which means each component can be rendered independently of any sub containers*/
    @Nonnull
    public ComponentBounds getBounds() {
        return bounds;
    }

    /**the components styling defines default text colours / background colours / margin widths etc.*/
    @Nonnull
    public ComponentStyling getStyling() {
        return styling;
    }

    ///

    public void setBounds(ComponentBounds bounds) {
        this.bounds = bounds;
    }

    public void setStyling(ComponentStyling styling){
        this.styling = styling;
    }

    ///

    /**this method "builds" the layout for the component based on the bounds passed to it, it should always be called before interacting with the component
     * the bounds are passed by the "host" - typically a GSI but could also be passed by containers like GridContainer / FloatingContainer
     * this method should set the ComponentBounds, keeping the x, y alignment of the passed "host's" bounds*/
    public void build(Quad2D bounds) {
        this.bounds.build(bounds, styling);
    }

    /**triggers a localised rebuild, used if the component is edited, it will rebuild with the last known host bounds, this will only perform a rebuild if the component has been built previously
     * if z layer has also changed it is advisable to rebuild the entire GSI instead. */
    public void rebuild(){
        if(getBounds().getHostBounds() != null) {
            build(getBounds().getHostBounds());
        }
    }

    /**this method will be called by the "host" and is used for rendering the component within the bounds setup, should only be called if {@link #isVisible} returns true*/
    public void render(GSIRenderContext context) {
        GSIRenderHelper.renderComponentBackground(context, bounds, styling);
        GSIRenderHelper.renderComponentBorder(context, bounds, styling);
    }

    /**called every game tick, client side*/
    public void tick(){}

    ///

    public boolean isVisible(){
        return isVisible;
    }

    public boolean canInteract(){
        return isVisible();
    }


    /**this is used if the component is itself a "host"
     * sub components should be rendered by the component that declares them.
     * sub components interactions are still handled by the GSI
     * if a component has sub components only it's sub components will be interacted with, it will be bypassed*/
    @Nullable
    public List<Component> getSubComponents(){
        return null;
    }

    /// helper methods

    public GSI getGSI(){
        return getHost().getGSI();
    }

    public GSIInteractionHandler getInteractionHandler(){
        return getGSI().interactionHandler;
    }

    public Vector2D getMousePos(){
        return getInteractionHandler().mousePos;
    }

    public Vector2D getRelativeMousePos(){
        return getMousePos().copy().sub(getBounds().innerSize().getAlignment());
    }

    public boolean isMouseOver() {
        return getBounds().outerSize().contains(getMousePos());
    }

    public boolean isFocusedComponent(){
        return getGSI().getFocusedListener().filter(listener -> listener == this).isPresent();
    }

    public boolean isDraggedComponent(){
        return isFocusedComponent() && getGSI().isDragging();
    }

    public boolean isHoveredComponent(){
        return getGSI().getHoveredListener().filter(listener -> listener == this).isPresent();
    }

    public boolean isMoveable(){
        return false;
    }

}
