package sonar.logistics.client.gui.interactions;

import sonar.logistics.client.gui.widgets.GSIViewportWidget;
import sonar.logistics.client.gui.widgets.ScaleableWidget;

public class DefaultDragInteraction extends AbstractViewportInteraction {

    public DefaultDragInteraction(GSIViewportWidget viewport){
        super(viewport);
    }

    @Override
    public void onDragged(double mouseX, double mouseY, int button) {
        super.onDragged(mouseX, mouseY, button);

        if (ScaleableWidget.snapToGrid(dragX, 1) != viewport.centreX) {
            viewport.centreX += ScaleableWidget.snapToGrid(dragX, 1);
            dragX = 0;
        }

        if (ScaleableWidget.snapToGrid(dragY, 1) != viewport.centreY) {
            viewport.centreY += ScaleableWidget.snapToGrid(dragY, 1);
            dragY = 0;
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean dragged = super.mouseClicked(mouseX, mouseY, button);
        if (button == 1) {
            viewport.defaultScaling();
            viewport.defaultCentre();
            return true;
        }
        return dragged;

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if(isMouseOver(mouseX, mouseY)) {
            viewport.scaling += scroll;
            return true;
        }
        return false;
    }
}
