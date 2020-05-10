package sonar.logistics.client.design.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.gsi.properties.ColourProperty;

import java.util.function.Function;

public class TransparentButtonWidget extends Button {

    public int uvLeft, uvTop;
    public Function<Button, Boolean> isActive;

    public TransparentButtonWidget(int x, int y, int uvLeft, int uvTop, Function<Button, Boolean> isActive, IPressable pressable) {
        this(x, y, 16, 16, uvLeft, uvTop, isActive, pressable);
    }

    public TransparentButtonWidget(int x, int y, int width, int height, int uvLeft, int uvTop, Function<Button, Boolean> isActive, IPressable pressable) {
        super(x, y, width, height, "", pressable);
        this.uvLeft = uvLeft;
        this.uvTop = uvTop;
        this.isActive = isActive;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bindTexture(ScreenUtils.BUTTONS_ALPHA);
        fill(x, y, x + width, y + height, isActive.apply(this) ? ScreenUtils.transparent_green_button.rgba : ScreenUtils.transparent_grey_button.rgba);
        blit(x, y, uvLeft, uvTop, width, height);
    }


}
