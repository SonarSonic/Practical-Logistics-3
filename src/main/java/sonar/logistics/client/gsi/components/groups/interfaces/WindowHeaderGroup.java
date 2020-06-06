package sonar.logistics.client.gsi.components.groups.interfaces;

import sonar.logistics.client.gsi.components.buttons.ColouredButtonComponent;
import sonar.logistics.client.gsi.components.buttons.TextButtonComponent;
import sonar.logistics.client.gsi.components.groups.HeaderGroup;
import sonar.logistics.client.gsi.components.groups.LayoutGroup;
import sonar.logistics.client.gsi.interactions.triggers.Trigger;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.properties.UnitLength;
import sonar.logistics.client.gsi.style.properties.UnitType;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;

public class WindowHeaderGroup extends LayoutGroup {

    public String windowName;
    public boolean isDropdown;

    private ColouredButtonComponent windowBar;
    private TextButtonComponent headerButton;
    private double headerSize = 12;

    public WindowHeaderGroup(String windowName, boolean isDropdown){
        this.windowName = windowName;
        this.isDropdown = isDropdown;
        this.styling.setHeight(new UnitLength(UnitType.PIXEL, headerSize));

        addComponent(windowBar = new ColouredButtonComponent((source, handler) -> moveWindow()).setColours(ScreenUtils.display_black_border.rgba, ScreenUtils.display_black_border.rgba, ScreenUtils.dark_grey.rgba));
        addComponent(headerButton = new TextButtonComponent(isDropdown ? "\u2630":  "\u2715", new Trigger<>((b, h) -> {
            if(isDropdown){
                dropdownWindow();
            }else{
                closeWindow();
            }
        }, (b, h) -> false))).setColours(ScreenUtils.red_button.rgba, ScreenUtils.display_black_border.rgba, ScreenUtils.red_button.rgba);
    }

    @Override
    public void build(Quad2D bounds) {
        this.bounds.build(bounds, styling);
        windowBar.getStyling().setSizing(0, 0, getBounds().innerSize().width - headerSize, headerSize, UnitType.PIXEL);
        headerButton.getStyling().setSizing(getBounds().innerSize().width - headerSize, 0, headerSize, headerSize, UnitType.PIXEL);
        subComponents.forEach(c -> c.build(getBounds().innerSize()));
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        if(isVisible){
            GSIRenderHelper.renderBasicString(context, windowName, getBounds().innerSize().getX() + 2, getBounds().innerSize().getY(), -1, false);
        }
    }

    public void closeWindow(){
        if(this.getHost() instanceof HeaderGroup){
            ((HeaderGroup) this.getHost()).toggleVisibility();
        }
    }

    public void dropdownWindow(){
        if(this.getHost() instanceof HeaderGroup){
            ((HeaderGroup) this.getHost()).toggleInternalVisibility();
        }
    }

    public void moveWindow(){
        ///TODO
    }

}
