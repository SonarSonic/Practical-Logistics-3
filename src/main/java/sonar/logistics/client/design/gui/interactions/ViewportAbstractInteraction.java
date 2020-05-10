package sonar.logistics.client.design.gui.interactions;

import sonar.logistics.client.design.gui.GSIDesignSettings;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;

public abstract class ViewportAbstractInteraction extends AbstractDraggingInteraction {

    public GSIViewportWidget viewport;

    public ViewportAbstractInteraction(GSIViewportWidget viewport){
        this.viewport = viewport;
    }



    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return viewport.isMouseOverViewport(mouseX, mouseY);
    }

    @Override
    public boolean canStartDrag(double mouseX, double mouseY, int button) {
        return super.canStartDrag(mouseX, mouseY, button) && viewport.isMouseOverViewport(mouseX, mouseY);
    }



    public abstract GSIDesignSettings.ViewportInteractSetting getViewportSetting();

    public void render(int mouseX, int mouseY, float partialTicks){}

    public void renderScissored(int mouseX, int mouseY, float partialTicks){}
}
