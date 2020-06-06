package sonar.logistics.client.gsi.components.groups;

import sonar.logistics.client.gsi.api.ComponentAlignment;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.style.StyleHelper;
import sonar.logistics.client.vectors.Quad2D;

//TODO this list isn't allowed to have a QUAD2D size at the moment, it should be able to limit...
public class BasicList extends AbstractGroup {

    @Override
    public boolean isMouseOver() {
        return interactions.stream().anyMatch(IInteractionListener::isMouseOver);
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        Quad2D offsetBounds = getBounds().innerSize().copy().setHeight(Double.MAX_VALUE); //TODO get max size..?
        subComponents.forEach(c -> {c.build(offsetBounds.copy()); offsetBounds.translate(0, c.getBounds().outerSize().height);});

        ///manually resize the element by the height of the last bounds TODO make this automatic, flexible container?
        getBounds().outerSize().height = offsetBounds.getY() - getBounds().outerSize().y + StyleHelper.getHeightOffset(getBounds().outerSize(), styling);
        getBounds().innerSize = StyleHelper.getComponentInnerSize(getBounds().outerSize(), styling);

    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible) {
            super.render(context);
            subComponents.forEach(e -> e.render(context));
        }
    }

}
