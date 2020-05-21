package sonar.logistics.client.design.gui.widgets;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import sonar.logistics.client.design.api.IInteractWidget;
import sonar.logistics.client.design.api.IMultipleGuiEventListener;
import sonar.logistics.client.design.api.ISimpleWidget;
import sonar.logistics.client.vectors.Quad2D;

import java.util.ArrayList;
import java.util.List;

//TODO REPLACE WITH COMPONENT
public class DropdownWidget extends AbstractGui implements IMultipleGuiEventListener, ISimpleWidget {

    public Quad2D quad;
    public List<ISimpleWidget> widgets;
    public List<IInteractWidget> interactables;
    private double yOffset = 0;
    public boolean activated;

    public DropdownWidget(ISimpleWidget host, List<ISimpleWidget> widgets){
        this(host.getQuad().getMaxX(), host.getQuad().getMaxY(), widgets);
    }

    public DropdownWidget(double x, double y, List<ISimpleWidget> widgets){
        this.quad = new Quad2D().setAlignment(x, y);
        this.widgets = widgets;
        this.interactables = new ArrayList<>();
        widgets.stream().filter(w -> w instanceof  IInteractWidget).forEach(w -> interactables.add((IInteractWidget) w));
        widgets.forEach(w -> {
            w.getQuad().x = (int)quad.x;
            w.getQuad().y = (int)(quad.y + quad.height + yOffset);
            yOffset += w.getQuad().height;
        });
    }

    @Override
    public List<? extends IGuiEventListener> getEventListeners() {
        return interactables;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if(activated) {
            widgets.forEach(w -> w.render(mouseX, mouseY, partialTicks));
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(!activated){
            return false;
        }
        return IMultipleGuiEventListener.super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean click = IMultipleGuiEventListener.super.mouseClicked(mouseX, mouseY, button);
        if(click){
            activated = false;
        }
        return click;
    }

    @Override
    public IGuiEventListener getEventListenerAt(double mouseX, double mouseY) {
        if(!activated){
            return null;
        }
        return IMultipleGuiEventListener.super.getEventListenerAt(mouseX, mouseY);
    }

    @Override
    public Quad2D getQuad() {
        return quad;
    }
}
