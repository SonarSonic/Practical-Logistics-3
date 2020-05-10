package sonar.logistics.client.gsi.components.text;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.components.text.api.IGlyphString;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.glyph.StyleGlyph;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;

import java.util.ArrayList;
import java.util.List;

public class StyledTextWrapper {

    public List<List<CachedGlyphLine>> cachedGlyphPages = new ArrayList<>();

    public StyledTextWrapper(){}

    //// WRAPPING \\\\

    private void startWrapping(){
        cachedGlyphPages.clear();
        currentPage = null;
        currentLine = null;
        isBuildingPage = false;
        isBuildingLine = false;
    }

    private void finishWrapping(){}

    //// PAGE BUILDING \\\\

    float pageYOffset = 0;
    List<CachedGlyphLine> currentPage;
    boolean isBuildingPage = false;

    private void startPage(){
        if(!isBuildingPage) {
            currentPage = new ArrayList<>();
            pageYOffset = 0;
            isBuildingPage = true;
        }
    }

    private void finishPage(){
        if(isBuildingPage) {
            if (!currentPage.isEmpty()) {
                cachedGlyphPages.add(currentPage);
            }
            isBuildingPage = false;
        }
    }

    //// LINE BUILDING \\\\

    CachedGlyphLine lastLine;
    CachedGlyphLine currentLine;
    boolean isBuildingLine = false;

    private void startLine(){
        if(!isBuildingLine) {
            currentLine = new CachedGlyphLine(currentLineStyling.copy(), currentGlyphStyling.copy());
            isBuildingLine = true;
        }
    }

    private void finishLine(boolean lineBreak){
        if(isBuildingLine) {
            lastLine = currentLine;
            if (!currentLine.glyphs.isEmpty()) {
                currentLine.finish(this, lineBreak);

                if(pageYOffset + currentLine.lineHeight > currentSizing.getY()){
                    ///the line is too big for the current page
                    finishPage();
                    startPage();
                }

                currentPage.add(currentLine);
                currentLine.offsetY = pageYOffset;
                pageYOffset += lastLine.lineHeight;

            }
            isBuildingLine = false;
            if(lastLine.remainder != null){
                startLine();
                lastLine.remainder.forEach(this::addGlyph); // the line we checked is now the last line...
            }
        }
    }

    //// WORD BUILDING \\\\



    public ScaledFontType fontType;
    public LineStyle currentLineStyling;
    public GlyphStyle currentGlyphStyling;
    public Vec3d currentSizing;

    public void wrap(ScaledFontType font, List<IGlyphString> glyphElements, GlyphStyle parentStyling, Vec3d maxSizing) {
        fontType = font;
        currentGlyphStyling = parentStyling;
        currentSizing = maxSizing;
        startWrapping();
        startPage();

        glyphElements.forEach(strings -> strings.getGlyphs().forEach(this::addGlyph));

        finishPage();
        finishWrapping();
    }

    public void addGlyph(IGlyphType glyph) {

        ///// SPECIAL TYPES \\\\\

        if(glyph instanceof LineBreakGlyph){
            currentLineStyling = ((LineBreakGlyph) glyph).styling;
            if(((LineBreakGlyph) glyph).page){
                finishLine(false);
                finishPage();

                startPage();
                startLine();
            }else{
                finishLine(true);
                startLine();
            }
            return;
        }
        if(currentLine.isOffPage){
            ////the line has filled the max width and wrapping is off
            return;
        }
        if(glyph instanceof StyleGlyph){
            ////glyphs style will be added, this allows rendering to perform as expected.
            currentGlyphStyling = ((StyleGlyph)glyph).alterStyle(currentGlyphStyling.copy());
            currentLine.addGlyph(this, glyph, 0, 0);
            return;
        }

        ///// STANDARD GLYPHS \\\\\

        float renderWidth = glyph.getRenderWidth(fontType, currentGlyphStyling);
        float renderHeight = glyph.getRenderHeight(fontType, currentGlyphStyling);

        if(currentLine.lineStyling.wrappingType.canRescale()){
            ////glyph will be rescaled to fit the line
            currentLine.addGlyph(this, glyph, renderWidth, renderHeight);
            return;
        }

        if(renderWidth > currentSizing.getX() && renderHeight > currentSizing.getY()){
            ////glyph is too big for the page size
            return;
        }
        if(currentLine.lineWidth + renderWidth > currentSizing.getX()){
            ////glyph is too big for the current line
            if(!currentLine.lineStyling.wrappingType.canWrap()) {
                currentLine.isOffPage = true;
                return;
            }

            finishLine(false);
            startLine();
        }
        currentLine.addGlyph(this, glyph, renderWidth, renderHeight);
    }

    public static class CachedGlyphLine{

        public float offsetY;
        public float offsetX;
        public float lineScaling, lineWidth, lineHeight;

        public int lastSpaceIndex = -1;
        public int justifySpaceCount = 0;
        public float justifyTotalWidth = 0;
        public float justifySpaceSize = 0;

        public LineStyle lineStyling;
        public GlyphStyle parentStyling;
        public List<IGlyphType> glyphs;
        public boolean isOffPage;

        protected List<IGlyphType> remainder;

        public CachedGlyphLine(LineStyle lineStyling, GlyphStyle parentStyling){
            this.lineStyling = lineStyling;
            this.parentStyling = parentStyling;
            this.glyphs = new ArrayList<>();
            this.lineScaling = 1;
        }

        public void addGlyph(StyledTextWrapper wrapper, IGlyphType glyph, float renderWidth, float renderHeight){

            if(glyph.isSpace()){
                justifyTotalWidth +=renderWidth;
                justifySpaceCount ++;
                lastSpaceIndex = glyphs.size();
            }

            glyphs.add(glyph);
            lineWidth += renderWidth;
            lineHeight = Math.max(lineHeight, renderHeight);
        }

        public void rebuildLine(StyledTextWrapper wrapper, List<IGlyphType> newGlyphs, GlyphStyle parentStyling){
            glyphs.clear();
            lineWidth = 0;
            lineHeight = 0;
            lastSpaceIndex = -1;
            justifySpaceCount = 0;
            justifyTotalWidth = 0;
            justifySpaceSize = 0;

            GlyphStyle currentStyling = parentStyling.copy();
            for(IGlyphType glyph : newGlyphs) {
                if (glyph instanceof StyleGlyph) {
                    currentStyling = ((StyleGlyph) glyph).alterStyle(currentStyling);
                }
                addGlyph(wrapper, glyph, glyph.getRenderWidth(wrapper.fontType, currentStyling), glyph.getRenderHeight(wrapper.fontType, currentStyling));
            }
        }

        public void finish(StyledTextWrapper wrapper, boolean lineBreak){
            if(!lineBreak && lineStyling.breakPreference.shouldBreakAtSpace()){

                //// finds the last space, removes it then adds to the next line. also has the effect of removing stray spaces from the end of broken lines
                if(lastSpaceIndex != -1){
                    remainder = new ArrayList<>();
                    List<IGlyphType> newGlyphs = new ArrayList<>();
                    GlyphStyle currentStyling = parentStyling;
                    for(int i = 0; i < glyphs.size(); i++){
                        IGlyphType glyph = glyphs.get(i);
                        if(glyph instanceof StyleGlyph){
                            currentStyling = ((StyleGlyph) glyph).alterStyle(currentStyling);
                        }
                        if(i > lastSpaceIndex) {
                            remainder.add(glyph);
                        }else if(i != lastSpaceIndex){
                            newGlyphs.add(glyph);
                        }
                    }
                    rebuildLine(wrapper, newGlyphs, parentStyling);
                }
            }

            if(lineStyling.wrappingType.canRescale()){
                if(lineStyling.wrappingType == LineStyle.WrappingType.SCALED_FIT && lineWidth <= wrapper.currentSizing.getX()) {
                    return; //don't rescale if line can already fit
                }
                lineScaling = (float)wrapper.currentSizing.getX() / lineWidth;
                lineWidth = (float)wrapper.currentSizing.getX();
                lineHeight = lineHeight * lineScaling;
            }
            lineHeight += lineStyling.lineSpacing;

            switch (lineStyling.alignType){
                case ALIGN_TEXT_LEFT:
                    break;
                case CENTER:
                    offsetX += wrapper.currentSizing.getX()/2 - lineWidth/2;
                    break;
                case ALIGN_TEXT_RIGHT:
                    offsetX = (float)wrapper.currentSizing.getX()-lineWidth;
                    break;
                case JUSTIFY:
                    if(!lineBreak) {
                        float size = (float) (wrapper.currentSizing.getX() - (lineWidth - justifyTotalWidth));
                        justifySpaceSize = size / justifySpaceCount;
                        lineWidth = (float)wrapper.currentSizing.getX(); //justified lines should always take up the max width
                    }
                    break;
            }
        }
    }
}
