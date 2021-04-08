package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;

public class ComponentGlyph extends Glyph {

    public Component component;

    public ComponentGlyph(Component component){
        this.component = component;
    }

    @Override
    public float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling) {
        return (float)component.getStyling().getWidth().getValue(parentStyling.fontHeight);
    }

    @Override
    public float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling) {
        return (float)component.getStyling().getHeight().getValue(parentStyling.fontHeight);
    }

    @Override
    public void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {
        context.renderContext.matrix.push();
        component.render(context.renderContext);
        context.renderContext.matrix.pop();
    }

    @Override
    public String toString() {
        return "Element Glyph: " + component.toString();
    }

}
