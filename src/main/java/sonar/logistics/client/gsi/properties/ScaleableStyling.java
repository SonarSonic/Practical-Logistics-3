package sonar.logistics.client.gsi.properties;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;
import sonar.logistics.multiparts.displays.old.info.elements.base.ElementAlignment;

public class ScaleableStyling {

    public ElementAlignment xAlign = ElementAlignment.CENTERED;
    public ElementAlignment yAlign = ElementAlignment.CENTERED;
    public ElementAlignment zAlign = ElementAlignment.CENTERED;

    public ColourProperty textColour = new ColourProperty(255, 255, 255, 255);
    public ColourProperty bgdColour = new ColourProperty(0, 0, 0, 0);;
    public ColourProperty borderColour = new ColourProperty(200, 200, 200, 255);

    public BorderProperty marginWidth = new BorderProperty(0.0625F/2, false);
    public BorderProperty marginHeight = new BorderProperty(0.0625F/2, false);

    public BorderProperty borderSize = new BorderProperty(0.0625F/4, false);
    public BorderProperty borderPadding = new BorderProperty(0.0625F/4, false);

    public Vec3d getRenderSizing(ScaleableAlignment alignment){
        double width = alignment.getSizing().getX();
        double height = alignment.getSizing().getY();

        width-= marginWidth.getRenderSize((float)alignment.getSizing().getX())*2;
        height -= marginHeight.getRenderSize((float)alignment.getSizing().getY())*2;

        if(borderSize.value != 0) {
            width -= borderSize.getRenderSize((float)alignment.getSizing().getX())*2;
            height -= borderSize.getRenderSize((float)alignment.getSizing().getY())*2;

            width -= borderPadding.getRenderSize((float)alignment.getSizing().getX())*2;
            height -= borderPadding.getRenderSize((float)alignment.getSizing().getY())*2;
        }
        return new Vec3d(width, height,0);
    }

    public Vec3d getRenderAlignment(ScaleableAlignment alignment){
        return DisplayVectorHelper.alignArrayWithin(alignment.getRenderSizing(), alignment.getSizing(), xAlign, yAlign, zAlign);
    }

}