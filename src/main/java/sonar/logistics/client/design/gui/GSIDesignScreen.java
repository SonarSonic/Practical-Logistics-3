package sonar.logistics.client.design.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import sonar.logistics.client.design.api.ISimpleWidget;
import sonar.logistics.client.design.gui.widgets.DropdownButton;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.design.gui.widgets.TransparentButtonWidget;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

import java.util.List;

public class GSIDesignScreen extends Screen {

    public final GSI gsi;

    public final List<ISimpleWidget> renderables = Lists.newArrayList();
    public GSIViewportWidget gsiRenderWidget;


    public int guiLeft;
    public int guiTop;
    public int xSize = 384;
    public int ySize = 256;

    public GSIDesignScreen(GSI gsi, PlayerEntity player) {
        super(new StringTextComponent("GSI"));
        GSIDesignSettings.setDesignScreen(this);
        this.gsi = gsi;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        renderables.clear();
        addRenderable(new TransparentButtonWidget(guiLeft + 32, guiTop + 12, 176, 48, button -> GSIDesignSettings.glyphStyle.bold, button -> GSIDesignSettings.toggleBoldStyling()));
        addRenderable(new TransparentButtonWidget(guiLeft + 32 + 16, guiTop + 12, 176, 64, button -> GSIDesignSettings.glyphStyle.italic, button -> GSIDesignSettings.toggleItalicStyling()));
        addRenderable(new TransparentButtonWidget(guiLeft + 32 + 32, guiTop + 12, 176, 80, button -> GSIDesignSettings.glyphStyle.underlined, button -> GSIDesignSettings.toggleUnderlineStyling()));
        addRenderable(new TransparentButtonWidget(guiLeft + 32 + 48, guiTop + 12, 176, 96, button -> GSIDesignSettings.glyphStyle.strikethrough, button -> GSIDesignSettings.toggleStrikethroughStyling()));


        addRenderable(new TransparentButtonWidget(guiLeft + 32 + 80, guiTop + 12, 176, 0, button -> GSIDesignSettings.lineStyle.alignType == LineStyle.AlignType.ALIGN_TEXT_LEFT, button -> GSIDesignSettings.setAlignType(LineStyle.AlignType.ALIGN_TEXT_LEFT)));
        addRenderable(new TransparentButtonWidget(guiLeft + 32 + 96, guiTop + 12, 176, 16, button -> GSIDesignSettings.lineStyle.alignType == LineStyle.AlignType.CENTER, button -> GSIDesignSettings.setAlignType(LineStyle.AlignType.CENTER)));
        addRenderable(new TransparentButtonWidget(guiLeft + 32 + 112, guiTop + 12, 176, 32, button -> GSIDesignSettings.lineStyle.alignType == LineStyle.AlignType.ALIGN_TEXT_RIGHT, button -> GSIDesignSettings.setAlignType(LineStyle.AlignType.ALIGN_TEXT_RIGHT)));

        addRenderable(new DropdownButton(guiLeft + 32 + 112 + 16, guiTop + 12, 16, 16,
                Lists.newArrayList(
                        new TransparentButtonWidget(guiLeft + 32 + 80, guiTop + 12, 176, 0, button -> GSIDesignSettings.lineStyle.alignType == LineStyle.AlignType.ALIGN_TEXT_LEFT, button -> GSIDesignSettings.setAlignType(LineStyle.AlignType.ALIGN_TEXT_LEFT)),
                        new TransparentButtonWidget(guiLeft + 32 + 96, guiTop + 12, 176, 16, button -> GSIDesignSettings.lineStyle.alignType == LineStyle.AlignType.CENTER, button -> GSIDesignSettings.setAlignType(LineStyle.AlignType.CENTER)),
                        new TransparentButtonWidget(guiLeft + 32 + 112, guiTop + 12, 176, 32, button -> GSIDesignSettings.lineStyle.alignType == LineStyle.AlignType.ALIGN_TEXT_RIGHT, button -> GSIDesignSettings.setAlignType(LineStyle.AlignType.ALIGN_TEXT_RIGHT)))
        ));
        addRenderable(gsiRenderWidget = new GSIViewportWidget(gsi, this.guiLeft + 32, this.guiTop + 32, this.xSize - 64, this.ySize - 64));

    }

    @Override
    public void tick() {
        super.tick();
        GSIDesignSettings.tickCursorCounter();
    }

    public void addGuiEventListener(IGuiEventListener listener){
        this.children.add(listener);
    }

    protected void addRenderable(ISimpleWidget renderable) {
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

        ////viewport border
        int marginWidth = 32, marginHeight = 32;
        int borderWidth = 1, borderHeight = 1;
        int endX = xSize - marginWidth, endY = ySize - marginHeight;

        fill(guiLeft + marginWidth, guiTop + marginHeight, guiLeft + marginWidth + borderWidth, guiTop + endY, ScreenUtils.light_grey.rgba);
        fill(guiLeft + endX-borderWidth, guiTop + marginHeight, guiLeft + endX-borderWidth + borderWidth, guiTop + endY, ScreenUtils.light_grey.rgba);

        fill(guiLeft + marginWidth, guiTop + marginHeight, guiLeft + endX, guiTop + marginHeight+ borderHeight, ScreenUtils.light_grey.rgba);
        fill( guiLeft + marginWidth, guiTop + endY - borderHeight, guiLeft + endX, guiTop + endY - borderHeight + borderHeight, ScreenUtils.light_grey.rgba);


        ////button dividers
        fill(guiLeft + 32 + 64 + 8 -1, guiTop + 12, guiLeft + 32 + 64 + 8 + 1, guiTop + 12 + 16, ScreenUtils.light_grey.rgba);


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
