package sonar.logistics.client.gsi.components.layouts.yoga;

import sonar.logistics.client.gsi.style.properties.UnitLength;

import javax.annotation.Nullable;

public class YogaStyling extends YogaStylingBase {

    public YogaStylingBase PARENT = YogaStylingDefault.INSTANCE;

    @Override
    public int getDirection() {
        return direction == null ? PARENT.getDirection() : direction;
    }

    @Override
    public int getFlexDirection() {
        return flexDirection == null ? PARENT.getFlexDirection() : flexDirection;
    }

    @Override
    public UnitLength getFlexBasis() {
        return flexBasis == null ? PARENT.getFlexBasis() : flexBasis;
    }

    @Override
    public float getFlexGrow() {
        return flexGrow == null ? PARENT.getFlexGrow() : flexGrow;
    }

    @Override
    public float getFlexShrink() {
        return flexShrink == null ? PARENT.getFlexShrink() : flexShrink;
    }

    @Override
    public int getFlexWrap() {
        return flexWrap == null ? PARENT.getFlexWrap() : flexWrap;
    }

    @Override
    public int getJustifyContent() {
        return justifyContent == null ? PARENT.getJustifyContent() : justifyContent;
    }

    @Override
    public int getAlignItems() {
        return alignItems == null ? PARENT.getAlignItems() : alignItems;
    }

    @Override
    public int getAlignSelf() {
        return alignSelf == null ? PARENT.getAlignSelf() : alignSelf;
    }

    @Override
    public int getAlignContent() {
        return alignContent == null ? PARENT.getAlignContent() : alignContent;
    }

    @Override
    public UnitLength getWidth() {
        return width == null ? PARENT.getWidth() : width;
    }

    @Override
    public UnitLength getHeight() {
        return height == null ? PARENT.getHeight() : height;
    }

    @Nullable
    @Override
    public UnitLength getMaxWidth() {
        return maxWidth == null ? PARENT.getMaxWidth() : maxWidth;
    }

    @Nullable
    @Override
    public UnitLength getMaxHeight() {
        return maxHeight == null ? PARENT.getMaxHeight() : maxHeight;
    }

    @Nullable
    @Override
    public UnitLength getMinWidth() {
        return minWidth == null ? PARENT.getMinWidth() : minWidth;
    }

    @Nullable
    @Override
    public UnitLength getMinHeight() {
        return minHeight == null ? PARENT.getMinHeight() : minHeight;
    }

    @Nullable
    @Override
    public Float getAspectRatio() {
        return aspectRatio == null ? PARENT.getAspectRatio() : aspectRatio;
    }

    @Override
    public UnitLength getPaddingRadius() {
        return paddingRadius == null ? PARENT.getPaddingRadius() : paddingRadius;
    }

    @Override
    public float getBorderRadius() {
        return borderRadius == null ? PARENT.getBorderRadius() : borderRadius;
    }

    @Override
    public UnitLength getMarginRadius() {
        return marginRadius == null ? PARENT.getMarginRadius() : marginRadius;
    }

    @Override
    public boolean isAbsolute() {
        return isAbsolute == null ? PARENT.isAbsolute() : isAbsolute;
    }

    @Override
    public UnitLength getLeft() {
        return left == null ? PARENT.getLeft() : left;
    }

    @Override
    public UnitLength getRight() {
        return right == null ? PARENT.getRight() : right;
    }

    @Override
    public UnitLength getTop() {
        return top == null ? PARENT.getTop() : top;
    }

    @Override
    public UnitLength getBottom() {
        return bottom == null ? PARENT.getBottom() : bottom;
    }
}
