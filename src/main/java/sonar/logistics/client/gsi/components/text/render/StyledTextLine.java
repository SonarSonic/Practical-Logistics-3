package sonar.logistics.client.gsi.components.text.render;

import sonar.logistics.client.gsi.components.text.style.LineStyle;

//TODO FIX JUSTIFY & LINE SCALING!
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
            return;
        }
        super.addGlyphInfo(info);
    }
}
