package sonar.logistics.client.design.gui;

import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
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
        onGlyphAttributeChanged(GlyphStyleAttributes.FONT_HEIGHT);
    }

    public static void setTextColour(ColourProperty textColour){
        glyphStyle.textColour = textColour;
        onGlyphAttributeChanged(GlyphStyleAttributes.TEXT_COLOUR);
    }

    public static void setBackgroundColour(ColourProperty backgroundColour){
        glyphStyle.backgroundColour = backgroundColour;
        onGlyphAttributeChanged(GlyphStyleAttributes.BACKGROUND_COLOUR);
    }

    public static void setTriggerId(int triggerId){
        glyphStyle.triggerId = triggerId;
        onGlyphAttributeChanged(GlyphStyleAttributes.TRIGGER_ID);
    }

    public static void toggleBoldStyling(){
        glyphStyle.bold = !glyphStyle.bold;
        onGlyphAttributeChanged(GlyphStyleAttributes.BOLD);
    }

    public static void toggleUnderlineStyling(){
        glyphStyle.underlined = !glyphStyle.underlined;
        onGlyphAttributeChanged(GlyphStyleAttributes.UNDERLINE);
    }

    public static void toggleItalicStyling(){
        glyphStyle.italic = !glyphStyle.italic;
        onGlyphAttributeChanged(GlyphStyleAttributes.ITALIC);
    }

    public static void toggleStrikethroughStyling(){
        glyphStyle.strikethrough = !glyphStyle.strikethrough;
        onGlyphAttributeChanged(GlyphStyleAttributes.STRIKETHROUGH);
    }

    public static void toggleObfuscatedStyling(){
        glyphStyle.obfuscated = !glyphStyle.obfuscated;
        onGlyphAttributeChanged(GlyphStyleAttributes.OBFUSCATED);
    }

    public static void toggleShadowStyling(){
        glyphStyle.shadow = !glyphStyle.shadow;
        onGlyphAttributeChanged(GlyphStyleAttributes.SHADOW);
    }

    public static void onGlyphAttributeChanged(GlyphStyleAttributes attribute){
        screen.onGlyphAttributeChanged(attribute, attribute.get(glyphStyle));
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
