package sonar.logistics.client.gui;

import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.imgui.GSIEditorScreen;
import sonar.logistics.util.MathUtils;

/**
 * The current settings for the GSI Design Settings
 */
public class GSIDesignSettings {

    public static GSIEditorScreen screen;

    public static void setDesignScreen(GSIEditorScreen screen){
        GSIDesignSettings.screen = screen;
    }

    public static void onSettingChanged(Object setting, Object settingObj){
        screen.onSettingChanged(setting, settingObj);
        DesignInterfaces.onSettingChanged(setting, settingObj);
    }

    //// GLYPH STYLING

    public static GlyphStyle glyphStyle = new GlyphStyle();

    public static final int[] DEFAULT_FONT_HEIGHTS = new int[]{8,9,10,11,12,14,16,18,20,22,24,26,28,36,48,72};

    public static int getPixelFontHeight(){
        return (int)(glyphStyle.fontHeight * 256);
    }

    public static void setFontHeight(int fontHeight){
        glyphStyle.fontHeight = fontHeight / 256F;
        onGlyphAttributeChanged(GlyphStyleAttributes.FONT_HEIGHT);
    }

    public static void increaseFontHeight(){
        int pixelFontHeight = getPixelFontHeight();
        if(pixelFontHeight < 8){
            setFontHeight(pixelFontHeight + 1);
            return;
        }

        for(int i : DEFAULT_FONT_HEIGHTS){
            if(pixelFontHeight < i){
                setFontHeight(i);
                return;
            }
        }

        setFontHeight((int)MathUtils.increase(pixelFontHeight, 10));
    }

    public static void decreaseFontHeight(){
        int pixelFontHeight = getPixelFontHeight();
        if(pixelFontHeight > 80){
            setFontHeight((int)MathUtils.decrease(pixelFontHeight, 10));
            return;
        }

        for(int i = DEFAULT_FONT_HEIGHTS.length -1; i >= 0 ; i --){
            if(DEFAULT_FONT_HEIGHTS[i] < pixelFontHeight){
                setFontHeight(DEFAULT_FONT_HEIGHTS[i]);
                return;
            }
        }
        if(pixelFontHeight != 1){
            setFontHeight(pixelFontHeight - 1);
        }
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
        onSettingChanged(attribute, attribute.get(glyphStyle));
    }

    //// LINE STYLING

    public static LineStyle lineStyle = new LineStyle();

    public static void setLineSpacing(double lineSpacing){
        lineStyle.lineSpacing = lineSpacing;
        onSettingChanged(EnumLineStyling.LINE_SPACING, lineStyle);
    }

    public static void setCharSpacing(double charSpacing){
        lineStyle.charSpacing = charSpacing;
        onSettingChanged(EnumLineStyling.CHAR_SPACING, lineStyle);
    }

    public static void setWrappingType(LineStyle.WrappingType wrappingType){
        lineStyle.wrappingType = wrappingType;
        onSettingChanged(EnumLineStyling.WRAPPING_TYPE, lineStyle);
    }

    public static void setJustifyType(LineStyle.JustifyType justifyType){
        lineStyle.justifyType = justifyType;
        onSettingChanged(EnumLineStyling.ALIGN_TYPE, lineStyle);
    }

    public static void setBreakPreference(LineStyle.BreakPreference breakPreference){
        lineStyle.breakPreference = breakPreference;
        onSettingChanged(EnumLineStyling.BREAK_PREFERENCE, lineStyle);
    }

    //// LINE BREAK GLYPH TYPES

    public static EnumLineBreakGlyphTypes currentLineBreakStyle = EnumLineBreakGlyphTypes.DEFAULT_BREAK;
    public static EnumLineBreakGlyphTypes selectedLineBreakStyle = EnumLineBreakGlyphTypes.ROUND_BULLET_POINT;

    public static void toggleLineBreakStyle(){
        currentLineBreakStyle = currentLineBreakStyle == selectedLineBreakStyle ? EnumLineBreakGlyphTypes.DEFAULT_BREAK : selectedLineBreakStyle;
        onSettingChanged(currentLineBreakStyle, null);
    }

    public static LineBreakGlyph getLineBreakGlyph(boolean pageBreak, LineStyle lineStyle){
        return currentLineBreakStyle.create(pageBreak, lineStyle);
    }


    //// GRID SNAPPING

    public static double snapping = 0.0625;

    public static void setOrResetSnapping(double value) {
        snapping = snapping == value ? 0 : value;
    }

    public static double snapToScaledGrid(double value, double scaled) {
        return snapping == 0 ? value : (snapping * scaled) * (Math.round(value / (snapping * scaled)));
    }

    public static double snapToNormalGrid(double value) {
        return snapping == 0 ? value : snapping * (Math.round(value / snapping));
    }


    //// COLOUR SELECTIONS

    public static ColourProperty selectedColour = ScreenUtils.white;


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
