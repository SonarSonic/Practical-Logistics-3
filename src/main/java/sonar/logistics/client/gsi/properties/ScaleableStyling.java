package sonar.logistics.client.gsi.properties;

import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.common.multiparts.displays.old.info.elements.base.ElementAlignment;

public class ScaleableStyling {

    public ElementAlignment alignX = ElementAlignment.CENTERED;
    public ElementAlignment alignY = ElementAlignment.CENTERED;

    public ColourProperty textColour = new ColourProperty(255, 255, 255, 255);
    public ColourProperty bgdColour = new ColourProperty(0, 0, 0, 0);;
    public ColourProperty borderColour = new ColourProperty(200, 200, 200, 255);

    public BorderProperty marginWidth = new BorderProperty(0, false);
    public BorderProperty marginHeight = new BorderProperty(0, false);

    public BorderProperty borderSize = new BorderProperty(0, false);
    public BorderProperty borderPadding = new BorderProperty(0, false);

    public Quad2D getRenderSizing(Quad2D bounds){

        Quad2D renderSize = new Quad2D(0, 0, bounds.getWidth(), bounds.getHeight());

        renderSize.width -= marginWidth.getRenderSize((float)bounds.getWidth())*2;
        renderSize.height -= marginHeight.getRenderSize((float)bounds.getHeight())*2;

        if(borderSize.value != 0) {
            renderSize.width -= borderSize.getRenderSize((float)bounds.getWidth())*2;
            renderSize.height -= borderSize.getRenderSize((float)bounds.getHeight())*2;

            renderSize.width -= borderPadding.getRenderSize((float)bounds.getWidth())*2;
            renderSize.height -= borderPadding.getRenderSize((float)bounds.getHeight())*2;
        }
        return renderSize.align(bounds, alignX, alignY);
    }

}