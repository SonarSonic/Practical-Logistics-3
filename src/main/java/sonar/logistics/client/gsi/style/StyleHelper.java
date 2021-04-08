package sonar.logistics.client.gsi.style;

import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.util.vectors.Quad2D;

public class StyleHelper {


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
        width += LengthProperty.getLengthSafe(style.getMarginWidth(), outerSize.getWidth())*2;
        width += LengthProperty.getLengthSafe(style.getPaddingWidth(), outerSize.getWidth())*2;
        width += LengthProperty.getLengthSafe(style.getBorderWidth(), outerSize.getWidth())*2;
        return width;
    }

    public static double getHeightOffset(Quad2D outerSize, ComponentStyling style){
        double height = 0;
        height += LengthProperty.getLengthSafe(style.getMarginHeight(), outerSize.getHeight())*2;
        height += LengthProperty.getLengthSafe(style.getPaddingHeight(), outerSize.getHeight())*2;
        height += LengthProperty.getLengthSafe(style.getBorderHeight(), outerSize.getHeight())*2;
        return height;
    }

}
