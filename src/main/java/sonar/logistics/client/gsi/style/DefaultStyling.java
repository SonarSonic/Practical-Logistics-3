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
        marginWidth = new LengthProperty(Unit.PIXEL, 0.0625F/2F);
        marginHeight = new LengthProperty(Unit.PIXEL, 0.0625F/2F);
        paddingWidth = new LengthProperty(Unit.PIXEL, 0);
        paddingHeight = new LengthProperty(Unit.PIXEL, 0);
        borderWidth = new LengthProperty(Unit.PIXEL, 0);
        borderHeight = new LengthProperty(Unit.PIXEL, 0);

        ///component colours
        outerBackgroundColour = new ColourProperty(0, 0,0, 0);
        innerBackgroundColour = new ColourProperty(0, 0,0, 0);
        borderColour = new ColourProperty(255, 255, 255, 255);

        ///text colours
        enabledTextColour = new ColourProperty(0, 0,0, 0);
        hoveredTextColour = new ColourProperty(0, 0,0, 0);
        disabledTextColour = new ColourProperty(0, 0,0, 0);
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

    @Nonnull
    @Override
    public LengthProperty getMarginWidth() {
        return marginWidth;
    }

    @Nonnull
    @Override
    public LengthProperty getMarginHeight() {
        return marginHeight;
    }

    @Nonnull
    @Override
    public LengthProperty getPaddingWidth() {
        return paddingWidth;
    }

    @Nonnull
    @Override
    public LengthProperty getPaddingHeight() {
        return paddingHeight;
    }

    @Nonnull
    @Override
    public LengthProperty getBorderWidth() {
        return borderWidth;
    }

    @Nonnull
    @Override
    public LengthProperty getBorderHeight() {
        return borderHeight;
    }

    @Nonnull
    @Override
    public ColourProperty getOuterBackgroundColour() {
        return outerBackgroundColour;
    }

    @Nonnull
    @Override
    public ColourProperty getInnerBackgroundColour() {
        return innerBackgroundColour;
    }

    @Nonnull
    @Override
    public ColourProperty getBorderColour() {
        return borderColour;
    }

    @Nonnull
    @Override
    public ColourProperty getEnabledTextColour() {
        return enabledTextColour;
    }

    @Nonnull
    @Override
    public ColourProperty getHoveredTextColour() {
        return hoveredTextColour;
    }

    @Nonnull
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
