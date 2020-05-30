package sonar.logistics.client.gui;

import sonar.logistics.client.gsi.components.buttons.ColouredButtonComponent;
import sonar.logistics.client.gsi.components.buttons.TextButtonComponent;
import sonar.logistics.client.gsi.components.groups.BasicGroup;
import sonar.logistics.client.gsi.components.groups.interfaces.ColourSelectionWindow;
import sonar.logistics.client.gsi.components.input.SliderComponent;
import sonar.logistics.client.gsi.components.input.TextInputComponent;
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

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_BOLD, new Trigger<>((b, h) -> GSIDesignSettings.toggleBoldStyling(), (b, h) -> GSIDesignSettings.glyphStyle.bold))).setBounds(new AbsoluteBounds(16*2, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_ITALIC, new Trigger<>((b, h) -> GSIDesignSettings.toggleItalicStyling(), (b, h) -> GSIDesignSettings.glyphStyle.italic))).setBounds(new AbsoluteBounds(16*3, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_UNDERLINE, new Trigger<>((b, h) -> GSIDesignSettings.toggleUnderlineStyling(), (b, h) -> GSIDesignSettings.glyphStyle.underlined))).setBounds(new AbsoluteBounds(16*4, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_STRIKETHROUGH, new Trigger<>((b, h) -> GSIDesignSettings.toggleStrikethroughStyling(), (b, h) -> GSIDesignSettings.glyphStyle.strikethrough))).setBounds(new AbsoluteBounds(16*5, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_OBFUSCATE, new Trigger<>((b, h) -> GSIDesignSettings.toggleObfuscatedStyling(), (b, h) -> GSIDesignSettings.glyphStyle.obfuscated))).setBounds(new AbsoluteBounds(16*6, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_SHADOW, new Trigger<>((b, h) -> GSIDesignSettings.toggleShadowStyling(), (b, h) -> GSIDesignSettings.glyphStyle.shadow))).setBounds(new AbsoluteBounds(16*7, 12, 16, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_LEFT, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_LEFT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_LEFT))).setBounds(new AbsoluteBounds(16*9, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_CENTRE, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_CENTRE), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_CENTRE))).setBounds(new AbsoluteBounds(16*10, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_RIGHT, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_RIGHT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_RIGHT))).setBounds(new AbsoluteBounds(16*11, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY, new Trigger<>((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY))).setBounds(new AbsoluteBounds(16*12, 12, 16, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.DECREASE_FONT_SIZE, new Trigger<>((b, h) -> GSIDesignSettings.decreaseFontHeight(), (b, h) -> false))).setBounds(new AbsoluteBounds(16*16, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.INCREASE_FONT_SIZE, new Trigger<>((b, h) -> GSIDesignSettings.increaseFontHeight(), (b, h) -> false))).setBounds(new AbsoluteBounds(16*17, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.BULLET_POINT_TOGGLE, new Trigger<>((b, h) -> GSIDesignSettings.toggleLineBreakStyle(), (b, h) -> GSIDesignSettings.currentLineBreakStyle == GSIDesignSettings.selectedLineBreakStyle))).setBounds(new AbsoluteBounds(16*18, 12, 16, 16));
       // textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.CLEAR_FORMATTING, new Trigger<>((b, h) -> { if(GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof EditStyledTextInteraction) ((EditStyledTextInteraction) GSIDesignSettings.screen.gsiViewportWidget.currentInteraction).clearFormatting();}, (b, h) -> false))).setBounds(new AbsoluteBounds(16*19, 12, 16, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_TEXT_COLOUR, new Trigger<>((b, h) -> GSIDesignSettings.setTextColour(GSIDesignSettings.glyphStyle.textColour), (b, h) -> false))).setBounds(new AbsoluteBounds(16*20, 12, 16, 16));


        ///
        ColouredButtonComponent textColourButton = new ColouredButtonComponent();
        ColourSelectionWindow textColourSelectionWindow = new ColourSelectionWindow(new AbsoluteBounds(100, 100, 160, 80)){

            @Override
            public void setTextColour(ColourProperty property) {
                GSIDesignSettings.setTextColour(property);
                textColourButton.setColours(GSIDesignSettings.glyphStyle.textColour.rgba, -1, -1);
            }
        };

        textToolsWidget.gsi.addComponent(textColourSelectionWindow);
        textToolsWidget.gsi.addComponent(textColourButton.setTrigger(new Trigger<>((b, h) -> textColourSelectionWindow.toggleVisibility(), (b, h) -> true)).setColours(GSIDesignSettings.glyphStyle.textColour.rgba, -1, -1)).setBounds(new AbsoluteBounds(16*21, 12, 16, 16));

        ///

        return textToolsWidget;
    }

}
