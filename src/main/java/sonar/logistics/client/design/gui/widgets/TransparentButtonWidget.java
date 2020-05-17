package sonar.logistics.client.design.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import sonar.logistics.client.design.api.IInteractWidget;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;

import java.util.function.Consumer;
import java.util.function.Function;

public class TransparentButtonWidget extends AbstractGui implements IInteractWidget {

    public int uvLeft, uvTop;
    public Function<TransparentButtonWidget, Boolean> isActive;
    public Consumer<TransparentButtonWidget> press;

    public Quad2D quad;

    public TransparentButtonWidget(int x, int y, int uvLeft, int uvTop, Function<TransparentButtonWidget, Boolean> isActive, Consumer<TransparentButtonWidget> press) {
        this(x, y, 16, 16, uvLeft, uvTop, isActive, press);
    }

    public TransparentButtonWidget(int x, int y, int width, int height, int uvLeft, int uvTop, Function<TransparentButtonWidget, Boolean> isActive, Consumer<TransparentButtonWidget> press) {
        this.quad = new Quad2D(x, y, width, height);
        this.uvLeft = uvLeft;
        this.uvTop = uvTop;
        this.isActive = isActive;
        this.press = press;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
        if(isMouseOver(mouseX, mouseY)){
            press.accept(this);
            return true;
        }
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bindTexture(ScreenUtils.BUTTONS_ALPHA);
        ScreenUtils.fillDouble(quad.x, quad.y, quad.x + quad.width, quad.y + quad.height, isActive.apply(this) ? ScreenUtils.transparent_activated_button.rgba : ScreenUtils.transparent_disabled_button.rgba);
        ScreenUtils.blitDouble(quad.x, quad.y, quad.width, quad.height, uvLeft, uvTop);
    }

    @Override
    public Quad2D getQuad() {
        return quad;
    }
}
