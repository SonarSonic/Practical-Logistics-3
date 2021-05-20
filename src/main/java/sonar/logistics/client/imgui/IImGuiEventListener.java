package sonar.logistics.client.imgui;

import imgui.ImGui;
import sonar.logistics.client.gui.widgets.IFlexibleGuiEventListener;

/**
 * Used for Widgets which use unscaled mouse positions.
 */
public interface IImGuiEventListener extends IFlexibleGuiEventListener {

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        getEventListener().mouseMoved(ImGui.getIO().getMousePosX(), ImGui.getIO().getMousePosY());
    }

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return getEventListener().mouseClicked(ImGui.getIO().getMousePosX(), ImGui.getIO().getMousePosY(), button);
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return getEventListener().mouseReleased(ImGui.getIO().getMousePosX(), ImGui.getIO().getMousePosY(), button);
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        double scaling = ImGui.getIO().getMousePosX() / mouseX;
        return getEventListener().mouseDragged(ImGui.getIO().getMousePosX(), ImGui.getIO().getMousePosY(), button, dragX*scaling, dragY*scaling);
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return getEventListener().mouseScrolled(ImGui.getIO().getMousePosX(), ImGui.getIO().getMousePosY(), scroll);
    }

    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return getEventListener().isMouseOver(ImGui.getIO().getMousePosX(), ImGui.getIO().getMousePosY());
    }
}
