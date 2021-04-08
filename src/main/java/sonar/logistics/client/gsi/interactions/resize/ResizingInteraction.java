package sonar.logistics.client.gsi.interactions.resize;

import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.interactions.AbstractComponentInteraction;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.properties.Unit;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.util.vectors.Quad2D;
import sonar.logistics.util.vectors.Vector2D;

public class ResizingInteraction extends AbstractComponentInteraction<Component> {

    private Vector2D dragStart;

    EnumRescaleType currentRescaleType = null;
    float clickBoxSize = 0.0625F/2;

    public ResizingInteraction(Component component) {
        super(component);
    }

    @Override
    public void renderInteraction(GSIRenderContext context) {
        super.renderInteraction(context);

        clickBoxSize = 4 / context.gsiScaling;

        Quad2D rescaledBounds = getRescaledBounds();
        if(rescaledBounds == null){
            return; //TODO hmmm
        }
        for (EnumRescaleType type : EnumRescaleType.values()) { //render selection box
            if (type.xPos != -1) {
                double[] clickBox = type.getClickBox(rescaledBounds, clickBoxSize);
                GSIRenderHelper.renderColouredRect(context, false, (float)clickBox[0], (float)clickBox[1], (float)clickBox[2], (float)clickBox[3], 255, 255, 255, 255);
            }
        }
        if (currentRescaleType != null) { //render resize box
            GSIRenderHelper.renderColouredRect(context, false, rescaledBounds, ScreenUtils.transparent_green_button.rgba);
        }
    }

    @Override
    public boolean canStartDrag(int button) {
        return getRescaleTypeFromMouse(component, getInteractionHandler()) != null && isDragButton(button);
    }

    @Override
    public void onDragStarted(int button) {
        super.onDragStarted(button);
        currentRescaleType = getRescaleTypeFromMouse(component, getInteractionHandler());
        dragStart = getInteractionHandler().mousePos.copy();
    }

    @Override
    public void onDragFinished(int button) {
        super.onDragFinished(button);
        Quad2D gsiBounds = getGSI().getGSIBounds();
        Quad2D componentBounds = getRescaledBounds();
        double x = (componentBounds.x - gsiBounds.x) / gsiBounds.width;
        double y = (componentBounds.y - gsiBounds.y) / gsiBounds.height;
        double width = componentBounds.width / gsiBounds.width;
        double height = componentBounds.height / gsiBounds.height;
        component.getStyling().setSizing(x, y, width, height, Unit.PERCENT);
        component.rebuild();
        currentRescaleType = null;
        dragStart = null;
    }

    @Override
    public boolean isMouseOver() {
        return getRescaleTypeFromMouse(component, getInteractionHandler()) != null;
    }

    public Quad2D getRescaledBounds(){
        return currentRescaleType == null ? component.getBounds().outerSize() : currentRescaleType.rescaleWindow(component.getBounds().outerSize(), getGSI().getGSIBounds(), GSIDesignSettings.snapToNormalGrid(getMousePos().x - dragStart.x), GSIDesignSettings.snapToNormalGrid(getMousePos().y - dragStart.y), getInteractionHandler().hasShiftDown());
    }

    public EnumRescaleType getRescaleTypeFromMouse(Component component, GSIInteractionHandler handler) {
        return EnumRescaleType.getRescaleTypeFromMouse(component.getBounds().outerSize(), handler.mousePos.x, handler.mousePos.y, clickBoxSize);
    }
}

