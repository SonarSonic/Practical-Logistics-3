package sonar.logistics.client.gui;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.util.vectors.Quad2F;

public class GSIDesignScreen extends SimpleWidgetScreen {

    public final GSI displayGSI;
    public GSIViewportWidget gsiViewportWidget;

    public GSIDesignScreen(GSI displayGSI){
        this.displayGSI = displayGSI;
        this.displayGSI.interactionHandler.setInteractionType(GSIInteractionHandler.InteractionType.GUI_EDITING);
        DesignInterfaces.initNormalTools(this);
        DesignInterfaces.initTextTools(this);
    }

    @Override
    protected void init() {
        super.init();

        DesignInterfaces.normalToolsWidget.setBoundsAndRebuild(new Quad2F(guiLeft, guiTop, xSize, ySize));
        addWidget(DesignInterfaces.normalToolsWidget);

        DesignInterfaces.textToolsWidget.setBoundsAndRebuild(new Quad2F(guiLeft, guiTop, xSize, ySize));
        addWidget(DesignInterfaces.textToolsWidget);

        addWidget(gsiViewportWidget = new GSIViewportWidget(displayGSI, this.guiLeft + 32, this.guiTop + 32, this.xSize - 64, this.ySize - 64));
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(mouseX, mouseY, partialTicks);
        fill(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, ScreenUtils.transparent_grey_bgd.rgba);

        ////viewport border
        int marginWidth = 32, marginHeight = 32;
        int borderWidth = 1, borderHeight = 1;
        int endX = xSize - marginWidth, endY = ySize - marginHeight;

        fill(guiLeft + marginWidth, guiTop + marginHeight, guiLeft + marginWidth + borderWidth, guiTop + endY, ScreenUtils.light_grey.rgba);
        fill(guiLeft + endX-borderWidth, guiTop + marginHeight, guiLeft + endX-borderWidth + borderWidth, guiTop + endY, ScreenUtils.light_grey.rgba);

        fill(guiLeft + marginWidth, guiTop + marginHeight, guiLeft + endX, guiTop + marginHeight+ borderHeight, ScreenUtils.light_grey.rgba);
        fill( guiLeft + marginWidth, guiTop + endY - borderHeight, guiLeft + endX, guiTop + endY - borderHeight + borderHeight, ScreenUtils.light_grey.rgba);


        ////button dividers
        fill(guiLeft + 16*8 + 7, guiTop + 8, guiLeft + 16*8 + 9, guiTop + 8 + 16, ScreenUtils.light_grey.rgba);
        fill(guiLeft + 16*13 + 7, guiTop + 8, guiLeft + 16*13 + 9, guiTop + 8 + 16, ScreenUtils.light_grey.rgba);

    }


    public void onSettingChanged(Object setting, Object settingObj){
        displayGSI.onSettingChanged(setting, settingObj);
    }

    public void onCursorStyleChanged() {
        ///updates the font height - this should really be automatic, but this works for now.
        displayGSI.onSettingChanged(GlyphStyleAttributes.FONT_HEIGHT, GSIDesignSettings.glyphStyle.fontHeight);
    }

    @Override
    public void tick() {
        super.tick();
        GSIDesignSettings.tickCursorCounter();
        DesignInterfaces.tick();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
