package sonar.logistics.client.design.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.multiparts.displays.api.IDisplay;

import java.util.List;

public class GSIDesignScreen extends Screen {

    public final GSI gsi;
    protected final List<IRenderable> renderables = Lists.newArrayList();

    protected int guiLeft;
    protected int guiTop;
    protected int xSize = 384;
    protected int ySize = 256;

    public GSIDesignScreen(GSI gsi, PlayerEntity player) {
        super(new StringTextComponent("GSI"));
        this.gsi = gsi;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        renderables.clear();
        addRenderable(new GSIRenderWidget(gsi, this.guiLeft + 32, this.guiTop + 32, this.xSize - 64, this.ySize - 64));
    }

    protected void addRenderable(IRenderable renderable) {
        this.renderables.add(renderable);
        if(renderable instanceof IGuiEventListener) {
            this.children.add((IGuiEventListener)renderable);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        for(int i = 0; i < this.renderables.size(); ++i) {
            this.renderables.get(i).render(mouseX, mouseY, partialTicks);
        }
        fill(guiLeft, guiTop, guiLeft + xSize, guiTop + 32, ScreenUtils.blue_overlay.rgba);
        fill(guiLeft, guiTop + ySize - 32, guiLeft + xSize, guiTop + ySize, ScreenUtils.blue_overlay.rgba);

        fill(guiLeft, guiTop, guiLeft + 32, guiTop + ySize, ScreenUtils.blue_overlay.rgba);
        fill(guiLeft + xSize - 32, guiTop, guiLeft + xSize, guiTop + ySize, ScreenUtils.blue_overlay.rgba);
        this.drawGuiContainerBackgroundLayer(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(int mouseX, int mouseY) {
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        fill(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, ScreenUtils.grey_base.rgba);
        fill(this.guiLeft + 1, this.guiTop + 1, this.guiLeft + this.xSize - 1, this.guiTop + this.ySize - 1, ScreenUtils.blue_overlay.rgba);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
