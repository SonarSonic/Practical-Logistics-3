package sonar.logistics.client.design.gui;

import sonar.logistics.client.design.gui.interactions.DefaultDragInteraction;
import sonar.logistics.client.design.gui.interactions.DefaultResizeInteraction;
import sonar.logistics.client.design.gui.interactions.DefaultTextInteraction;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.design.gui.widgets.GSIWidget;
import sonar.logistics.client.design.gui.widgets.PL3TextWidget;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.components.buttons.EnumButtonIcons;
import sonar.logistics.client.gsi.components.buttons.IconButtonComponent;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.properties.AbsoluteBounds;
import sonar.logistics.client.gsi.triggers.Trigger;
import sonar.logistics.client.vectors.Quad2D;

public class GSIDesignScreen extends SimpleWidgetScreen {

    public final GSI displayGSI;
    public GSIWidget editControls = new GSIWidget(); //TODO CONVERT TO A STATIC GSI - WHEN DESIGN IS FINISHED.
    public GSIViewportWidget gsiViewportWidget;

    public PL3TextWidget fontHeight; //TODO CHANGE ME

    public GSIDesignScreen(GSI displayGSI){
        this.displayGSI = displayGSI;
        GSIDesignSettings.setDesignScreen(this);
    }


    @Override
    protected void init() {
        super.init();
        ///
        addWidget(editControls);

        ///
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_SELECT, new Trigger((b, h) -> gsiViewportWidget.currentInteraction = gsiViewportWidget.dragInteraction, (b, h) -> gsiViewportWidget.currentInteraction instanceof DefaultDragInteraction))).setBounds(new AbsoluteBounds(8, 16*2, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_MOVE, new Trigger((b, h) -> gsiViewportWidget.currentInteraction = gsiViewportWidget.resizeInteraction, (b, h) -> gsiViewportWidget.currentInteraction instanceof DefaultResizeInteraction))).setBounds(new AbsoluteBounds(8, 16*3, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_EDIT_TEXT, new Trigger((b, h) -> gsiViewportWidget.setTextInteraction(), (b, h) -> gsiViewportWidget.currentInteraction instanceof DefaultTextInteraction))).setBounds(new AbsoluteBounds(8, 16*4, 16, 16));

        ///

        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.GRID_NORMAL, new Trigger((b, h) -> GSIDesignSettings.setOrResetSnapping(0.0625F), (b, h) -> GSIDesignSettings.snapping == 0.0625F))).setBounds(new AbsoluteBounds(8, 16*13, 16, 16));

        ///

        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_BOLD, new Trigger((b, h) -> GSIDesignSettings.toggleBoldStyling(), (b, h) -> GSIDesignSettings.glyphStyle.bold))).setBounds(new AbsoluteBounds(16*2, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_ITALIC, new Trigger((b, h) -> GSIDesignSettings.toggleItalicStyling(), (b, h) -> GSIDesignSettings.glyphStyle.italic))).setBounds(new AbsoluteBounds(16*3, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_UNDERLINE, new Trigger((b, h) -> GSIDesignSettings.toggleUnderlineStyling(), (b, h) -> GSIDesignSettings.glyphStyle.underlined))).setBounds(new AbsoluteBounds(16*4, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_STRIKETHROUGH, new Trigger((b, h) -> GSIDesignSettings.toggleStrikethroughStyling(), (b, h) -> GSIDesignSettings.glyphStyle.strikethrough))).setBounds(new AbsoluteBounds(16*5, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_OBFUSCATE, new Trigger((b, h) -> GSIDesignSettings.toggleObfuscatedStyling(), (b, h) -> GSIDesignSettings.glyphStyle.obfuscated))).setBounds(new AbsoluteBounds(16*6, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_SHADOW, new Trigger((b, h) -> GSIDesignSettings.toggleShadowStyling(), (b, h) -> GSIDesignSettings.glyphStyle.shadow))).setBounds(new AbsoluteBounds(16*7, 12, 16, 16));

        ///

        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_LEFT, new Trigger((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_LEFT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_LEFT))).setBounds(new AbsoluteBounds(16*9, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_CENTRE, new Trigger((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_CENTRE), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_CENTRE))).setBounds(new AbsoluteBounds(16*10, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_RIGHT, new Trigger((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_RIGHT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_RIGHT))).setBounds(new AbsoluteBounds(16*11, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY, new Trigger((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY))).setBounds(new AbsoluteBounds(16*12, 12, 16, 16));

        ///

        //TODO CONVERT FONTHEIGHT TO PL3 COMPONENT
        fontHeight = PL3TextWidget.create("", font, guiLeft + 16*14, guiTop + 13, 32, 14).setOutlineColor(ScreenUtils.light_grey.rgba).setDigitsOnly();
        fontHeight.setMaxStringLength(4);
        fontHeight.setText(String.valueOf((int)(GSIDesignSettings.glyphStyle.fontHeight * 256F)));
        fontHeight.setResponder(string -> {
            if(fontHeight.isFocused()) {
                int height = fontHeight.getIntegerFromText(false);
                if (!fontHeight.getText().equals(String.valueOf(height))) {
                    GSIDesignSettings.setFontHeight(height);
                }
            }
        });
        addWidget(fontHeight);

        ///
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.DECREASE_FONT_SIZE, new Trigger((b, h) -> GSIDesignSettings.decreaseFontHeight(), (b, h) -> false))).setBounds(new AbsoluteBounds(16*16, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.INCREASE_FONT_SIZE, new Trigger((b, h) -> GSIDesignSettings.increaseFontHeight(), (b, h) -> false))).setBounds(new AbsoluteBounds(16*17, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.BULLET_POINT_TOGGLE, new Trigger((b, h) -> GSIDesignSettings.toggleLineBreakStyle(), (b, h) -> GSIDesignSettings.currentLineBreakStyle == GSIDesignSettings.selectedLineBreakStyle))).setBounds(new AbsoluteBounds(16*18, 12, 16, 16));
        editControls.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.CLEAR_FORMATTING, new Trigger((b, h) -> { if(gsiViewportWidget.currentInteraction instanceof DefaultTextInteraction) ((DefaultTextInteraction) gsiViewportWidget.currentInteraction).clearFormatting();}, (b, h) -> false))).setBounds(new AbsoluteBounds(16*19, 12, 16, 16));

        ///

        editControls.build(new Quad2D(guiLeft, guiTop, xSize, ySize));
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
        fill(guiLeft + 16*8 + 7, guiTop + 12, guiLeft + 16*8 + 9, guiTop + 12 + 16, ScreenUtils.light_grey.rgba);
        fill(guiLeft + 16*13 + 7, guiTop + 12, guiLeft + 16*13 + 9, guiTop + 12 + 16, ScreenUtils.light_grey.rgba);

    }



    public void onGlyphAttributeChanged(GlyphStyleAttributes attribute, Object attributeObj) {
        gsiViewportWidget.onGlyphAttributeChanged(attribute, attributeObj);
        if(attribute == GlyphStyleAttributes.FONT_HEIGHT){
            String value = String.valueOf((int)((float)(attributeObj) * 256F));
            if(!fontHeight.getText().equals(value))
                fontHeight.setText(value);
        }
    }

    public void onLineStyleChanged(EnumLineStyling lineStyling) {
        gsiViewportWidget.onLineStyleChanged(lineStyling);
    }

    public void onLineBreakGlyphChanged(EnumLineBreakGlyph currentLineBreakStyle) {
        gsiViewportWidget.onLineBreakGlyphChanged(currentLineBreakStyle);
    }

    public void onCursorStyleChanged() {
        String value = String.valueOf((int)(GSIDesignSettings.glyphStyle.fontHeight * 256F));
        if(!fontHeight.getText().equals(value))
            fontHeight.setText(value);
    }



    @Override
    public boolean isPauseScreen() {
        return false;
    }
}