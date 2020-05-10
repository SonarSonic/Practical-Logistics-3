package sonar.logistics.client.design.gui.interactions;

import net.minecraft.client.gui.IGuiEventListener;

public class AbstractDraggingInteraction implements IGuiEventListener {

    public double dragX, dragY;
    public boolean isDragging;

    public boolean canStartDrag(double mouseX, double mouseY, int button){
        return button == 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(canStartDrag(mouseX, mouseY, button)){
            dragX = dragY = 0;
            isDragging = true;
            onDragStarted(mouseX, mouseY, button);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(isDragging){
            onDragFinished(mouseX, mouseY, button);
            dragX = dragY = 0;
            isDragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(isDragging){
            this.dragX += dragX;
            this.dragY += dragY;
            onDragged(mouseX, mouseY, button);
            return true;
        }
        return false;
    }

    public void onDragStarted(double mouseX, double mouseY, int button){}

    public void onDragFinished(double mouseX, double mouseY, int button){}

    public void onDragged(double mouseX, double mouseY, int button){}
}
