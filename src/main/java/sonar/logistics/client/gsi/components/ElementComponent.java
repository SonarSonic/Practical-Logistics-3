package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;

public class ElementComponent extends AbstractStyledScaleable implements IScaleableComponent {

    public IRenderableElement element;

    public ElementComponent(IRenderableElement element) {
        super();
        this.element = element;
    }

    @Override
    public boolean canRender(ScaleableRenderContext context, DisplayInteractionHandler handler) {
        return element.canRender(context);
    }

    @Override
    public void render(ScaleableRenderContext context, DisplayInteractionHandler handler) {
        super.render(context, handler);
        context.matrix.push();
        element.render(context, bounds.renderBounds());
        context.matrix.pop();
    }

    @Override
    public String toString() {
        return element.toString();
    }
}