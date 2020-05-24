package sonar.logistics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import sonar.logistics.client.gui.api.IInteractWidget;
import sonar.logistics.client.gui.api.ISimpleWidget;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SimpleWidgetScreen extends Screen {

    public List<ISimpleWidget> simpleWidgets = new ArrayList<>();
    public List<IInteractWidget> interactWidgets = new ArrayList<>();

    public int guiLeft;
    public int guiTop;
    public int xSize = 384;
    public int ySize = 256;

    protected SimpleWidgetScreen() {
        super(new StringTextComponent("PL3"));
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        simpleWidgets.clear();
        interactWidgets.clear();
    }

    public void addWidget(ISimpleWidget widget){
        simpleWidgets.add(widget);
        if(widget instanceof IInteractWidget){
            interactWidgets.add((IInteractWidget) widget);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderSystem.disableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.defaultAlphaFunc();
        renderBackground();

        RenderSystem.translated(0, 0 ,-10);
        drawBackground(mouseX, mouseY, partialTicks);
        RenderSystem.translated(0, 0 ,10);

        drawWidgets(mouseX, mouseY, partialTicks);
        drawForeground(mouseX, mouseY, partialTicks);
    }

    public void drawBackground(int mouseX, int mouseY, float partialTicks){

    }

    public void drawWidgets(int mouseX, int mouseY, float partialTicks){
        simpleWidgets.forEach(w -> w.render(mouseX, mouseY, partialTicks));
    }


    public void drawForeground(int mouseX, int mouseY, float partialTicks){

    }

    @Nonnull
    @Override
    public List<? extends IGuiEventListener> children() {
        return interactWidgets;
    }
}
