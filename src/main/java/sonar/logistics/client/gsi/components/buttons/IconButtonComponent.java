package sonar.logistics.client.gsi.components.buttons;

import net.minecraft.client.Minecraft;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;
import sonar.logistics.client.gsi.triggers.ITrigger;

public class IconButtonComponent extends ColouredButtonComponent {

    public EnumButtonIcons icon;

    public IconButtonComponent(EnumButtonIcons icon, ITrigger trigger) {
        super(trigger);
        this.icon = icon;
    }

    @Override
    public void render(ScaleableRenderContext context, DisplayInteractionHandler handler) {
        super.render(context, handler);
        context.matrix.translate(0,0, -0.0001F);
        ScaleableRenderHelper.renderTexturedRect(context, ScaleableRenderHelper.BUTTON_RENDER_TYPE, false, bounds.renderBounds(), ScreenUtils.white.rgba, icon.getUVLeft() / 256F, (icon.getUVLeft() + icon.getWidth()) / 256F, icon.getUVTop() / 256F, (icon.getUVTop() + icon.getHeight()) / 256F);
    }

}
