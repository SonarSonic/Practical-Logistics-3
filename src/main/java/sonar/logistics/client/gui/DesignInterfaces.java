package sonar.logistics.client.gui;

import sonar.logistics.client.gsi.components.buttons.ColouredButtonComponent;
import sonar.logistics.client.gsi.components.buttons.TextButtonComponent;
import sonar.logistics.client.gsi.components.groups.BasicGroup;
import sonar.logistics.client.gsi.components.groups.interfaces.ColourSelectionWindow;
import sonar.logistics.client.gsi.components.input.SliderComponent;
import sonar.logistics.client.gsi.components.input.TextInputComponent;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.api.ISimpleWidget;
import sonar.logistics.client.gui.widgets.GSIWidget;
import sonar.logistics.client.gsi.api.EnumButtonIcons;
import sonar.logistics.client.gsi.components.buttons.IconButtonComponent;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.properties.AbsoluteBounds;
import sonar.logistics.client.gsi.interactions.triggers.Trigger;
import sonar.logistics.client.vectors.Quad2D;


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
        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_SELECT, new Trigger<>((b, h) -> GSIDesignSettings.screen.displayGSI.interactionHandler.setInteractionType(GSIInteractionHandler.InteractionType.GUI_EDITING), (b, h) -> GSIDesignSettings.screen.displayGSI.interactionHandler.getInteractionType() == GSIInteractionHandler.InteractionType.GUI_EDITING))).setBounds(new AbsoluteBounds(8, 16*2, 16, 16));
        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_MOVE, new Trigger<>((b, h) -> GSIDesignSettings.screen.displayGSI.interactionHandler.setInteractionType(GSIInteractionHandler.InteractionType.GUI_RESIZING), (b, h) -> GSIDesignSettings.screen.displayGSI.interactionHandler.getInteractionType() == GSIInteractionHandler.InteractionType.GUI_RESIZING))).setBounds(new AbsoluteBounds(8, 16*3, 16, 16));
        //normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_EDIT_TEXT, new Trigger((b, h) -> GSIDesignSettings.screen.gsiViewportWidget.setTextInteraction(), (b, h) -> GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof EditStandardTextInteraction))).setBounds(new AbsoluteBounds(8, 16*4, 16, 16));

        ///

        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.GRID_NORMAL, new Trigger<>((b, h) -> GSIDesignSettings.setOrResetSnapping(0.0625F), (b, h) -> GSIDesignSettings.snapping == 0.0625F))).setBounds(new AbsoluteBounds(8, 16*13, 16, 16));

        ///

        return normalToolsWidget;
    }


    public static ISimpleWidget initTextTools(GSIDesignScreen screen){
        textToolsWidget = new GSIWidget();

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_BOLD, new Trigger<>((b, h) -> GSIDesignSettings.toggleBoldStyling(), (b, h) -> GSIDesignSettings.glyphStyle.bold))).setBounds(new AbsoluteBounds(16*2, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_ITALIC, new Trigger<>((b, h) -> GSIDesignSettings.toggleItalicStyling(), (b, h) -> GSIDesignSettings.glyphStyle.italic))).setBounds(new AbsoluteBounds(16*3, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_UNDERLINE, new Trigger<>((b, h) -> GSIDesignSettings.toggleUnderlineStyling(), (b, h) -> GSIDesignSettings.glyphStyle.underlined))).setBounds(new AbsoluteBounds(16*4, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_STRIKETHROUGH, new Trigger<>((b, h) -> GSIDesignSettings.toggleStrikethroughStyling(), (b, h) -> GSIDesignSettings.glyphStyle.strikethrough))).setBounds(new AbsoluteBounds(16*5, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_OBFUSCATE, new Trigger<>((b, h) -> GSIDesignSettings.toggleObfuscatedStyling(), (b, h) -> GSIDesignSettings.glyphStyle.obfuscated))).setBounds(new AbsoluteBounds(16*6, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_SHADOW, new Trigger<>((b, h) -> GSIDesignSettings.toggleShadowStyling(), (b, h) -> GSIDesignSettings.glyphStyle.shadow))).setBounds(new AbsoluteBounds(16*7, 8, 16, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_LEFT, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_LEFT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_LEFT))).setBounds(new AbsoluteBounds(16*9, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_CENTRE, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_CENTRE), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_CENTRE))).setBounds(new AbsoluteBounds(16*10, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_RIGHT, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_RIGHT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_RIGHT))).setBounds(new AbsoluteBounds(16*11, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY))).setBounds(new AbsoluteBounds(16*12, 8, 16, 16));

        ///
        TextInputComponent fontHeightComponent = new TextInputComponent(){

            {
                //set default gui font height
                inputGlyphStyle.fontHeight = 9;

                styling.bgdColour = ScreenUtils.transparent_disabled_button;
                styling.borderSize.value = 1;
                styling.borderColour = ScreenUtils.button_border;
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
        textToolsWidget.gsi.addComponent(fontHeightComponent.setInputType(TextInputComponent.EnumTextInputType.DIGIT_ONLY).setMaxStringLength(4).setTrigger((component, handler) -> GSIDesignSettings.setFontHeight(component.text().getIntegerFromText())).setText("" + ((int)(GSIDesignSettings.glyphStyle.fontHeight*256F)))).setBounds(new AbsoluteBounds(16*14, 8, 32, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.DECREASE_FONT_SIZE, new Trigger<>((b, h) -> GSIDesignSettings.decreaseFontHeight(), (b, h) -> false))).setBounds(new AbsoluteBounds(16*16, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.INCREASE_FONT_SIZE, new Trigger<>((b, h) -> GSIDesignSettings.increaseFontHeight(), (b, h) -> false))).setBounds(new AbsoluteBounds(16*17, 8, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.BULLET_POINT_TOGGLE, new Trigger<>((b, h) -> GSIDesignSettings.toggleLineBreakStyle(), (b, h) -> GSIDesignSettings.currentLineBreakStyle == GSIDesignSettings.selectedLineBreakStyle))).setBounds(new AbsoluteBounds(16*18, 8, 16, 16));
       // textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.CLEAR_FORMATTING, new Trigger<>((b, h) -> { if(GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof EditStyledTextInteraction) ((EditStyledTextInteraction) GSIDesignSettings.screen.gsiViewportWidget.currentInteraction).clearFormatting();}, (b, h) -> false))).setBounds(new AbsoluteBounds(16*19, 12, 16, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_TEXT_COLOUR, new Trigger<>((b, h) -> GSIDesignSettings.setTextColour(GSIDesignSettings.selectedColour.copy()), (b, h) -> false))).setBounds(new AbsoluteBounds(16*20, 8, 16, 16));


        ///
        ColouredButtonComponent selectedColourButton = new ColouredButtonComponent(){

            {
                styling.borderSize.value = 1;
                styling.borderColour = ScreenUtils.button_border;
            }
        };
        ColourSelectionWindow colourSelectionWindow = new ColourSelectionWindow(new AbsoluteBounds(100, 100, 160, 80)){

            @Override
            public void setTextColour(ColourProperty property) {
                GSIDesignSettings.selectedColour = property.copy();
                selectedColourButton.setColours(property.rgba, -1, -1);
            }
        };

        textToolsWidget.gsi.addComponent(colourSelectionWindow);
        textToolsWidget.gsi.addComponent(selectedColourButton.setTrigger(new Trigger<>((b, h) -> colourSelectionWindow.toggleVisibility(), (b, h) -> true)).setColours(GSIDesignSettings.selectedColour.rgba, -1, -1)).setBounds(new AbsoluteBounds(16*21, 8, 16, 16));

        ///

        return textToolsWidget;
    }

}
