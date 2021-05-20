package sonar.logistics.client.gsi.style;

import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.client.gsi.style.properties.Unit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ComponentStyling {

    protected Boolean absolute;

    ///TODO ALIGNMENT? //TODO RATIOS?

    ///positioning
    protected Integer zLayer;
    protected LengthProperty xPos;
    protected LengthProperty yPos;

    ////sizing
    protected LengthProperty width;
    protected LengthProperty height;

    ///offsets
    protected LengthProperty marginWidth;
    protected LengthProperty marginHeight;
    protected LengthProperty paddingWidth;
    protected LengthProperty paddingHeight;
    protected LengthProperty borderWidth;
    protected LengthProperty borderHeight;

    ///component colours
    protected ColourProperty outerBackgroundColour;
    protected ColourProperty innerBackgroundColour;
    protected ColourProperty borderColour;

    ///text colours
    protected ColourProperty enabledTextColour;
    protected ColourProperty hoveredTextColour;
    protected ColourProperty disabledTextColour;

    ///

    public ComponentStyling alterStyling(Consumer<ComponentStyling> alter){
        alter.accept(this);
        return this;
    }

    public ComponentStyling setSizing(double xPos, double yPos, double width, double height, Unit type){
        return setSizing(new LengthProperty(type, xPos), new LengthProperty(type, yPos), new LengthProperty(type, width), new LengthProperty(type, height));
    }

    public ComponentStyling setSizing(LengthProperty xPos, LengthProperty yPos, LengthProperty width, LengthProperty height){
        setXPos(xPos);
        setYPos(yPos);
        setWidth(width);
        setHeight(height);
        return this;
    }


    ///

    @Nonnull
    public Boolean isAbsolute() {
        return absolute == null ? DefaultStyling.INSTANCE.isAbsolute() : absolute;
    }

    public void setAbsolute(@Nonnull Boolean absolute) {
        this.absolute = absolute;
    }

    public Integer getZLayer() {
        return zLayer == null ? DefaultStyling.INSTANCE.getZLayer() : zLayer;
    }

    public void setZLayer(Integer zLayer) {
        this.zLayer = zLayer;
    }

    ///

    @Nonnull
    public LengthProperty getXPos() {
        return xPos == null ? DefaultStyling.INSTANCE.getXPos() : xPos;
    }

    public void setXPos(@Nonnull LengthProperty xPos) {
        this.xPos = xPos;
    }

    @Nonnull
    public LengthProperty getYPos() {
        return yPos == null ? DefaultStyling.INSTANCE.getYPos() : yPos;
    }

    public void setYPos(@Nonnull LengthProperty yPos) {
        this.yPos = yPos;
    }

    ///

    @Nonnull
    public LengthProperty getWidth() {
        return width == null ? DefaultStyling.INSTANCE.getWidth() : width;
    }

    public void setWidth(@Nonnull LengthProperty width) {
        this.width = width;
    }

    @Nonnull
    public LengthProperty getHeight() {
        return height == null ? DefaultStyling.INSTANCE.getHeight() : height;
    }

    public void setHeight(@Nonnull LengthProperty height) {
        this.height = height;
    }

    ///

    @Nonnull
    public LengthProperty getMarginWidth() {
        return marginWidth == null ? DefaultStyling.INSTANCE.getMarginWidth() : marginWidth;
    }

    public void setMarginWidth(@Nullable LengthProperty marginWidth) {
        this.marginWidth = marginWidth;
    }

    @Nonnull
    public LengthProperty getMarginHeight() {
        return marginHeight == null ? DefaultStyling.INSTANCE.getMarginHeight() : marginHeight;
    }

    public void setMarginHeight(@Nullable LengthProperty marginHeight) {
        this.marginHeight = marginHeight;
    }

    @Nonnull
    public LengthProperty getPaddingWidth() {
        return paddingWidth == null ? DefaultStyling.INSTANCE.getPaddingWidth() : paddingWidth;
    }

    public void setPaddingWidth(@Nullable LengthProperty paddingWidth) {
        this.paddingWidth = paddingWidth;
    }

    @Nonnull
    public LengthProperty getPaddingHeight() {
        return paddingHeight == null ? DefaultStyling.INSTANCE.getPaddingHeight() : paddingHeight;
    }

    public void setPaddingHeight(@Nullable LengthProperty paddingHeight) {
        this.paddingHeight = paddingHeight;
    }

    @Nonnull
    public LengthProperty getBorderWidth() {
        return borderWidth == null ? DefaultStyling.INSTANCE.getBorderWidth() : borderWidth;
    }

    public void setBorderWidth(@Nullable LengthProperty borderWidth) {
        this.borderWidth = borderWidth;
    }

    @Nonnull
    public LengthProperty getBorderHeight() {
        return borderHeight == null ? DefaultStyling.INSTANCE.getBorderHeight() : borderHeight;
    }

    public void setBorderHeight(@Nullable LengthProperty borderHeight) {
        this.borderHeight = borderHeight;
    }

    ///

    @Nonnull
    public ColourProperty getOuterBackgroundColour() {
        return outerBackgroundColour == null ? DefaultStyling.INSTANCE.getOuterBackgroundColour() : outerBackgroundColour;
    }

    public void setOuterBackgroundColour(@Nullable ColourProperty outerBackgroundColour) {
        this.outerBackgroundColour = outerBackgroundColour;
    }

    @Nonnull
    public ColourProperty getInnerBackgroundColour() {
        return innerBackgroundColour == null ? DefaultStyling.INSTANCE.getInnerBackgroundColour() : innerBackgroundColour;
    }

    public void setInnerBackgroundColour(@Nullable ColourProperty innerBackgroundColour) {
        this.innerBackgroundColour = innerBackgroundColour;
    }

    @Nonnull
    public ColourProperty getBorderColour() {
        return borderColour == null ? DefaultStyling.INSTANCE.getBorderColour() : borderColour;
    }

    public void setBorderColour(@Nonnull ColourProperty borderColour) {
        this.borderColour = borderColour;
    }

    ///

    @Nonnull
    public ColourProperty getEnabledTextColour() {
        return enabledTextColour == null ? DefaultStyling.INSTANCE.getEnabledTextColour() : enabledTextColour;
    }

    public void setEnabledTextColour(@Nullable ColourProperty enabledTextColour) {
        this.enabledTextColour = enabledTextColour;
    }

    @Nonnull
    public ColourProperty getHoveredTextColour() {
        return hoveredTextColour == null ? DefaultStyling.INSTANCE.getHoveredTextColour() : hoveredTextColour;
    }

    public void setHoveredTextColour(@Nullable ColourProperty hoveredTextColour) {
        this.hoveredTextColour = hoveredTextColour;
    }

    @Nonnull
    public ColourProperty getDisabledTextColour() {
        return disabledTextColour == null ? DefaultStyling.INSTANCE.getDisabledTextColour() : disabledTextColour;
    }

    public void setDisabledTextColour(@Nullable ColourProperty disabledTextColour) {
        this.disabledTextColour = disabledTextColour;
    }


}