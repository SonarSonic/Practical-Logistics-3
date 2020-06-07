package sonar.logistics.client.gui;

import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.components.buttons.ColouredButtonComponent;
import sonar.logistics.client.gsi.components.groups.HeaderGroup;
import sonar.logistics.client.gsi.components.groups.interfaces.*;
import sonar.logistics.client.gsi.components.input.TextInputComponent;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.UnitLength;
import sonar.logistics.client.gsi.style.properties.UnitType;
import sonar.logistics.client.gui.api.ISimpleWidget;
import sonar.logistics.client.gui.widgets.GSIWidget;
import sonar.logistics.client.gsi.api.EnumButtonIcons;
import sonar.logistics.client.gsi.components.buttons.IconButtonComponent;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.interactions.triggers.Trigger;


public class DesignInterfaces {

    public static GSIWidget normalToolsWidget;
    public static GSIWidget textToolsWidget;

    public static void tick(){
        if(normalToolsWidget != null){
            normalToolsWidget.gsi.tick();
        }
        if(textToolsWidget != null){
            textToolsWidget.gsi.tick();
        }
    }

    public static void onSettingChanged(Object setting, Object settingObj){
        if(normalToolsWidget != null){
            normalToolsWidget.gsi.onSettingChanged(setting, settingObj);
        }
        if(textToolsWidget != null){
            textToolsWidget.gsi.onSettingChanged(setting, settingObj);
        }
    }

    public static ISimpleWidget initNormalTools(GSIDesignScreen screen){
        normalToolsWidget = new GSIWidget();
        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_SELECT, new Trigger<>((b, h) -> GSIDesignSettings.screen.displayGSI.interactionHandler.setInteractionType(GSIInteractionHandler.InteractionType.GUI_EDITING), (b, h) -> GSIDesignSettings.screen.displayGSI.interactionHandler.getInteractionType() == GSIInteractionHandler.InteractionType.GUI_EDITING))).getStyling().setSizing(8, 16*2, 16, 16, UnitType.PIXEL);
        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_MOVE, new Trigger<>((b, h) -> GSIDesignSettings.screen.displayGSI.interactionHandler.setInteractionType(GSIInteractionHandler.InteractionType.GUI_RESIZING), (b, h) -> GSIDesignSettings.screen.displayGSI.interactionHandler.getInteractionType() == GSIInteractionHandler.InteractionType.GUI_RESIZING))).getStyling().setSizing(8, 16*3, 16, 16, UnitType.PIXEL);
        //normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_EDIT_TEXT, new Trigger((b, h) -> GSIDesignSettings.screen.gsiViewportWidget.setTextInteraction(), (b, h) -> GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof EditStandardTextInteraction))).setBounds(new AbsoluteBounds(8, 16*4, 16, 16));

        ///

        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.GRID_NORMAL, new Trigger<>((b, h) -> GSIDesignSettings.setOrResetSnapping(0.0625F), (b, h) -> GSIDesignSettings.snapping == 0.0625F))).getStyling().setSizing(8, 16*13, 16, 16, UnitType.PIXEL);

        ///

        return normalToolsWidget;
    }


    public static ISimpleWidget initTextTools(GSIDesignScreen screen){
        textToolsWidget = new GSIWidget();

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_BOLD, new Trigger<>((b, h) -> GSIDesignSettings.toggleBoldStyling(), (b, h) -> GSIDesignSettings.glyphStyle.bold))).getStyling().setSizing(16*2, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_ITALIC, new Trigger<>((b, h) -> GSIDesignSettings.toggleItalicStyling(), (b, h) -> GSIDesignSettings.glyphStyle.italic))).getStyling().setSizing(16*3, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_UNDERLINE, new Trigger<>((b, h) -> GSIDesignSettings.toggleUnderlineStyling(), (b, h) -> GSIDesignSettings.glyphStyle.underlined))).getStyling().setSizing(16*4, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_STRIKETHROUGH, new Trigger<>((b, h) -> GSIDesignSettings.toggleStrikethroughStyling(), (b, h) -> GSIDesignSettings.glyphStyle.strikethrough))).getStyling().setSizing(16*5, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_OBFUSCATE, new Trigger<>((b, h) -> GSIDesignSettings.toggleObfuscatedStyling(), (b, h) -> GSIDesignSettings.glyphStyle.obfuscated))).getStyling().setSizing(16*6, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_SHADOW, new Trigger<>((b, h) -> GSIDesignSettings.toggleShadowStyling(), (b, h) -> GSIDesignSettings.glyphStyle.shadow))).getStyling().setSizing(16*7, 8, 16, 16, UnitType.PIXEL);

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_LEFT, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_LEFT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_LEFT))).getStyling().setSizing(16*9, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_CENTRE, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_CENTRE), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_CENTRE))).getStyling().setSizing(16*10, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_RIGHT, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_RIGHT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_RIGHT))).getStyling().setSizing(16*11, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY))).getStyling().setSizing(16*12, 8, 16, 16, UnitType.PIXEL);

        ///
        TextInputComponent fontHeightComponent = new TextInputComponent(){

            {
                //set default gui font height
                inputGlyphStyle.fontHeight = 9;

                styling.setOuterBackgroundColour(ScreenUtils.transparent_disabled_button);
                styling.setBorderWidth(new UnitLength(UnitType.PIXEL, 1));
                styling.setBorderHeight(new UnitLength(UnitType.PIXEL, 1));
                styling.setBorderColour(ScreenUtils.button_border);
            }

            @Override
            public void onSettingChanged(Object setting, Object settingObj) {
                super.onSettingChanged(setting, settingObj);
                if(setting == GlyphStyleAttributes.FONT_HEIGHT){
                    String value = String.valueOf((int)((float)(settingObj) * 256F));
                    if(!this.text().getRawString().equals(value)) {
                        setText(value);
                        rebuild();
                    }
                }
            }
        };
        textToolsWidget.gsi.addComponent(fontHeightComponent.setInputType(TextInputComponent.EnumTextInputType.DIGIT_ONLY).setMaxStringLength(4).setTrigger((component, handler) -> GSIDesignSettings.setFontHeight(component.text().getIntegerFromText())).setText("" + ((int)(GSIDesignSettings.glyphStyle.fontHeight*256F)))).getStyling().setSizing(16*14, 8, 32, 16, UnitType.PIXEL);

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.DECREASE_FONT_SIZE, new Trigger<>((b, h) -> GSIDesignSettings.decreaseFontHeight(), (b, h) -> false))).getStyling().setSizing(16*16, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.INCREASE_FONT_SIZE, new Trigger<>((b, h) -> GSIDesignSettings.increaseFontHeight(), (b, h) -> false))).getStyling().setSizing(16*17, 8, 16, 16, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.BULLET_POINT_TOGGLE, new Trigger<>((b, h) -> GSIDesignSettings.toggleLineBreakStyle(), (b, h) -> GSIDesignSettings.currentLineBreakStyle == GSIDesignSettings.selectedLineBreakStyle))).getStyling().setSizing(16*18, 8, 16, 16, UnitType.PIXEL);
       // textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.CLEAR_FORMATTING, new Trigger<>((b, h) -> { if(GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof EditStyledTextInteraction) ((EditStyledTextInteraction) GSIDesignSettings.screen.gsiViewportWidget.currentInteraction).clearFormatting();}, (b, h) -> false))).setBounds(new AbsoluteBounds(16*19, 12, 16, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_TEXT_COLOUR, new Trigger<>((b, h) -> GSIDesignSettings.setTextColour(GSIDesignSettings.selectedColour.copy()), (b, h) -> false))).getStyling().setSizing(16*20, 8, 16, 16, UnitType.PIXEL);


        ///
        ColouredButtonComponent selectedColourButton = new ColouredButtonComponent(){

            {
                styling.setBorderWidth(new UnitLength(UnitType.PIXEL, 1));
                styling.setBorderHeight(new UnitLength(UnitType.PIXEL, 1));
                styling.setBorderColour(ScreenUtils.button_border);
            }
        };
        ColourSelectionWindow colourSelectionWindow = new ColourSelectionWindow(){

            @Override
            public void setTextColour(ColourProperty property) {
                GSIDesignSettings.selectedColour = property.copy();
                selectedColourButton.setColours(property.rgba, -1, -1);
            }
        };

        textToolsWidget.gsi.addComponent(colourSelectionWindow).getStyling().setSizing(100, 100, 160, 80, UnitType.PIXEL);
        textToolsWidget.gsi.addComponent(selectedColourButton.setTrigger(new Trigger<>((b, h) -> colourSelectionWindow.toggleVisibility(), (b, h) -> true)).setColours(GSIDesignSettings.selectedColour.rgba, -1, -1)).getStyling().setSizing(16*21, 8, 16, 16, UnitType.PIXEL);

        /*
        LayoutGroup optionsList = new LayoutGroup();
        applyWindowStyling(optionsList);

        BasicList sizeOptionsList = new BasicList();
        sizeOptionsList.addComponent(new SliderOptionGroup.RangedInteger("Border Size", 0, 0, 10)).getStyling().setSizing(0, 0, 160, 20, UnitType.PIXEL);
        sizeOptionsList.addComponent(new SliderOptionGroup.RangedInteger("Text", 0, 0, 2)).getStyling().setSizing(0, 0, 160, 20, UnitType.PIXEL);
        sizeOptionsList.addComponent(new SliderOptionGroup.RangedInteger("Image Size", 3, 0, 10000)).getStyling().setSizing(0, 0, 160, 60, UnitType.PIXEL);
        optionsList.addComponent(createHeader(sizeOptionsList, "Size Options", true));

        textToolsWidget.gsi.addComponent(createHeader(optionsList, "Component Options", false)).getStyling().setSizing(100, 100, 160, 80, UnitType.PIXEL);
        */
        ///

        return textToolsWidget;
    }

    public static IComponent createHeader(IComponent component, String headerName, boolean isDropdown){
        WindowHeaderGroup header = new WindowHeaderGroup(headerName, isDropdown);
        return new HeaderGroup().setHeader(header).setInternal(component);
    }

    public static void applyWindowStyling(IComponent component){
        component.getStyling().setMarginWidth(new UnitLength(UnitType.PIXEL, 2));
        component.getStyling().setMarginHeight(new UnitLength(UnitType.PIXEL, 2));
        component.getStyling().setBorderWidth(new UnitLength(UnitType.PIXEL, 2));
        component.getStyling().setBorderHeight(new UnitLength(UnitType.PIXEL, 2));
        component.getStyling().setPaddingWidth(new UnitLength(UnitType.PIXEL, 2));
        component.getStyling().setPaddingHeight(new UnitLength(UnitType.PIXEL, 2));
        component.getStyling().setOuterBackgroundColour(ScreenUtils.display_black_border);
    }


}
