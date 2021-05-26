package sonar.logistics.client.gsi.components.groups;

import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.components.layouts.ListLayout;
import sonar.logistics.client.gsi.interactions.api.IInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.ComponentBounds;
import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.client.gsi.style.properties.Unit;
import sonar.logistics.util.vectors.Vector2F;

/**
 * TODO make this useable as a dropdown button.
 */
public class HeaderGroup extends LayoutGroup {

    public Component header;
    public Component internal;

    public HeaderGroup(){
        this.setLayout(ListLayout.INSTANCE);
    }

    public HeaderGroup setHeader(Component header){
        this.header = header;
        addComponent(header);
        return this;
    }

    public HeaderGroup setInternal(Component internal){
        this.internal = internal;
        addComponent(internal);
        return this;
    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible){
            GSIRenderHelper.renderComponentBackground(context, bounds, styling);
            GSIRenderHelper.renderComponentBorder(context, bounds, styling);

            GSIRenderHelper.renderComponent(context, header);
            GSIRenderHelper.renderComponent(context, internal);
        }
    }

    @Override
    public boolean isMouseOver() {
        //windows always check their own bounds, to avoid people interacting with things beneath them.
        return isVisible && getBounds().outerSize().contains(getInteractionHandler().mousePos) || subComponents.stream().anyMatch(IInteractionHandler::isMouseOver);
    }

    @Override
    public boolean mouseClicked(int button) {
        //windows always return true, to avoid people interacting with things beneath them.
        super.mouseClicked(button);
        return true;
    }

    public void toggleInternalVisibility(){
        this.internal.isVisible = !internal.isVisible;
    }

    ////moving group -- TODO: make a more universal way of moving windows??

    private Vector2F dragStart;
    private Vector2F oldAlignment;
    private boolean moveDrag;

    public void startDrag(){
        this.moveDrag = true;
        this.getGSI().setDragging(true);
        this.getGSI().setFocused(this);
        onDragStarted(0);
    }

    @Override
    public void onDragStarted(int button) {
        if(moveDrag) {
            //n.b. this assumes the Window has absolute bounds, should we change this? if also allows positions outside of the hosts bounds.
            ComponentBounds bounds = getBounds();
            dragStart = getInteractionHandler().mousePos.copy();
            oldAlignment = new Vector2F(styling.getXPos().getValue(bounds.hostBounds.getWidth()), styling.getYPos().getValue(bounds.hostBounds.getHeight()));
        }else{
            super.onDragStarted(button);
        }
    }

    @Override
    public void onDragFinished(int button) {
        if(moveDrag) {
            dragStart = null;
            oldAlignment = null;
            moveDrag = false;
        }else{
            super.onDragFinished(button);
        }
    }

    @Override
    public boolean mouseDragged() {
        if(moveDrag) {
            styling.setSizing(new LengthProperty(Unit.PIXEL, oldAlignment.x + getMousePos().x - dragStart.x), new LengthProperty(Unit.PIXEL, oldAlignment.y + getMousePos().y - dragStart.y), styling.getWidth(), styling.getHeight());
            rebuild();
            return true;
        }else{
            return super.mouseDragged();
        }
    }
}
