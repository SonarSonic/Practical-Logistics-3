package sonar.logistics.client.gsi.components.groups.interfaces;

import sonar.logistics.client.gsi.components.buttons.ColouredButtonComponent;
import sonar.logistics.client.gsi.components.buttons.TextButtonComponent;
import sonar.logistics.client.gsi.components.groups.BasicGroup;
import sonar.logistics.client.gsi.interactions.triggers.Trigger;
import sonar.logistics.client.gsi.properties.AbsoluteBounds;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Vector2D;

public class WindowGroup extends BasicGroup {

    public String windowName;

    public WindowGroup(String windowName, AbsoluteBounds bounds){
        this.windowName = windowName;
        this.setBounds(bounds);
        this.init();
        this.bounds.setZLayer(10);
    }

    public void init(){
        ///the pop up always starts invisible.
        isVisible = false;

        styling.marginHeight.value = 2;
        styling.marginWidth.value = 2;
        styling.borderSize.value = 2;
        styling.borderPadding.value = 2;
        styling.bgdColour = ScreenUtils.display_black_border;


        addComponent(new TextButtonComponent("\u2715", new Trigger<>((b, h) -> toggleVisibility(), (b, h) -> false)).setColours(ScreenUtils.red_button.rgba, ScreenUtils.display_black_border.rgba, ScreenUtils.red_button.rgba)).setBounds(new AbsoluteBounds(((AbsoluteBounds)getBounds()).absoluteQuad.getWidth() - 12 - 6, -12 - 6, 12, 12));
        addComponent(new ColouredButtonComponent((source, handler) -> startDrag()).setColours(ScreenUtils.display_black_border.rgba, ScreenUtils.display_black_border.rgba, ScreenUtils.dark_grey.rgba)).setBounds(new AbsoluteBounds(-6, -12 - 6, ((AbsoluteBounds)getBounds()).absoluteQuad.getWidth() - 12, 12));

    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        if(isVisible){
            GSIRenderHelper.renderBasicString(context, windowName, getBounds().maxBounds().getX() + 2, getBounds().maxBounds().getY() - 10, -1, false);
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
        return isVisible && (getBounds().maxBounds().contains(getInteractionHandler().mousePos) || super.isMouseOver());
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
            AbsoluteBounds bounds = (AbsoluteBounds) getBounds();
            dragStart = getInteractionHandler().mousePos.copy();
            oldAlignment = bounds.absoluteQuad.getAlignment();
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
            AbsoluteBounds bounds = (AbsoluteBounds) getBounds();
            bounds.absoluteQuad.setAlignment(oldAlignment.x + getMousePos().x - dragStart.x, oldAlignment.y + getMousePos().y - dragStart.y);
            rebuild();
            return true;
        }else{
            return super.mouseDragged();
        }
    }
}
