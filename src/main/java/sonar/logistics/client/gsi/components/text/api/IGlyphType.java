package sonar.logistics.client.gsi.components.text.api;

import sonar.logistics.client.gsi.components.text.StyledTextRenderer;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

public interface IGlyphType {

    //int DEFAULT_ELEMENT_HEIGHT = 8; //ignores any underlining

    float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling);

    float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling);

    float render(StyledTextRenderer.GlyphRenderContext context);

    default float upscale(ScaledFontType fontType, GlyphStyle parentStyling){
        return fontType.getFontScaling() / parentStyling.fontHeight;
    }

    default float downscale(ScaledFontType fontType, GlyphStyle parentStyling){
        return parentStyling.fontHeight / fontType.getFontScaling();
    }

    default boolean isSpace(){
        return false;
    }

}