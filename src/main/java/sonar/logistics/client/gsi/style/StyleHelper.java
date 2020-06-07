package sonar.logistics.client.gsi.style;

import sonar.logistics.client.gsi.api.ComponentAlignment;
import sonar.logistics.client.gsi.style.properties.Length;
import sonar.logistics.client.vectors.Quad2D;

public class StyleHelper {


    public static double getLengthSafe(Length length, double maxValue) {
        return length == null ? 0 : length.getValue(maxValue);
    }

    public static float getLengthSafeF(Length length, double maxValue) {
        return (float)getLengthSafe(length, maxValue);
    }

    /*returns the maximum outer size for the component*/
    public static Quad2D getComponentOuterSize(Quad2D bounds, ComponentStyling style){
        Quad2D outerSize = new Quad2D(
                style.getXPos().getValue(bounds.width),
                style.getYPos().getValue(bounds.height),
                style.getWidth().getValue(bounds.width),
                style.getHeight().getValue(bounds.height));

        if(!style.isAbsolute()){
            outerSize.translate(bounds.getX(), bounds.getY());
        }
        return outerSize;
    }

    ///FIXME ???? should all percentage styles ref width and not height or width
    public static Quad2D getComponentInnerSize(Quad2D outerSize, ComponentStyling style){
        Quad2D innerSize = new Quad2D(0 , 0, outerSize.getWidth(), outerSize.getHeight());

        innerSize.width -= getWidthOffset(outerSize, style);
        innerSize.height -= getHeightOffset(outerSize, style);

        innerSize.align(outerSize, ComponentAlignment.CENTERED, ComponentAlignment.CENTERED);

        return innerSize;
    }

    public static double getWidthOffset(Quad2D outerSize, ComponentStyling style){
        double width = 0;
        width += getLengthSafe(style.getMarginWidth(), outerSize.getWidth())*2;
        width += getLengthSafe(style.getPaddingWidth(), outerSize.getWidth())*2;
        width += getLengthSafe(style.getBorderWidth(), outerSize.getWidth())*2;
        return width;
    }

    public static double getHeightOffset(Quad2D outerSize, ComponentStyling style){
        double height = 0;
        height += getLengthSafe(style.getMarginHeight(), outerSize.getHeight())*2;
        height += getLengthSafe(style.getPaddingHeight(), outerSize.getHeight())*2;
        height += getLengthSafe(style.getBorderHeight(), outerSize.getHeight())*2;
        return height;
    }

}
