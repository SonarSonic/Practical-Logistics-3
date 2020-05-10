package sonar.logistics.client.design.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.design.gui.widgets.TransparentButtonWidget;
import sonar.logistics.client.gsi.GSI;

import java.util.List;

public class GSIDesignScreen extends Screen {

    public final GSI gsi;

    public final List<IRenderable> renderables = Lists.newArrayList();
    public GSIViewportWidget gsiRenderWidget;


    public int guiLeft;
    public int guiTop;
    public int xSize = 384;
    public int ySize = 256;

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
        addRenderable(gsiRenderWidget = new GSIViewportWidget(gsi, this.guiLeft + 32, this.guiTop + 32, this.xSize - 64, this.ySize - 64));
        addRenderable(new TransparentButtonWidget(guiLeft + 6, guiTop + 4, 192, 0, button -> GSIDesignSettings.viewportInteractSetting == GSIDesignSettings.ViewportInteractSetting.RESIZE_COMPONENTS, button -> GSIDesignSettings.setViewportInteractSetting(GSIDesignSettings.ViewportInteractSetting.RESIZE_COMPONENTS)));
        addRenderable(new TransparentButtonWidget(guiLeft + 6, guiTop + 4 + 16 + 4, 192, 16, button -> GSIDesignSettings.viewportInteractSetting == GSIDesignSettings.ViewportInteractSetting.ZOOM_VIEWPORT, button -> GSIDesignSettings.setViewportInteractSetting(GSIDesignSettings.ViewportInteractSetting.ZOOM_VIEWPORT)));
        addRenderable(new TransparentButtonWidget(guiLeft + 6, guiTop + 4 + 16*2 + 4*2, 192, 48, button -> GSIDesignSettings.snapping == 0.0625, button -> GSIDesignSettings.setOrResetSnapping(0.0625)));
        addRenderable(new TransparentButtonWidget(guiLeft + 6, guiTop + 4 + 16*3 + 4*3, 192, 32, button -> GSIDesignSettings.viewportInteractSetting == GSIDesignSettings.ViewportInteractSetting.EDIT_TEXT, button -> GSIDesignSettings.setViewportInteractSetting(GSIDesignSettings.ViewportInteractSetting.EDIT_TEXT)));

    }

    @Override
    public void tick() {
        super.tick();
        GSIDesignSettings.tickCursorCounter();
    }

    public void addGuiEventListener(IGuiEventListener listener){
        this.children.add(listener);
    }

    protected void addRenderable(IRenderable renderable) {
        this.renderables.add(renderable);
        if(renderable instanceof IGuiEventListener) {
            addGuiEventListener((IGuiEventListener) renderable);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        RenderSystem.disableDepthTest();
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        for(int i = 0; i < this.renderables.size(); ++i) {
            this.renderables.get(i).render(mouseX, mouseY, partialTicks);
        }
        int marginWidth = 32, marginHeight = 32;
        int borderWidth = 1, borderHeight = 1;
        int endX = xSize - marginWidth, endY = ySize - marginHeight;

        fill(guiLeft + marginWidth, guiTop + marginHeight, guiLeft + marginWidth + borderWidth, guiTop + endY, ScreenUtils.light_grey.rgba);
        fill(guiLeft + endX-borderWidth, guiTop + marginHeight, guiLeft + endX-borderWidth + borderWidth, guiTop + endY, ScreenUtils.light_grey.rgba);

        fill(guiLeft + marginWidth, guiTop + marginHeight, guiLeft + endX, guiTop + marginHeight+ borderHeight, ScreenUtils.light_grey.rgba);
        fill( guiLeft + marginWidth, guiTop + endY - borderHeight, guiLeft + endX, guiTop + endY - borderHeight + borderHeight, ScreenUtils.light_grey.rgba);
        /*
        fill(guiLeft, guiTop, guiLeft + xSize, guiTop + 32, ScreenUtils.blue_overlay.rgba);
        fill(guiLeft, guiTop + ySize - 32, guiLeft + xSize, guiTop + ySize, ScreenUtils.blue_overlay.rgba);

        fill(guiLeft, guiTop, guiLeft + 32, guiTop + ySize, ScreenUtils.blue_overlay.rgba);
        fill(guiLeft + xSize - 32, guiTop, guiLeft + xSize, guiTop + ySize, ScreenUtils.blue_overlay.rgba);
        */
        this.drawGuiContainerBackgroundLayer(mouseX, mouseY);
        RenderSystem.enableDepthTest();
    }

    protected void drawGuiContainerBackgroundLayer(int mouseX, int mouseY) {
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        fill(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, ScreenUtils.transparent_grey_bgd.rgba);
        ///fill(this.guiLeft + 1, this.guiTop + 1, this.guiLeft + this.xSize - 1, this.guiTop + this.ySize - 1, ScreenUtils.blue_overlay.rgba);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
