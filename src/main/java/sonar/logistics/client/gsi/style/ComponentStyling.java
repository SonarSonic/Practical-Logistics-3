package sonar.logistics.client.gsi.style;

import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.Length;
import sonar.logistics.client.gsi.style.properties.UnitLength;
import sonar.logistics.client.gsi.style.properties.UnitType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ComponentStyling {

    protected Boolean absolute;

    ///TODO ALIGNMENT? //TODO RATIOS?

    ///positioning
    protected Integer zLayer;
    protected Length xPos;
    protected Length yPos;

    ////sizing
    protected Length width;
    protected Length minWidth;
    protected Length maxWidth;

    protected Length height;
    protected Length minHeight;
    protected Length maxHeight;

    ///offsets
    protected Length marginWidth;
    protected Length marginHeight;
    protected Length paddingWidth;
    protected Length paddingHeight;
    protected Length borderWidth;
    protected Length borderHeight;

    ///component colours
    protected ColourProperty outerBackgroundColour;
    protected ColourProperty innerBackgroundColour;
    protected ColourProperty borderColour;

    ///text colours
    protected ColourProperty normalTextColour;
    protected ColourProperty hoveredTextColour;
    protected ColourProperty disabledTextColour;

    ///

    public ComponentStyling alterStyling(Consumer<ComponentStyling> alter){
        alter.accept(this);
        return this;
    }

    public ComponentStyling setSizing(double xPos, double yPos, double width, double height, UnitType type){
        return setSizing(new UnitLength(type, xPos), new UnitLength(type, yPos), new UnitLength(type, width), new UnitLength(type, height));
    }

    public ComponentStyling setSizing(Length xPos, Length yPos, Length width, Length height){
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
    public Length getXPos() {
        return xPos == null ? DefaultStyling.INSTANCE.getXPos() : xPos;
    }

    public void setXPos(@Nonnull Length xPos) {
        this.xPos = xPos;
    }

    @Nonnull
    public Length getYPos() {
        return yPos == null ? DefaultStyling.INSTANCE.getYPos() : yPos;
    }

    public void setYPos(@Nonnull Length yPos) {
        this.yPos = yPos;
    }

    ///

    @Nonnull
    public Length getWidth() {
        return width == null ? DefaultStyling.INSTANCE.getWidth() : width;
    }

    public void setWidth(@Nonnull Length width) {
        this.width = width;
    }

    @Nonnull
    public Length getHeight() {
        return height == null ? DefaultStyling.INSTANCE.getHeight() : height;
    }

    public void setHeight(@Nonnull Length height) {
        this.height = height;
    }

    ///

    @Nullable
    public Length getMarginWidth() {
        return marginWidth == null ? DefaultStyling.INSTANCE.getMarginWidth() : marginWidth;
    }

    public void setMarginWidth(@Nullable Length marginWidth) {
        this.marginWidth = marginWidth;
    }

    @Nullable
    public Length getMarginHeight() {
        return marginHeight == null ? DefaultStyling.INSTANCE.getMarginHeight() : marginHeight;
    }

    public void setMarginHeight(@Nullable Length marginHeight) {
        this.marginHeight = marginHeight;
    }

    @Nullable
    public Length getPaddingWidth() {
        return paddingWidth == null ? DefaultStyling.INSTANCE.getPaddingWidth() : paddingWidth;
    }

    public void setPaddingWidth(@Nullable Length paddingWidth) {
        this.paddingWidth = paddingWidth;
    }

    @Nullable
    public Length getPaddingHeight() {
        return paddingHeight == null ? DefaultStyling.INSTANCE.getPaddingHeight() : paddingHeight;
    }

    public void setPaddingHeight(@Nullable Length paddingHeight) {
        this.paddingHeight = paddingHeight;
    }

    @Nullable
    public Length getBorderWidth() {
        return borderWidth == null ? DefaultStyling.INSTANCE.getBorderWidth() : borderWidth;
    }

    public void setBorderWidth(@Nullable Length borderWidth) {
        this.borderWidth = borderWidth;
    }

    @Nullable
    public Length getBorderHeight() {
        return borderHeight == null ? DefaultStyling.INSTANCE.getBorderHeight() : borderHeight;
    }

    public void setBorderHeight(@Nullable Length borderHeight) {
        this.borderHeight = borderHeight;
    }

    ///

    @Nullable
    public ColourProperty getOuterBackgroundColour() {
        return outerBackgroundColour == null ? DefaultStyling.INSTANCE.getOuterBackgroundColour() : outerBackgroundColour;
    }

    public void setOuterBackgroundColour(@Nullable ColourProperty outerBackgroundColour) {
        this.outerBackgroundColour = outerBackgroundColour;
    }

    @Nullable
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

    @Nullable
    public ColourProperty getNormalTextColour() {
        return normalTextColour == null ? DefaultStyling.INSTANCE.getNormalTextColour() : normalTextColour;
    }

    public void setNormalTextColour(@Nullable ColourProperty normalTextColour) {
        this.normalTextColour = normalTextColour;
    }

    @Nullable
    public ColourProperty getHoveredTextColour() {
        return hoveredTextColour == null ? DefaultStyling.INSTANCE.getHoveredTextColour() : hoveredTextColour;
    }

    public void setHoveredTextColour(@Nullable ColourProperty hoveredTextColour) {
        this.hoveredTextColour = hoveredTextColour;
    }

    @Nullable
    public ColourProperty getDisabledTextColour() {
        return disabledTextColour == null ? DefaultStyling.INSTANCE.getDisabledTextColour() : disabledTextColour;
    }

    public void setDisabledTextColour(@Nullable ColourProperty disabledTextColour) {
        this.disabledTextColour = disabledTextColour;
    }


}