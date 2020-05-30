package sonar.logistics.client.gsi.interactions;

import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gui.api.EnumRescaleType;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

public class ResizingInteraction extends AbstractComponentInteraction<IComponent> {

    private Vector2D dragStart;

    EnumRescaleType currentRescaleType = null;
    float clickBoxSize = 0.0625F/2;

    public ResizingInteraction(IComponent component) {
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
        component.setBounds(new ScaleableBounds(new Quad2D(x, y, width, height)));
        getGSI().build();
        currentRescaleType = null;
        dragStart = null;
    }

    @Override
    public boolean isMouseOver() {
        return getRescaleTypeFromMouse(component, getInteractionHandler()) != null && super.isMouseOver();
    }

    public Quad2D getRescaledBounds(){
        return currentRescaleType == null ? component.getBounds().maxBounds() : currentRescaleType.rescaleWindow(component.getBounds().maxBounds(), getGSI().getGSIBounds(), GSIDesignSettings.snapToNormalGrid(getMousePos().x - dragStart.x), GSIDesignSettings.snapToNormalGrid(getMousePos().y - dragStart.y), getInteractionHandler().hasShiftDown());
    }

    public EnumRescaleType getRescaleTypeFromMouse(IComponent component, GSIInteractionHandler handler) {
        return EnumRescaleType.getRescaleTypeFromMouse(component.getBounds().maxBounds(), handler.mousePos.x, handler.mousePos.y, clickBoxSize);
    }
}

