package sonar.logistics.client.gsi.components;

import net.minecraft.client.Minecraft;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

public class ButtonComponent extends TriggerComponent {

    public int uvLeft, uvTop;
    public static int activatedColour = ScreenUtils.transparent_activated_button.rgba;
    public static int hoveredColour = ScreenUtils.transparent_hovered_button.rgba;
    public static int disabledColour = ScreenUtils.transparent_disabled_button.rgba;

    public ButtonComponent(int triggerId, int uvLeft, int uvTop) {
        super(triggerId);
        this.uvLeft = uvLeft;
        this.uvTop = uvTop;
    }

    public int getColour(DisplayInteractionHandler interact){
        if(interact.hovered == this){
            return hoveredColour;
        }
        if(interact.isActive(this, triggerId)){
            return activatedColour;
        }
        return disabledColour;
    }

    @Override
    public void render(ScaleableRenderContext context, DisplayInteractionHandler interact) {
        super.render(context, interact);
        context.matrix.translate(0, 0, -1);
        ScaleableRenderHelper.renderColouredRect(context, bounds.renderBounds(), getColour(interact));
        ScaleableRenderHelper.renderTexturedRect(context, ScaleableRenderHelper.BUTTON_RENDER_TYPE, bounds.renderBounds(), ScreenUtils.white.rgba, uvLeft / 256F, (uvLeft + 16) / 256F, uvTop / 256F, (uvTop + 16) / 256F);
    }
}
