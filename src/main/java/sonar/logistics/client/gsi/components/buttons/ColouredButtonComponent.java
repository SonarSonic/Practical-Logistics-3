package sonar.logistics.client.gsi.components.buttons;

import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.interactions.triggers.ITrigger;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gui.ScreenUtils;

import javax.annotation.Nullable;

public class ColouredButtonComponent extends Component implements IInteractionListener {

    @Nullable
    public ITrigger<ColouredButtonComponent> trigger;

    public ColouredButtonComponent(){}

    public ColouredButtonComponent(ITrigger<ColouredButtonComponent> trigger){
        this.trigger = trigger;
    }

    public ColouredButtonComponent setTrigger(@Nullable ITrigger<ColouredButtonComponent> trigger) {
        this.trigger = trigger;
        return this;
    }

    public ColouredButtonComponent setColours(int enabled, int disabled, int hovered){
        styling.setEnabledTextColour(new ColourProperty(enabled));
        styling.setDisabledTextColour(new ColourProperty(disabled));
        styling.setHoveredTextColour(new ColourProperty(hovered));
        return this;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        context.matrix.translate(0,0, -0.0001F);

        boolean isEnabled = trigger != null && trigger.isActive(this, getInteractionHandler());
        ColourProperty rgba = (isEnabled ? styling.getEnabledTextColour() : isMouseOver() ? styling.getHoveredTextColour() : styling.getDisabledTextColour());
        rgba = rgba != null ? rgba : (isEnabled ? ScreenUtils.transparent_green_button : isMouseOver() ? ScreenUtils.transparent_hovered_button : ScreenUtils.transparent_disabled_button);
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
