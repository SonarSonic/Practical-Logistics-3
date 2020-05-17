package sonar.logistics.client.design.gui.interactions;

import net.minecraft.client.gui.IGuiEventListener;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.design.gui.EnumLineStyling;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;

public abstract class AbstractViewportInteraction extends AbstractDraggingInteraction implements IGuiEventListener {

    public GSIViewportWidget viewport;

    public AbstractViewportInteraction(GSIViewportWidget viewport){
        this.viewport = viewport;
    }

    /////

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return true; //the viewport is already checked
    }

    @Override
    public boolean canStartDrag(double mouseX, double mouseY, int button) {
        return super.canStartDrag(mouseX, mouseY, button);
    }

    //////
    public void onGlyphStyleChanged(GlyphStyleAttributes attribute, Object attributeObj){}

    public void onLineStyleChanged(EnumLineStyling styling){}

    public void render(int mouseX, int mouseY, float partialTicks){}

    public void renderScissored(int mouseX, int mouseY, float partialTicks){}
}
