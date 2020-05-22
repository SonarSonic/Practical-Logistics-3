package sonar.logistics.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IGuiEventListener;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gui.api.IInteractWidget;
import sonar.logistics.client.gui.api.IMultipleGuiEventListener;
import sonar.logistics.client.gui.api.ISimpleWidget;
import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class PopUpWidget extends SimpleWidget implements IMultipleGuiEventListener {

    public List<ISimpleWidget> simpleWidgets = new ArrayList<>();
    public List<IInteractWidget> interactWidgets = new ArrayList<>();
    public double zOffset;
    public boolean isVisible;

    public PopUpWidget(double x, double y, double w, double h, double zOffset) {
        super(x, y, w, h);
        this.zOffset = zOffset;
    }

    public PopUpWidget(Quad2D quad, double zOffset) {
        super(quad);
        this.zOffset = zOffset;
    }

    public void addWidget(ISimpleWidget widget){
        simpleWidgets.add(widget);
        if(widget instanceof IInteractWidget){
            interactWidgets.add((IInteractWidget) widget);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
       // if(!isVisible){
        //    return;
       // }
        RenderSystem.translated(0, 0, zOffset);
        ScreenUtils.fillDouble(quad, ScreenUtils.transparent_grey_bgd.rgba);
        simpleWidgets.forEach(w -> w.render(mouseX, mouseY, partialTicks));
        RenderSystem.translated(0, 0, -zOffset);
    }

    @Nullable
    @Override
    public IGuiEventListener getEventListenerAt(double mouseX, double mouseY) {
        if(!isVisible){
            return null;
        }
        return IMultipleGuiEventListener.super.getEventListenerAt(mouseX, mouseY);
    }

    public void toggle(){
        isVisible = !isVisible;
        if(isVisible){
            onOpened();
        }else{
            onClosed();
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if(key == 256 && isVisible){
            toggle();
            return true;
        }
        return IMultipleGuiEventListener.super.keyPressed(key, scanCode, modifiers);
    }

    public abstract void onOpened();

    public abstract void onClosed();

    @Override
    public List<? extends IGuiEventListener> getEventListeners() {
        return interactWidgets;
    }
}
