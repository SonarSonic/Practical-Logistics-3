package sonar.logistics.client.gsi.components.buttons;

import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gsi.api.IInteractableComponent;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.client.gsi.interactions.triggers.ITrigger;

public class ColouredButtonComponent extends AbstractComponent implements IInteractableComponent {

    public ITrigger trigger;
    public int activatedColour = ScreenUtils.transparent_activated_button.rgba;
    public int disabledColour = ScreenUtils.transparent_disabled_button.rgba;
    public int hoveredColour = ScreenUtils.transparent_hovered_button.rgba;

    public ColouredButtonComponent(ITrigger trigger){
        this.trigger = trigger;
    }

    @Override
    public boolean mouseClicked(DisplayInteractionHandler handler, int button) {
        if(isMouseOver(handler)) {
            trigger.trigger(this, handler);
            return true;
        }
        return false;
    }

    @Override
    public void render(GSIRenderContext context, DisplayInteractionHandler handler) {
        super.render(context, handler);
        context.matrix.translate(0,0, -0.0001F);
        int rgba = trigger.isActive(this, handler) ? activatedColour : isMouseOver(handler) ? hoveredColour : disabledColour;
        GSIRenderHelper.renderColouredRect(context, true, bounds.renderBounds(), rgba);
    }
}
