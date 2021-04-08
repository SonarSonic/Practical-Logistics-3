package sonar.logistics.client.gsi.style;

import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.client.gsi.style.properties.Unit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DefaultStyling extends ComponentStyling {

    public static final DefaultStyling INSTANCE = new DefaultStyling();

    {

        absolute = false;
        zLayer = 0;

        ///positioning
        xPos = new LengthProperty(Unit.PERCENT, 0);
        yPos = new LengthProperty(Unit.PERCENT, 0);

        ////sizing
        width = new LengthProperty(Unit.PERCENT, 1);
        height = new LengthProperty(Unit.PERCENT, 1);

        ///offsets
        marginWidth = new LengthProperty(Unit.PIXEL, 0.0625/2);
        marginHeight = new LengthProperty(Unit.PIXEL, 0.0625/2);
        paddingWidth = null;
        paddingHeight = null;
        borderWidth = null;
        borderHeight = null;

        ///component colours
        outerBackgroundColour = null;
        innerBackgroundColour = null;
        borderColour = new ColourProperty(255, 255, 255, 255);

        ///text colours
        enabledTextColour = null;
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
    public LengthProperty getXPos() {
        return xPos;
    }

    @Nonnull
    @Override
    public LengthProperty getYPos() {
        return yPos;
    }

    @Nonnull
    @Override
    public LengthProperty getWidth() {
        return width;
    }

    @Nonnull
    @Override
    public LengthProperty getHeight() {
        return height;
    }

    @Nullable
    @Override
    public LengthProperty getMarginWidth() {
        return marginWidth;
    }

    @Nullable
    @Override
    public LengthProperty getMarginHeight() {
        return marginHeight;
    }

    @Nullable
    @Override
    public LengthProperty getPaddingWidth() {
        return paddingWidth;
    }

    @Nullable
    @Override
    public LengthProperty getPaddingHeight() {
        return paddingHeight;
    }

    @Nullable
    @Override
    public LengthProperty getBorderWidth() {
        return borderWidth;
    }

    @Nullable
    @Override
    public LengthProperty getBorderHeight() {
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
    public ColourProperty getEnabledTextColour() {
        return enabledTextColour;
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
    public void setXPos(@Nonnull LengthProperty xPos) {}

    @Override
    public void setYPos(@Nonnull LengthProperty yPos) {}

    @Override
    public void setWidth(@Nonnull LengthProperty width) {}

    @Override
    public void setHeight(@Nonnull LengthProperty height) {}

    @Override
    public void setMarginWidth(@Nullable LengthProperty marginWidth) {}

    @Override
    public void setMarginHeight(@Nullable LengthProperty marginHeight) {}

    @Override
    public void setPaddingWidth(@Nullable LengthProperty paddingWidth) {}

    @Override
    public void setPaddingHeight(@Nullable LengthProperty paddingHeight) {}

    @Override
    public void setBorderWidth(@Nullable LengthProperty borderWidth) {}

    @Override
    public void setBorderHeight(@Nullable LengthProperty borderHeight) {}

    @Override
    public void setOuterBackgroundColour(@Nullable ColourProperty outerBackgroundColour) {}

    @Override
    public void setInnerBackgroundColour(@Nullable ColourProperty innerBackgroundColour) {}

    @Override
    public void setBorderColour(@Nonnull ColourProperty borderColour) {}

    @Override
    public void setEnabledTextColour(@Nullable ColourProperty enabledTextColour) {}

    @Override
    public void setHoveredTextColour(@Nullable ColourProperty hoveredTextColour) {}

    @Override
    public void setDisabledTextColour(@Nullable ColourProperty disabledTextColour) {}
}
