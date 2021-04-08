package sonar.logistics.client.gsi.components.buttons;

import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.interactions.triggers.ITrigger;

public class IconButtonComponent extends ColouredButtonComponent {

    public EnumButtonIcons icon;

    public IconButtonComponent(EnumButtonIcons icon, ITrigger trigger) {
        super(trigger);
        this.icon = icon;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        context.matrix.translate(0,0, -0.0001F);
        GSIRenderHelper.renderTexturedRect(context, GSIRenderHelper.BUTTON_RENDER_TYPE, false, bounds.innerSize(), ScreenUtils.white.rgba, icon.getUVLeft() / 256F, (icon.getUVLeft() + icon.getWidth()) / 256F, icon.getUVTop() / 256F, (icon.getUVTop() + icon.getHeight()) / 256F);
    }

}
