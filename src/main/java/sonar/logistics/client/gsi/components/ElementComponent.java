package sonar.logistics.client.gsi.components;

import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;

public class ElementComponent extends AbstractStyledScaleable {

    public IRenderableElement element;

    public ElementComponent(IRenderableElement element) {
        super();
        this.element = element;
    }

    @Override
    public boolean canRender(ScaleableRenderContext context) {
        return element.canRender(context);
    }

    @Override
    public void render(ScaleableRenderContext context) {
        super.render(context);
        context.matrix.push();
        element.render(context, alignment.getRenderAlignment(), alignment.getRenderSizing());
        context.matrix.pop();
    }

    @Override
    public String toString() {
        return element.toString();
    }
}