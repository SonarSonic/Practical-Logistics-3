package sonar.logistics.client.design.gui.widgets;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.widget.Widget;
import sonar.logistics.client.design.api.IFlexibleGuiEventListener;
import sonar.logistics.client.design.api.IInteractWidget;
import sonar.logistics.client.design.api.IMultipleGuiEventListener;
import sonar.logistics.client.design.api.ISimpleWidget;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DropdownButton extends AbstractGui implements IMultipleGuiEventListener, ISimpleWidget {

    public boolean open = false;
    public List<IInteractWidget> dropDowns;
    public int dropDownHeight;

    public Quad2D quad;

    public DropdownButton(int x, int y, int width, int height, List<IInteractWidget> dropDowns) {
        super();
        this.quad = new Quad2D(x, y, width, height);
        this.dropDowns = dropDowns;
        dropDowns.forEach(d -> {
            d.getQuad().x = (int)quad.x;
            d.getQuad().y = (int)(quad.y + quad.height + dropDownHeight);
            dropDownHeight += d.getQuad().height;
        });
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(IMultipleGuiEventListener.super.isMouseOver(mouseX, mouseY) || quad.contains(mouseX, mouseY)){
            return true;
        }
        open = false;
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(quad.contains(mouseX, mouseY)){
            open = !open;
            return true;
        }
        return IMultipleGuiEventListener.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        ScreenUtils.fillDouble(quad.x, quad.y, quad.x + quad.width, quad.y + quad.height, open ? ScreenUtils.transparent_green_button.rgba : ScreenUtils.transparent_grey_button.rgba);
        if(open){
            dropDowns.forEach(widget -> widget.render(mouseX, mouseY, partialTicks));
        }
    }

    @Override
    public Quad2D getQuad() {
        return quad;
    }

    @Override
    public List<? extends IGuiEventListener> getEventListeners() {
        return open ? dropDowns : new ArrayList<>();
    }
}
