package sonar.logistics.client.gsi.components.text;

import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.context.DisplayClickContext;
import sonar.logistics.client.gsi.context.DisplayInteractionContext;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;

public class StyledTextButton extends StyledTextComponent {

    public boolean isHovered = false;
    public boolean wasClicked = false;

    @Override
    public void render(ScaleableRenderContext context) {
        super.render(context);
        if(wasClicked){

        }

    }

    @Override
    public float renderGlyph(IGlyphType glyph, StyledTextRenderer.GlyphRenderContext context) {
        if(isHovered || wasClicked){
            /*
            byte[] last = context.parentStyling.textColour;
            context.updateColour(new byte[]{(byte) 255,(byte)255,(byte)255,(byte)255}, 1.0F);
            float offsetX = glyph.render(context);
            context.updateColour(last, 1.0F);
            return offsetX;

             */
        }

        return glyph.render(context);
    }

    @Override
    public boolean onHovered(DisplayInteractionContext context) {
        isHovered = true;
        return true;
    }


    @Override
    public boolean onClicked(DisplayClickContext context) {
        wasClicked = true;
        return true;
    }
}
