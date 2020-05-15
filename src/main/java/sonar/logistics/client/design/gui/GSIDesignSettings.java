package sonar.logistics.client.design.gui;

import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.properties.ColourProperty;

public class GSIDesignSettings {

    public static GSIDesignScreen screen;

    public static void setDesignScreen(GSIDesignScreen screen){
        GSIDesignSettings.screen = screen;
    }


    //// GLYPH STYLING

    public static GlyphStyle glyphStyle = new GlyphStyle();

    public static void setFontHeight(float fontHeight){
        glyphStyle.fontHeight = fontHeight;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.FONT_HEIGHT);
    }

    public static void setTextColour(ColourProperty textColour){
        glyphStyle.textColour = textColour;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.TEXT_COLOUR);
    }

    public static void setBackgroundColour(ColourProperty backgroundColour){
        glyphStyle.backgroundColour = backgroundColour;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.BACKGROUND_COLOUR);
    }

    public static void setActionID(int actionId){
        glyphStyle.actionId = actionId;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.ACTION_ID);
    }

    public static void toggleBoldStyling(){
        glyphStyle.bold = !glyphStyle.bold;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.BOLD);
    }

    public static void toggleUnderlineStyling(){
        glyphStyle.underlined = !glyphStyle.underlined;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.UNDERLINE);
    }

    public static void toggleItalicStyling(){
        glyphStyle.italic = !glyphStyle.italic;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.ITALIC);
    }

    public static void toggleStrikethroughStyling(){
        glyphStyle.strikethrough = !glyphStyle.strikethrough;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.STRIKETHROUGH);
    }

    public static void toggleObfuscatedStyling(){
        glyphStyle.obfuscated = !glyphStyle.obfuscated;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.OBFUSCATED);
    }

    public static void toggleShadowStyling(){
        glyphStyle.shadow = !glyphStyle.shadow;
        screen.gsiRenderWidget.onGlyphStyleChanged(EnumGlyphStyling.SHADOW);
    }


    //// LINE STYLING

    public static LineStyle lineStyle = new LineStyle();

    public static void setLineSpacing(double lineSpacing){
        lineStyle.lineSpacing = lineSpacing;
        screen.gsiRenderWidget.onLineStyleChanged(EnumLineStyling.LINE_SPACING);
    }

    public static void setCharSpacing(double charSpacing){
        lineStyle.charSpacing = charSpacing;
        screen.gsiRenderWidget.onLineStyleChanged(EnumLineStyling.CHAR_SPACING);
    }

    public static void setWrappingType(LineStyle.WrappingType wrappingType){
        lineStyle.wrappingType = wrappingType;
        screen.gsiRenderWidget.onLineStyleChanged(EnumLineStyling.WRAPPING_TYPE);
    }

    public static void setAlignType(LineStyle.AlignType alignType){
        lineStyle.alignType = alignType;
        screen.gsiRenderWidget.onLineStyleChanged(EnumLineStyling.ALIGN_TYPE);
    }

    public static void setBreakPreference(LineStyle.BreakPreference breakPreference){
        lineStyle.breakPreference = breakPreference;
        screen.gsiRenderWidget.onLineStyleChanged(EnumLineStyling.BREAK_PREFERENCE);
    }

    //// GRID SNAPPING

    public static double snapping = 0;

    public static void setOrResetSnapping(double value) {
        snapping = snapping == value ? 0 : value;
    }

    public static double snapToScaledGrid(double value, double scaled) {
        return snapping == 0 ? value : (snapping * scaled) * (Math.round(value / (snapping * scaled)));
    }

    public static double snapToNormalGrid(double value) {
        return snapping == 0 ? value : snapping * (Math.round(value / snapping));
    }



    //// CURSOR COUNTER

    private static int cursorCounter;

    public static void tickCursorCounter() {
        cursorCounter ++;
        if(cursorCounter == 100){
            cursorCounter = 1;
        }
    }

    public static boolean canRenderCursor(){
        return cursorCounter / 6 % 2==0;
    }

}
