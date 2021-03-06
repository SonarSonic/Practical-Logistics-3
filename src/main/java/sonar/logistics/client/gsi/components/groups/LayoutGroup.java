package sonar.logistics.client.gsi.components.groups;

import sonar.logistics.client.gsi.components.layouts.Layout;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.util.vectors.Quad2F;

public class LayoutGroup extends AbstractGroup {

    public Layout layout = Layout.DEFAULT;

    public LayoutGroup setLayout(Layout layout) {
        this.layout = layout;
        return this;
    }

    @Override
    public void build(Quad2F bounds) {
        super.build(bounds);
        layout.buildComponents(getBounds().innerSize(), subComponents);
    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible) {
            super.render(context);
            subComponents.forEach(c -> GSIRenderHelper.renderComponent(context, c));
        }
    }
}
