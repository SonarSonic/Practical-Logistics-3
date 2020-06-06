package sonar.logistics.client.gsi.components.groups.interfaces;

import sonar.logistics.client.gsi.components.groups.LayoutGroup;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.properties.UnitLength;
import sonar.logistics.client.gsi.style.properties.UnitType;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;

/**gui only component*/
public class DropdownGroup extends LayoutGroup {

    public String groupName;

    public DropdownGroup(String groupName){
        this.groupName = groupName;
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

        //addComponent(new TextButtonComponent("\u2630", new Trigger<>((b, h) -> toggleVisibility(), (b, h) -> false)).setColours(ScreenUtils.red_button.rgba, ScreenUtils.display_black_border.rgba, ScreenUtils.red_button.rgba)).setBounds(new AbsoluteBounds(160 - 12 - 6, -12 - 6, 12, 12));
        //addComponent(new RectangleComponent(ScreenUtils.display_black_border.rgba)).setBounds(new AbsoluteBounds(-6, -12 - 6, 160 - 12, 12));

    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        Quad2D offsetBounds = getBounds().innerSize().copy();
        subComponents.forEach(c -> {c.build(offsetBounds.copy()); offsetBounds.translate(0, c.getBounds().outerSize().height);});
    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible){
            GSIRenderHelper.renderComponentBackground(context, bounds, styling);
            GSIRenderHelper.renderComponentBorder(context, bounds, styling);
            subComponents.forEach(e -> e.render(context));
        }

        GSIRenderHelper.renderBasicString(context, groupName, getBounds().outerSize().getX() + 2, getBounds().outerSize().getY() - 10, -1, false);

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
}
