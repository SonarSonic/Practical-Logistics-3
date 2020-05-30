package sonar.logistics.client.gsi.components.buttons;

import net.minecraft.client.Minecraft;
import sonar.logistics.client.gsi.api.ComponentAlignment;
import sonar.logistics.client.gsi.interactions.triggers.ITrigger;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;

//only gui scaling for now.
public class TextButtonComponent extends ColouredButtonComponent {

    public String text;

    public TextButtonComponent(String text, ITrigger<ColouredButtonComponent> trigger) {
        super(trigger);
        this.text = text;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        context.matrix.translate(0,0, -0.0001F);
        int width = Minecraft.getInstance().fontRenderer.getStringWidth(text);
        int height = Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
        GSIRenderHelper.renderBasicString(context, text, getBounds().renderBounds().x + ComponentAlignment.CENTERED.align(width, getBounds().renderBounds().getWidth()), getBounds().renderBounds().y + ComponentAlignment.CENTERED.align(height, getBounds().renderBounds().getHeight()), -1, false);
    }

}
