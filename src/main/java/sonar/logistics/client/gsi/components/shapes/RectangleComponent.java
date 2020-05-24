package sonar.logistics.client.gsi.components.shapes;

import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.components.AbstractComponent;

public class RectangleComponent extends AbstractComponent {

    public int rgba;

    public RectangleComponent(int rgba){
        this.rgba = rgba;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        GSIRenderHelper.renderColouredRect(context, true, bounds.renderBounds(), rgba);
    }
}
