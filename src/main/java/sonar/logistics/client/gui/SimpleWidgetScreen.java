package sonar.logistics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import sonar.logistics.client.gui.widgets.AbstractWidget;
import sonar.logistics.client.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.gui.widgets.GSIWidget;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SimpleWidgetScreen extends Screen {

    public List<AbstractWidget> widgets = new ArrayList<>();

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
        widgets.clear();
    }

    public void addWidget(AbstractWidget widget){
        widgets.add(widget);
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

    public void drawBackground(int mouseX, int mouseY, float partialTicks){}

    public void drawWidgets(int mouseX, int mouseY, float partialTicks){
        widgets.forEach(w -> w.render(mouseX, mouseY, partialTicks));
    }


    public void drawForeground(int mouseX, int mouseY, float partialTicks){}

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        //gsi interactions have their own drag control, so we end dragging manually.
        widgets.forEach(i ->
            {
                if(i instanceof GSIWidget){
                    ((GSIWidget) i).gsi.tryEndDragging(button);
                }

                if(i instanceof GSIViewportWidget){
                    ((GSIViewportWidget) i).gsi.tryEndDragging(button);
                }
            }
       );
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.getFocused() != null && this.isDragging() && (button == 0 || button == 2) && this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Nonnull
    @Override
    public List<? extends IGuiEventListener> children() {
        return widgets;
    }
}
