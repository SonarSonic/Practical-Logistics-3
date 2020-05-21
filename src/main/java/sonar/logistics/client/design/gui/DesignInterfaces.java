package sonar.logistics.client.design.gui;

import sonar.logistics.client.design.api.ISimpleWidget;
import sonar.logistics.client.design.gui.interactions.DefaultDragInteraction;
import sonar.logistics.client.design.gui.interactions.DefaultResizeInteraction;
import sonar.logistics.client.design.gui.interactions.DefaultTextInteraction;
import sonar.logistics.client.design.gui.widgets.GSIWidget;
import sonar.logistics.client.gsi.components.buttons.EnumButtonIcons;
import sonar.logistics.client.gsi.components.buttons.IconButtonComponent;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.properties.AbsoluteBounds;
import sonar.logistics.client.gsi.triggers.Trigger;
import sonar.logistics.client.vectors.Quad2D;

public class DesignInterfaces {

    public static GSIWidget normalToolsWidget = new GSIWidget();
    public static GSIWidget textToolsWidget = new GSIWidget();



    public static ISimpleWidget initNormalTools(GSIDesignScreen screen){

        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_SELECT, new Trigger((b, h) -> GSIDesignSettings.screen.gsiViewportWidget.currentInteraction = GSIDesignSettings.screen.gsiViewportWidget.dragInteraction, (b, h) -> GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof DefaultDragInteraction))).setBounds(new AbsoluteBounds(8, 16*2, 16, 16));
        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_MOVE, new Trigger((b, h) -> GSIDesignSettings.screen.gsiViewportWidget.currentInteraction = GSIDesignSettings.screen.gsiViewportWidget.resizeInteraction, (b, h) -> GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof DefaultResizeInteraction))).setBounds(new AbsoluteBounds(8, 16*3, 16, 16));
        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.MODE_EDIT_TEXT, new Trigger((b, h) -> GSIDesignSettings.screen.gsiViewportWidget.setTextInteraction(), (b, h) -> GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof DefaultTextInteraction))).setBounds(new AbsoluteBounds(8, 16*4, 16, 16));

        ///

        normalToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.GRID_NORMAL, new Trigger((b, h) -> GSIDesignSettings.setOrResetSnapping(0.0625F), (b, h) -> GSIDesignSettings.snapping == 0.0625F))).setBounds(new AbsoluteBounds(8, 16*13, 16, 16));

        ///

        normalToolsWidget.build(new Quad2D(screen.guiLeft, screen.guiTop, screen.xSize, screen.ySize));

        ///

        return normalToolsWidget;
    }


    public static ISimpleWidget initTextTools(GSIDesignScreen screen){

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_BOLD, new Trigger((b, h) -> GSIDesignSettings.toggleBoldStyling(), (b, h) -> GSIDesignSettings.glyphStyle.bold))).setBounds(new AbsoluteBounds(16*2, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_ITALIC, new Trigger((b, h) -> GSIDesignSettings.toggleItalicStyling(), (b, h) -> GSIDesignSettings.glyphStyle.italic))).setBounds(new AbsoluteBounds(16*3, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_UNDERLINE, new Trigger((b, h) -> GSIDesignSettings.toggleUnderlineStyling(), (b, h) -> GSIDesignSettings.glyphStyle.underlined))).setBounds(new AbsoluteBounds(16*4, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_STRIKETHROUGH, new Trigger((b, h) -> GSIDesignSettings.toggleStrikethroughStyling(), (b, h) -> GSIDesignSettings.glyphStyle.strikethrough))).setBounds(new AbsoluteBounds(16*5, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_OBFUSCATE, new Trigger((b, h) -> GSIDesignSettings.toggleObfuscatedStyling(), (b, h) -> GSIDesignSettings.glyphStyle.obfuscated))).setBounds(new AbsoluteBounds(16*6, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_SHADOW, new Trigger((b, h) -> GSIDesignSettings.toggleShadowStyling(), (b, h) -> GSIDesignSettings.glyphStyle.shadow))).setBounds(new AbsoluteBounds(16*7, 12, 16, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_LEFT, new Trigger((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_LEFT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_LEFT))).setBounds(new AbsoluteBounds(16*9, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_CENTRE, new Trigger((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_CENTRE), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_CENTRE))).setBounds(new AbsoluteBounds(16*10, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY_RIGHT, new Trigger((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_RIGHT), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_RIGHT))).setBounds(new AbsoluteBounds(16*11, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.JUSTIFY, new Trigger((b, h) -> GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY), (b, h) -> GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY))).setBounds(new AbsoluteBounds(16*12, 12, 16, 16));

        ///

        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.DECREASE_FONT_SIZE, new Trigger((b, h) -> GSIDesignSettings.decreaseFontHeight(), (b, h) -> false))).setBounds(new AbsoluteBounds(16*16, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.INCREASE_FONT_SIZE, new Trigger((b, h) -> GSIDesignSettings.increaseFontHeight(), (b, h) -> false))).setBounds(new AbsoluteBounds(16*17, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.BULLET_POINT_TOGGLE, new Trigger((b, h) -> GSIDesignSettings.toggleLineBreakStyle(), (b, h) -> GSIDesignSettings.currentLineBreakStyle == GSIDesignSettings.selectedLineBreakStyle))).setBounds(new AbsoluteBounds(16*18, 12, 16, 16));
        textToolsWidget.gsi.addComponent(new IconButtonComponent(EnumButtonIcons.CLEAR_FORMATTING, new Trigger((b, h) -> { if(GSIDesignSettings.screen.gsiViewportWidget.currentInteraction instanceof DefaultTextInteraction) ((DefaultTextInteraction) GSIDesignSettings.screen.gsiViewportWidget.currentInteraction).clearFormatting();}, (b, h) -> false))).setBounds(new AbsoluteBounds(16*19, 12, 16, 16));

        ///

        textToolsWidget.build(new Quad2D(screen.guiLeft, screen.guiTop, screen.xSize, screen.ySize));

        ///

        return textToolsWidget;
    }

}
