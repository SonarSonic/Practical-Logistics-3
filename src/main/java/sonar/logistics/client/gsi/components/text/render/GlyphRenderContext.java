package sonar.logistics.client.gsi.components.text.render;

import net.minecraft.client.gui.fonts.Font;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.style.properties.ColourProperty;

public class GlyphRenderContext {

    public GSIRenderContext renderContext;
    public ScaledFontType fontType;

    public float red, green, blue, alpha;

    public GlyphRenderContext(GSIRenderContext renderContext, ScaledFontType fontType){
        this.renderContext = renderContext;
        this.fontType = fontType;
    }

    public void startLine(StyledTextLine line){}

    public void finishLine(StyledTextLine line){}

    public ScaledFontType getScaledFont(){
        return fontType;
    }

    public Font getFont(){
        return fontType.getFont();
    }

    ///// RENDERING HELPERS \\\\\\

    public void updateColour(ColourProperty colour, float brightness){
        red = colour.getRed() / 255.0F * brightness;
        green = colour.getGreen() / 255.0F * brightness;
        blue = colour.getBlue() / 255.0F  * brightness;
        alpha = colour.getAlpha() / 255.0F;
    }
}