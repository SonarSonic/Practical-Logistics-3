package sonar.logistics.client.gsi.components.text.render;

import sonar.logistics.client.gsi.components.text.style.LineStyle;

//TODO FIX JUSTIFY & LINE STYLING!
public class StyledTextLine extends GlyphMetric {

    public LineStyle lineStyle;
    public float lineScaling = 1;
    public boolean isOffPage = false;
    public StyledTextLine(LineStyle lineStyle){
        super();
        this.lineStyle = lineStyle;
    }

    @Override
    public void addGlyphInfo(GlyphRenderInfo info){
        if(isOffPage){
            ///add off page glyphs ?
            return;
        }
        super.addGlyphInfo(info);
    }

    public void justifyLine(double boundsWidth, boolean isLastLine){
        if(!isLastLine){
            /*TODO
            long spaceCount = glyphs.stream().filter(IGlyphType::isSpace).count();

            float spaceSize = (float) (boundsWidth - (renderSize.width - spaceCount));
            justifySpaceSize = spaceSize / spaceCount;
            renderSize.width = (float)boundsWidth; //justified lines should always take up the max width

             */
        }
    }
}
