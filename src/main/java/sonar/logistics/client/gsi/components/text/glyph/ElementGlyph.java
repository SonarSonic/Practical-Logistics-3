package sonar.logistics.client.gsi.components.text.glyph;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.components.text.StyledTextRenderer;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

public class ElementGlyph implements IGlyphType {

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
    public float render(StyledTextRenderer.GlyphRenderContext context) {
        float size = getRenderWidth(context.getScaledFont(), context.parentStyling);
        if(element.canRender(context.renderContext)){
            context.renderContext.matrix.push();
            element.render(context.renderContext, new Vec3d(context.offsetX, context.offsetY, 0), new Vec3d(size, size, 0));
            context.renderContext.matrix.pop();
        }
        return size;
    }

    @Override
    public String toString() {
        return "Element Glyph: " + element.toString();
    }

}
