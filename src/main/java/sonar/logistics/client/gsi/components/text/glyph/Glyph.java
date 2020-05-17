package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleHolder;

public abstract class Glyph {

    public GlyphStyleHolder styleHolder = null; // 0 = default style , 0 > = common style, 0 < = custom style.

    public GlyphStyle getStyle(){
        if(styleHolder == null){
            return new GlyphStyle();
        }
        return styleHolder.getStyle();
    }

    public abstract float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling);

    public abstract float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling);

    public abstract void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo);

    public float upscale(ScaledFontType fontType, GlyphStyle parentStyling){
        return fontType.getFontScaling() / parentStyling.fontHeight;
    }

    public float downscale(ScaledFontType fontType, GlyphStyle parentStyling){
        return parentStyling.fontHeight / fontType.getFontScaling();
    }

    public boolean isSpace(){
        return false;
    }

    public boolean isWordBreaker(){
        return isSpace();
    }

    /**this doesn't prevent render being called instead this should be false if the end-user can't see the glyph, when moving the cursor for example, invisible glyphs like style / line breaks will be ignored*/
    public boolean isVisible(){
        return true;
    }


}
