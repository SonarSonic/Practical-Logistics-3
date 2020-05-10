package sonar.logistics.client.design.gui.interactions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import sonar.logistics.client.design.gui.GSIDesignSettings;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.design.api.Window;
import sonar.logistics.client.design.windows.EnumRescaleType;
import sonar.logistics.client.gsi.context.DisplayInteractionContext;

public class ViewportResizeInteraction extends ViewportAbstractInteraction {

    public ViewportResizeInteraction(GSIViewportWidget viewport) {
        super(viewport);
    }

    @Override
    public GSIDesignSettings.ViewportInteractSetting getViewportSetting() {
        return GSIDesignSettings.ViewportInteractSetting.RESIZE_COMPONENTS;
    }

    @Override
    public void renderScissored(int mouseX, int mouseY, float partialTicks) {
        super.renderScissored(mouseX, mouseY, partialTicks);

        if(viewport.selectedComponent != null) {
            Window window = viewport.getWindowForComponent(viewport.selectedComponent);
            for (EnumRescaleType type : EnumRescaleType.values()) { //render selection box
                if (type.xPos != -1) {
                    double[] clickBox = type.getClickBox(window, 3.0F);
                    ScreenUtils.fillDouble(clickBox[0], clickBox[1], clickBox[2], clickBox[3], ScreenUtils.white.rgba);
                }
            }
            if(viewport.currentRescaleType != null) { //render resize box
                Window rescaledWindow = viewport.currentRescaleType.rescaleWindow(window, viewport.getBoundsForDisplay(), viewport.getSnappedDragX(dragX), viewport.getSnappedDragY(dragY), Screen.hasShiftDown());
                ScreenUtils.fillDouble(rescaledWindow.x, rescaledWindow.y, rescaledWindow.x + rescaledWindow.width, rescaledWindow.y + rescaledWindow.height, ScreenUtils.transparent_green_button.rgba);
            }
        }

    }

    @Override
    public void onDragged(double mouseX, double mouseY, int button) {
        super.onDragged(mouseX, mouseY, button);
        if(viewport.selectedComponent != null && viewport.currentRescaleType == null){
            viewport.currentRescaleType = EnumRescaleType.MOVE;
        }
    }

    @Override
    public void onDragStarted(double mouseX, double mouseY, int button) {
        super.onDragStarted(mouseX, mouseY, button);
        if(viewport.selectedComponent  != null) {
            viewport.currentRescaleType = EnumRescaleType.getRescaleTypeFromMouse(viewport.getWindowForComponent(viewport.selectedComponent), mouseX, mouseY, 4.0F);
        }
    }

    @Override
    public void onDragFinished(double mouseX, double mouseY, int button) {
        super.onDragFinished(mouseX, mouseY, button);
        if(viewport.currentRescaleType != null && viewport.selectedComponent != null) {
            viewport.setAlignmentFromWindow(viewport.selectedComponent, viewport.currentRescaleType.rescaleWindow(viewport.getWindowForComponent(viewport.selectedComponent), viewport.getBoundsForDisplay(), viewport.getSnappedDragX(dragX), viewport.getSnappedDragY(dragY), Screen.hasShiftDown()));
            viewport.currentRescaleType = null;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean dragged = super.mouseClicked(mouseX, mouseY, button);
        if(viewport.isMouseOverViewport(mouseX, mouseY)) {
            if (button == 0) {
                if (viewport.selectedComponent == null || viewport.currentRescaleType == null) {
                    DisplayInteractionContext context = new DisplayInteractionContext(viewport.gsi, Minecraft.getInstance().player, true);
                    context.setDisplayClick((mouseX - viewport.getRenderOffsetX()) / viewport.scaling, (mouseY - viewport.getRenderOffsetY()) / viewport.scaling);
                    viewport.selectedComponent = viewport.gsi.getInteractedComponent(context);
                    return true;
                }
            }
        }
        return dragged;

    }
}
