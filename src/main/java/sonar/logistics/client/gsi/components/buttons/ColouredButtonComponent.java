package sonar.logistics.client.gsi.components.buttons;

import sonar.logistics.client.gsi.interactions.api.IInteractionComponent;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.client.gsi.interactions.triggers.ITrigger;

public class ColouredButtonComponent extends AbstractComponent implements IInteractionComponent {


    public ITrigger trigger;
    public int activatedColour = ScreenUtils.transparent_activated_button.rgba;
    public int disabledColour = ScreenUtils.transparent_disabled_button.rgba;
    public int hoveredColour = ScreenUtils.transparent_hovered_button.rgba;

    public ColouredButtonComponent(ITrigger trigger){
        this.trigger = trigger;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        context.matrix.translate(0,0, -0.0001F);
        int rgba = trigger.isActive(this, getInteractionHandler()) ? activatedColour : isMouseOver() ? hoveredColour : disabledColour;
        GSIRenderHelper.renderColouredRect(context, true, bounds.renderBounds(), rgba);
    }

    @Override
    public boolean mouseClicked(int button) {
        trigger.trigger(this, getInteractionHandler());
        return true;
    }
}
