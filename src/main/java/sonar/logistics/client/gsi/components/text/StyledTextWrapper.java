package sonar.logistics.client.gsi.components.text;

import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.render.GlyphMetric;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.render.StyledTextLine;
import sonar.logistics.client.gsi.components.text.render.StyledTextPages;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.vectors.Quad2D;

import java.util.ArrayList;
import java.util.List;

/**used for wrapping styled strings within a specified bounds, it also will break strings into seperate pages*/
public class StyledTextWrapper {

    public static StyledTextWrapper INSTANCE = new StyledTextWrapper();

    private StyledTextWrapper(){}

    /// building variables
    private ScaledFontType fontType;
    private Quad2D bounds;
    private int index;

    private LineStyle currentLineStyle;
    private boolean currentPageBreak;

    ///outputs
    public StyledTextPages styledTextPages;

    // builds wrapping pages
    public void build(StyledTextPages pages, ScaledFontType fontType, Quad2D bounds){
        pages.clearCache();
        this.fontType = fontType;
        this.bounds = bounds;
        this.index = 0;
        this.currentLineStyle = new LineStyle();
        this.currentPageBreak = false;
        this.styledTextPages = pages;

        //build the raw lines from line breaks
        startPage();
        startRawLine();
        for(index = 0; index < styledTextPages.text.glyphs.size(); index ++){
            Glyph glyph = styledTextPages.text.glyphs.get(index);

            if(glyph instanceof LineBreakGlyph){
                finishRawLine();

                currentLineStyle = ((LineBreakGlyph) glyph).styling;
                currentPageBreak = ((LineBreakGlyph) glyph).page;

                startRawLine();
            }

            addGlyphToRawLine(glyph);
        }
        finishRawLine();
        finishPage();
    }


    ///// building pages

    List<StyledTextLine> page;
    double pageYAdvance;

    public void startPage(){
        page = new ArrayList<>();
        pageYAdvance = 0;
    }

    public void finishPage(){
        if(!page.isEmpty()) {
            styledTextPages.styledPages.add(page);
            styledTextPages.styledLines.addAll(page);
        }
    }


    ///// building raw lines

    List<StyledTextLine> styledLines;

    public void startRawLine(){
        styledLines = new ArrayList<>();
        startStyledLine();
        startGlyphMetric();
    }

    public void finishRawLine(){
        finishGlyphMetric();
        finishStyledLine();
        addStyledLinesToPage(styledLines);
    }


    ///// building glyph metrics - these are typically words, but can be others too.

    GlyphMetric metric;
    public void startGlyphMetric(){
        metric = new GlyphMetric();
    }

    public void finishGlyphMetric(){
        addGlyphMetricToStyledLine(metric);
    }


    ///// building styled lines

    StyledTextLine line;
    public void startStyledLine(){
        line = new StyledTextLine(currentLineStyle);
    }

    public void finishStyledLine(){
        addStyledLineToRawLine(line);
    }


    ///// raw metric building

    public void addGlyphToRawLine(Glyph glyph){
        GlyphStyle style = glyph.getStyle();
        float renderWidth = glyph.getRenderWidth(fontType, style);
        float renderHeight = glyph.getRenderHeight(fontType, style);
        GlyphRenderInfo renderInfo = new GlyphRenderInfo(index, glyph, style, renderWidth, renderHeight);
        styledTextPages.styledGlyphs.add(renderInfo);

        if(currentLineStyle.breakPreference.isWordBreaker(glyph)){
            finishGlyphMetric();
            addGlyphInfoToStyledLine(renderInfo);
            startGlyphMetric();
        }else {
            addGlyphInfoToGlyphMetric(renderInfo);
        }
    }

    public void addGlyphInfoToGlyphMetric(GlyphRenderInfo glyphInfo){
        metric.addGlyphInfo(glyphInfo);
    }

    ///// styled metric building

    //add the finished glyph metric to the styled line, called by finishRawWord()
    public void addGlyphMetricToStyledLine(GlyphMetric metric){

        if(checkAdvance(metric.renderSize.width, line.renderSize.width)){
            ///first check the metric can fit on the current line, if it can no further action is needed
            line.addMetric(metric);
        } else if(bounds.canFit(metric.renderSize)){
            //if the metric can fit within the bounds without being split we will place it on the next line
            finishStyledLine();
            startStyledLine();
            line.addMetric(metric);
        } else {
            //if the metric doesn't fit on any line we will add each glyph individually
            metric.glyphInfo.forEach(this::addGlyphInfoToStyledLine);
        }
    }

    //add a single glyph to the styled line
    public void addGlyphInfoToStyledLine(GlyphRenderInfo glyphInfo){

        //check the glyph is not big for the scaling! - TODO add error message?
        if(!currentLineStyle.wrappingType.canRescale() && !bounds.canFit(glyphInfo.quad.width, glyphInfo.quad.height)) {
            return;
        }

        //if the glyph can fit on the current line we add it
        if(checkAdvance(glyphInfo.quad.width, line.renderSize.width)){
            line.addGlyphInfo(glyphInfo);
            return;
        }

        //if wrapping is disabled we will add too the end of the line
        if(currentLineStyle.wrappingType.canWrap()){
            finishStyledLine();
            startStyledLine();
            line.addGlyphInfo(glyphInfo);
        }else{
            line.isOffPage = true;
            line.addGlyphInfo(glyphInfo);
        }
    }


    //// styled page building

    public void addStyledLineToRawLine(StyledTextLine line){
        if(!line.glyphInfo.isEmpty()) {
            styledLines.add(line);
        }
    }

    public void addStyledLinesToPage(List<StyledTextLine> styledLines){

        //if the line causes a page break do it here
        if(currentPageBreak){
            finishPage();
            startPage();
        }

        ///add styled lines to page
        for(StyledTextLine line : styledLines){
            if(!(line.renderSize.height + pageYAdvance <= bounds.height)){
                finishPage();
                startPage();
            }
            line.renderSize.x = bounds.getX();
            line.renderSize.y = bounds.getY() + pageYAdvance;
            pageYAdvance += line.renderSize.height;
            page.add(line);
        }

        ///alignment
        alignStyledLines(styledLines);
    }


    //// styled line alignment

    public void alignStyledLines(List<StyledTextLine> styledLines){

        ////update the final width / height
        int lineIndex = 0;
        for(StyledTextLine line : styledLines){
            if(line.lineStyle.wrappingType.canRescale()){
                if(line.lineStyle.wrappingType == LineStyle.WrappingType.SCALED_FIT && line.renderSize.width <= bounds.width) {
                    return; //don't rescale if line can already fit
                }
                line.lineScaling = (float)(bounds.width / line.renderSize.width);
                line.renderSize.width = (float)bounds.width;
                line.renderSize.height = line.renderSize.height * line.lineScaling;
                //TODO FIX LINE SCALING FOR GLYPH RENDER SIZE!
            }
            line.renderSize.height += line.lineStyle.lineSpacing;
            lineIndex ++;

            ////align
            boolean isLast = lineIndex == styledLines.size();
            currentLineStyle.alignType.align(line, bounds.width, isLast);

            double offsetX = 0;
            for(GlyphRenderInfo glyphInfo : line.glyphInfo){
                glyphInfo.quad.x = line.renderSize.getX() + offsetX;
                glyphInfo.quad.y = line.renderSize.getY();
                offsetX += glyphInfo.quad.width;
            }
        }

    }


    //// HELPER METHODS

    public boolean checkAdvance(double add, double lineAdvance){

        //if the line can rescale the words will fit
        if(currentLineStyle.wrappingType.canRescale()){
            return true;
        }

        //if word can fit without rescaling add it to the line
        return add + lineAdvance <= bounds.width;
    }

}
