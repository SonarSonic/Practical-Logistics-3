package sonar.logistics.client.gsi.components.basic;

import sonar.logistics.client.gsi.elements.IRenderableElement;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.components.Component;

public class ElementComponent extends Component {

    public IRenderableElement element;

    public ElementComponent(IRenderableElement element) {
        super();
        this.element = element;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        if(!element.canRender(context)){
            return;
        }
        context.matrix.push();
        element.render(context, bounds.innerSize());
        context.matrix.pop();
    }

    @Override
    public String toString() {
        return element.toString();
    }
}