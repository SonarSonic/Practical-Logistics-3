package sonar.logistics.client.gsi.components.groups;

import com.google.common.collect.Lists;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.style.ComponentBounds;
import sonar.logistics.client.gsi.style.StyleHelper;
import sonar.logistics.client.gsi.style.properties.UnitLength;
import sonar.logistics.client.gsi.style.properties.UnitType;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

import java.util.List;

public class HeaderGroup extends AbstractGroup {

    public IComponent header;
    public IComponent internal;

    public boolean isInternalVisible = true;

    public HeaderGroup setHeader(IComponent header){
        this.header = header;
        addComponent(header);
        return this;
    }

    public HeaderGroup setInternal(IComponent internal){
        this.internal = internal;
        addComponent(internal);
        return this;
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        Quad2D offsetBounds = getBounds().innerSize().copy().setHeight(Double.MAX_VALUE); //TODO get max size..?
        subComponents.forEach(c -> {c.build(offsetBounds.copy()); offsetBounds.translate(0, c.getBounds().outerSize().height);});

        ///manually resize the element by the height of the last bounds TODO make this automatic, flexible container?
        getBounds().outerSize().height = offsetBounds.getY() - getBounds().innerSize().y + StyleHelper.getHeightOffset(getBounds().outerSize(), styling);
        getBounds().innerSize = StyleHelper.getComponentInnerSize(getBounds().outerSize(), styling);

    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible){
            super.render(context);
            header.render(context);

            if(isInternalVisible) {
                internal.render(context);
            }
        }
    }

    @Override
    public boolean isMouseOver() {
        //windows always check their own bounds, to avoid people interacting with things beneath them.
        return isVisible && getBounds().outerSize().contains(getInteractionHandler().mousePos) || interactions.stream().anyMatch(IInteractionListener::isMouseOver);
    }

    @Override
    public boolean mouseClicked(int button) {
        //windows always return true, to avoid people interacting with things beneath them.
        super.mouseClicked(button);
        return true;
    }

    public void toggleInternalVisibility(){
        this.isInternalVisible = !isInternalVisible;
    }

    @Override
    public List<IInteractionListener> getChildren() {
        return isInternalVisible ? super.getChildren() : header instanceof IInteractionListener ?  Lists.newArrayList((IInteractionListener) header) : Lists.newArrayList();
    }

    ////moving group -- TODO: make a more universal way of moving windows.

    private Vector2D dragStart;
    private Vector2D oldAlignment;
    private boolean moveDrag;

    public void startDrag(){
        this.moveDrag = true;
        this.getHost().setDragging(true);
        this.getHost().setFocused(this);
        onDragStarted(0);
    }

    @Override
    public void onDragStarted(int button) {
        if(moveDrag) {
            //n.b. this assumes the Window has absolute bounds, should we change this? if also allows positions outside of the hosts bounds.
            ComponentBounds bounds = getBounds();
            dragStart = getInteractionHandler().mousePos.copy();
            oldAlignment = new Vector2D(styling.getXPos().getValue(bounds.hostBounds.getWidth()), styling.getYPos().getValue(bounds.hostBounds.getHeight()));
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
            styling.setSizing(new UnitLength(UnitType.PIXEL, oldAlignment.x + getMousePos().x - dragStart.x), new UnitLength(UnitType.PIXEL, oldAlignment.y + getMousePos().y - dragStart.y), styling.getWidth(), styling.getHeight());
            rebuild();
            return true;
        }else{
            return super.mouseDragged();
        }
    }
}
