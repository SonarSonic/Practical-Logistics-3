package sonar.logistics.client.gsi.components.layouts.yoga;

import sonar.logistics.client.gsi.style.properties.UnitLength;

import javax.annotation.Nullable;

//use YogaStyling instead, abstract to prevent accidental usage
public abstract class YogaStylingBase {

    /// flex

    //defines the direction of which text and items are laid out
    protected Integer direction;

    //defines the direction of the main-axis
    protected Integer flexDirection;

    //wrapping behaviour when child nodes don't fit into a single line
    protected Integer flexWrap;

    //default size of a node along the main axis
    protected UnitLength flexBasis;
    //the factor of remaining space should be given to this node
    protected Float flexGrow;
    //the shrink factor of this element if parent has no space left
    protected Float flexShrink;

    /// alignment

    //aligns child nodes along the main-axis
    protected Integer justifyContent;
    //aligns child nodes along the cross-axis
    protected Integer alignItems;
    //override align items of parent
    protected Integer alignSelf;
    //alignment of lines along the cross-axis when wrapping
    protected Integer alignContent;

    /// layout

    //dimensions of the node
    protected UnitLength width;
    protected UnitLength height;

    //maximum dimensions of the node
    protected UnitLength maxWidth;
    protected UnitLength maxHeight;

    //minimum dimensions of the node
    protected UnitLength minWidth;
    protected UnitLength minHeight;

    //Width/Height aspect ratio of node
    protected Float aspectRatio;

    /// positioning

    //inner padding radius
    protected UnitLength paddingRadius;
    //border radius, surrounding the padding
    protected Float borderRadius;
    //margin radius, surrounding the border
    protected UnitLength marginRadius;

    //by default the position type will be relative
    protected Boolean isAbsolute;

    //node positions
    protected UnitLength left;
    protected UnitLength right;
    protected UnitLength top;
    protected UnitLength bottom;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getFlexDirection() {
        return flexDirection;
    }

    public void setFlexDirection(int flexDirection) {
        this.flexDirection = flexDirection;
    }

    public UnitLength getFlexBasis() {
        return flexBasis;
    }

    public void setFlexBasis(UnitLength flexBasis) {
        this.flexBasis = flexBasis;
    }

    public float getFlexGrow() {
        return flexGrow;
    }

    public void setFlexGrow(float flexGrow) {
        this.flexGrow = flexGrow;
    }

    public float getFlexShrink() {
        return flexShrink;
    }

    public void setFlexShrink(float flexShrink) {
        this.flexShrink = flexShrink;
    }

    public int getFlexWrap() {
        return flexWrap;
    }

    public void setFlexWrap(int flexWrap) {
        this.flexWrap = flexWrap;
    }

    public int getJustifyContent() {
        return justifyContent;
    }

    public void setJustifyContent(int justifyContent) {
        this.justifyContent = justifyContent;
    }

    public int getAlignItems() {
        return alignItems;
    }

    public void setAlignItems(int alignItems) {
        this.alignItems = alignItems;
    }

    public int getAlignSelf() {
        return alignSelf;
    }

    public void setAlignSelf(int alignSelf) {
        this.alignSelf = alignSelf;
    }

    public int getAlignContent() {
        return alignContent;
    }

    public void setAlignContent(int alignContent) {
        this.alignContent = alignContent;
    }

    public UnitLength getWidth() {
        return width;
    }

    public void setWidth(UnitLength width) {
        this.width = width;
    }

    public UnitLength getHeight() {
        return height;
    }

    public void setHeight(UnitLength height) {
        this.height = height;
    }

    @Nullable
    public UnitLength getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(@Nullable UnitLength maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Nullable
    public UnitLength getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(@Nullable UnitLength maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Nullable
    public UnitLength getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(@Nullable UnitLength minWidth) {
        this.minWidth = minWidth;
    }

    @Nullable
    public UnitLength getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(@Nullable UnitLength minHeight) {
        this.minHeight = minHeight;
    }

    @Nullable
    public Float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(@Nullable Float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public UnitLength getPaddingRadius() {
        return paddingRadius;
    }

    public void setPaddingRadius(UnitLength paddingRadius) {
        this.paddingRadius = paddingRadius;
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
    }

    public UnitLength getMarginRadius() {
        return marginRadius;
    }

    public void setMarginRadius(UnitLength marginRadius) {
        this.marginRadius = marginRadius;
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }

    public void setAbsolute(boolean absolute) {
        isAbsolute = absolute;
    }

    public UnitLength getLeft() {
        return left;
    }

    public void setLeft(UnitLength left) {
        this.left = left;
    }

    public UnitLength getRight() {
        return right;
    }

    public void setRight(UnitLength right) {
        this.right = right;
    }

    public UnitLength getTop() {
        return top;
    }

    public void setTop(UnitLength top) {
        this.top = top;
    }

    public UnitLength getBottom() {
        return bottom;
    }

    public void setBottom(UnitLength bottom) {
        this.bottom = bottom;
    }
}
