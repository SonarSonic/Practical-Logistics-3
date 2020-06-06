package sonar.logistics.client.gsi.components.groups.interfaces;

import sonar.logistics.client.gsi.components.buttons.ColouredButtonComponent;
import sonar.logistics.client.gsi.components.buttons.TextButtonComponent;
import sonar.logistics.client.gsi.components.groups.LayoutGroup;
import sonar.logistics.client.gsi.interactions.triggers.Trigger;
import sonar.logistics.client.gsi.style.ComponentBounds;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.StyleHelper;
import sonar.logistics.client.gsi.style.properties.UnitLength;
import sonar.logistics.client.gsi.style.properties.UnitType;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

public class WindowGroup extends LayoutGroup {

    public String windowName;

    private ColouredButtonComponent windowBar;
    private TextButtonComponent closeButton;
    private double headerSize = 12;

    public WindowGroup(String windowName){
        this.windowName = windowName;
        this.init();
        this.getStyling().setZLayer(10);
    }

    public void init(){
        ///the pop up always starts invisible.
        isVisible = false;

        styling.setMarginWidth(new UnitLength(UnitType.PIXEL, 2));
        styling.setMarginHeight(new UnitLength(UnitType.PIXEL, 2));
        styling.setBorderWidth(new UnitLength(UnitType.PIXEL, 2));
        styling.setBorderHeight(new UnitLength(UnitType.PIXEL, 2));
        styling.setPaddingWidth(new UnitLength(UnitType.PIXEL, 2));
        styling.setPaddingHeight(new UnitLength(UnitType.PIXEL, 2));
        styling.setOuterBackgroundColour(ScreenUtils.display_black_border);


        addComponent(closeButton = new TextButtonComponent("\u2715", new Trigger<>((b, h) -> toggleVisibility(), (b, h) -> false))).setColours(ScreenUtils.red_button.rgba, ScreenUtils.display_black_border.rgba, ScreenUtils.red_button.rgba);
        addComponent(windowBar = new ColouredButtonComponent((source, handler) -> startDrag()).setColours(ScreenUtils.display_black_border.rgba, ScreenUtils.display_black_border.rgba, ScreenUtils.dark_grey.rgba));

    }

    @Override
    public void build(Quad2D bounds) {
        ///we get the initial outer size to use as a reference for the size of the window bar and close button
        Quad2D outerSize = StyleHelper.getComponentOuterSize(bounds, styling);

        ///we add the window bar within the windows outerSize, and then pass through an innerWindowBounds for the scaling of sub components
        ///note it's important that this windows actual outerBounds and innerBounds are relative to the innerWindow to allow borders / bgds to render as expected.
        windowBar.getStyling().setSizing(0, -headerSize, outerSize.width - headerSize, headerSize, UnitType.PIXEL);
        closeButton.getStyling().setSizing(outerSize.width - headerSize, -headerSize, headerSize, headerSize, UnitType.PIXEL);
        Quad2D innerWindowBounds = new Quad2D(outerSize.getX(), outerSize.getY() + headerSize, outerSize.getWidth(), outerSize.getHeight() - headerSize);
        super.build(innerWindowBounds);

        //the host bounds should always be the original Quad2D not the modified one, so we reset it here.
        this.bounds.setHostBounds(bounds);
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        if(isVisible){
            GSIRenderHelper.renderBasicString(context, windowName, getBounds().outerSize().getX() + 2, getBounds().outerSize().getY() - 10, -1, false);
        }
    }

    private Vector2D dragStart;
    private Vector2D oldAlignment;
    private boolean moveDrag;

    public void startDrag(){
        this.moveDrag = true;
        this.getHost().setDragging(true);
        this.getHost().setFocused(this);
        onDragStarted(0);
    }

    @Override
    public boolean isMouseOver() {
        //windows always check their own bounds, to avoid people interacting with things beneath them.
        return isVisible && (getBounds().outerSize().contains(getInteractionHandler().mousePos) || super.isMouseOver());
    }

    @Override
    public boolean mouseClicked(int button) {
        //windows always return true, to avoid people interacting with things beneath them.
        super.mouseClicked(button);
        return true;
    }

    @Override
    public void onDragStarted(int button) {
        if(moveDrag) {
            //n.b. this assumes the Window has absolute bounds, should we change this? if also allows positions outside of the hosts bounds.
            ComponentBounds bounds = getBounds();
            dragStart = getInteractionHandler().mousePos.copy();
            oldAlignment = new Vector2D(styling.getXPos().getValue(bounds.hostBounds.getWidth()), styling.getYPos().getValue(bounds.hostBounds.getHeight()));
        }else{
            super.onDragStarted(button);
        }
    }

    @Override
    public void onDragFinished(int button) {
        if(moveDrag) {
            dragStart = null;
            oldAlignment = null;
            moveDrag = false;
        }else{
            super.onDragFinished(button);
        }
    }

    @Override
    public boolean mouseDragged() {
        if(moveDrag) {
            styling.setSizing(new UnitLength(UnitType.PIXEL, oldAlignment.x + getMousePos().x - dragStart.x), new UnitLength(UnitType.PIXEL, oldAlignment.y + getMousePos().y - dragStart.y), styling.getWidth(), styling.getHeight());
            rebuild();
            return true;
        }else{
            return super.mouseDragged();
        }
    }
}
