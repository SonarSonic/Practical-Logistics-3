package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

public class ElementGlyph extends Glyph {

    public IRenderableElement element;

    public ElementGlyph(IRenderableElement element){
        this.element = element;
    }

    @Override
    public float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling) {
        return fontType.getElementScaling()  * downscale(fontType, parentStyling);
    }

    @Override
    public float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling) {
        return fontType.getElementScaling()  * downscale(fontType, parentStyling);
    }

    @Override
    public void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        if(element.canRender(context.renderContext)){
            context.renderContext.matrix.push();
            element.render(context.renderContext, glyphInfo.quad);
            context.renderContext.matrix.pop();
        }
    }

    @Override
    public String toString() {
        return "Element Glyph: " + element.toString();
    }

}
