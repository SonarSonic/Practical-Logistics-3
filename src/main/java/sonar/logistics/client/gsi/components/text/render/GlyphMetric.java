package sonar.logistics.client.gsi.components.text.render;

import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.vectors.Quad2D;

import java.util.ArrayList;
import java.util.List;

public class GlyphMetric {

    public List<GlyphRenderInfo> glyphInfo;
    public Quad2D renderSize;


    public GlyphMetric(){
        this.glyphInfo = new ArrayList<>();
        this.renderSize = new Quad2D();
    }

    public void addGlyph(int index, Glyph glyph, GlyphStyle style, double width, double height){
        addGlyphInfo(new GlyphRenderInfo(index, glyph, style, width, height));
    }

    public void addGlyphInfo(GlyphRenderInfo info){
        glyphInfo.add(info);
        renderSize.width += info.quad.width;
        renderSize.height = Math.max(renderSize.height, info.quad.height);
    }

    public void addMetric(GlyphMetric metric){
        glyphInfo.addAll(metric.glyphInfo);
        renderSize.width += metric.renderSize.width;
        renderSize.height = Math.max(renderSize.height, metric.renderSize.height);
    }

    public int getStartIndex(){
        return glyphInfo.get(0).index;
    }

    public int getEndIndex(){
        return glyphInfo.get(glyphInfo.size()-1).index;
    }
}
