package sonar.logistics.client.gsi.interactions;

import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.vectors.Vector2D;

public class DraggingInteraction<C extends IComponent> extends AbstractComponentInteraction<C> {

    public Vector2D startMouse;
    public boolean isDragging;

    public DraggingInteraction(C component) {
        super(component);
    }

    @Override
    public boolean mouseClicked(int button) {
        if(canStartDrag(button)){
            startMouse = getInteractionHandler().mousePos.copy();
            isDragging = true;
            onDragStarted(button);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int button) {
        if(isDragging){
            onDragFinished(button);
            startMouse = null;
            isDragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(int button) {
        if(isDragging){
            onDragged(button);
            return true;
        }
        return false;
    }

    @Override
    public boolean isDragging() {
        return isDragging;
    }

    public double getDragX(){
        return getInteractionHandler().mousePos.x - startMouse.x;
    }

    public double getDragY(){
        return getInteractionHandler().mousePos.y - startMouse.y;
    }

    public boolean canStartDrag(int button){
        return button == 0;
    }

    public void onDragStarted(int button){}

    public void onDragFinished(int button){}

    public void onDragged(int button){}
}
