package sonar.logistics.client.gsi.style;

import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.Length;
import sonar.logistics.client.gsi.style.properties.UnitLength;
import sonar.logistics.client.gsi.style.properties.UnitType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DefaultStyling extends ComponentStyling {

    public static final DefaultStyling INSTANCE = new DefaultStyling();

    {

        absolute = false;
        zLayer = 0;

        ///positioning
        xPos = new UnitLength(UnitType.PERCENT, 0);
        yPos = new UnitLength(UnitType.PERCENT, 0);

        ////sizing
        width = new UnitLength(UnitType.PERCENT, 1);
        height = new UnitLength(UnitType.PERCENT, 1);

        ///offsets
        marginWidth = new UnitLength(UnitType.PIXEL, 0.0625/2);
        marginHeight = new UnitLength(UnitType.PIXEL, 0.0625/2);
        paddingWidth = null;
        paddingHeight = null;
        borderWidth = null;
        borderHeight = null;

        ///component colours
        outerBackgroundColour = null;
        innerBackgroundColour = null;
        borderColour = new ColourProperty(255, 255, 255, 255);

        ///text colours
        normalTextColour = null;
        hoveredTextColour = null;
        disabledTextColour = null;
    }

    @Nonnull
    @Override
    public Boolean isAbsolute() {
        return absolute;
    }

    @Nonnull
    @Override
    public Integer getZLayer() {
        return zLayer;
    }

    @Nonnull
    @Override
    public Length getXPos() {
        return xPos;
    }

    @Nonnull
    @Override
    public Length getYPos() {
        return yPos;
    }

    @Nonnull
    @Override
    public Length getWidth() {
        return width;
    }

    @Nonnull
    @Override
    public Length getHeight() {
        return height;
    }

    @Nullable
    @Override
    public Length getMarginWidth() {
        return marginWidth;
    }

    @Nullable
    @Override
    public Length getMarginHeight() {
        return marginHeight;
    }

    @Nullable
    @Override
    public Length getPaddingWidth() {
        return paddingWidth;
    }

    @Nullable
    @Override
    public Length getPaddingHeight() {
        return paddingHeight;
    }

    @Nullable
    @Override
    public Length getBorderWidth() {
        return borderWidth;
    }

    @Nullable
    @Override
    public Length getBorderHeight() {
        return borderHeight;
    }

    @Nullable
    @Override
    public ColourProperty getOuterBackgroundColour() {
        return outerBackgroundColour;
    }

    @Nullable
    @Override
    public ColourProperty getInnerBackgroundColour() {
        return innerBackgroundColour;
    }

    @Nonnull
    @Override
    public ColourProperty getBorderColour() {
        return borderColour;
    }

    @Nullable
    @Override
    public ColourProperty getNormalTextColour() {
        return normalTextColour;
    }

    @Nullable
    @Override
    public ColourProperty getHoveredTextColour() {
        return hoveredTextColour;
    }

    @Nullable
    @Override
    public ColourProperty getDisabledTextColour() {
        return disabledTextColour;
    }

    @Override
    public void setAbsolute(@Nonnull Boolean absolute) {}

    @Override
    public void setZLayer(Integer zLayer) {}

    @Override
    public void setXPos(@Nonnull Length xPos) {}

    @Override
    public void setYPos(@Nonnull Length yPos) {}

    @Override
    public void setWidth(@Nonnull Length width) {}

    @Override
    public void setHeight(@Nonnull Length height) {}

    @Override
    public void setMarginWidth(@Nullable Length marginWidth) {}

    @Override
    public void setMarginHeight(@Nullable Length marginHeight) {}

    @Override
    public void setPaddingWidth(@Nullable Length paddingWidth) {}

    @Override
    public void setPaddingHeight(@Nullable Length paddingHeight) {}

    @Override
    public void setBorderWidth(@Nullable Length borderWidth) {}

    @Override
    public void setBorderHeight(@Nullable Length borderHeight) {}

    @Override
    public void setOuterBackgroundColour(@Nullable ColourProperty outerBackgroundColour) {}

    @Override
    public void setInnerBackgroundColour(@Nullable ColourProperty innerBackgroundColour) {}

    @Override
    public void setBorderColour(@Nonnull ColourProperty borderColour) {}

    @Override
    public void setNormalTextColour(@Nullable ColourProperty normalTextColour) {}

    @Override
    public void setHoveredTextColour(@Nullable ColourProperty hoveredTextColour) {}

    @Override
    public void setDisabledTextColour(@Nullable ColourProperty disabledTextColour) {}
}
