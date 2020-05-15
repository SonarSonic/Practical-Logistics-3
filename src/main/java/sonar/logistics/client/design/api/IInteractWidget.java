package sonar.logistics.client.design.api;

import net.minecraft.client.gui.IGuiEventListener;

public interface IInteractWidget extends IGuiEventListener, ISimpleWidget {

    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return getQuad().contains(mouseX, mouseY);
    }
}
