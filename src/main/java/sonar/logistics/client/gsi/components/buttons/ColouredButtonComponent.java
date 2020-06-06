package sonar.logistics.client.gsi.components.buttons;

import sonar.logistics.client.gsi.interactions.api.IInteractionComponent;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.client.gsi.interactions.triggers.ITrigger;

import javax.annotation.Nullable;

public class ColouredButtonComponent extends AbstractComponent implements IInteractionComponent {

    @Nullable
    public ITrigger<ColouredButtonComponent> trigger;
    public int activatedColour = ScreenUtils.transparent_green_button.rgba;
    public int disabledColour = ScreenUtils.transparent_disabled_button.rgba;
    public int hoveredColour = ScreenUtils.transparent_hovered_button.rgba;

    public ColouredButtonComponent(){}

    public ColouredButtonComponent(ITrigger<ColouredButtonComponent> trigger){
        this.trigger = trigger;
    }

    public ColouredButtonComponent setTrigger(@Nullable ITrigger<ColouredButtonComponent> trigger) {
        this.trigger = trigger;
        return this;
    }

    public ColouredButtonComponent setColours(int activated, int disabled, int hovered){
        activatedColour = activated;
        disabledColour = disabled;
        hoveredColour = hovered;
        return this;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        context.matrix.translate(0,0, -0.0001F);
        int rgba = trigger != null && trigger.isActive(this, getInteractionHandler()) ? activatedColour : isMouseOver() ? hoveredColour : disabledColour;
        GSIRenderHelper.renderColouredRect(context, true, bounds.innerSize(), rgba);
    }

    @Override
    public boolean mouseClicked(int button) {
        if(isMouseOver() && trigger != null) {
            trigger.trigger(this, getInteractionHandler());
            return true;
        }
        return false;
    }

}
