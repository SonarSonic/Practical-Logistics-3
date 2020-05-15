package sonar.logistics.client.gsi.components.text.api;

import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

public interface IGlyphType {

    float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling);

    float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling);

    void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo);

    default float upscale(ScaledFontType fontType, GlyphStyle parentStyling){
        return fontType.getFontScaling() / parentStyling.fontHeight;
    }

    default float downscale(ScaledFontType fontType, GlyphStyle parentStyling){
        return parentStyling.fontHeight / fontType.getFontScaling();
    }

    default boolean isSpace(){
        return false;
    }

    default boolean isWordBreaker(){
        return isSpace();
    }

    /**this doesn't prevent render being called instead this should be false if the end-user can't see the glyph, when moving the cursor for example, invisible glyphs like style / line breaks will be ignored*/
    default boolean isVisible(){
        return true;
    }

}