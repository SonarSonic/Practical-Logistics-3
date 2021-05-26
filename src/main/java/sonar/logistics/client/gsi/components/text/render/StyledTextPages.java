package sonar.logistics.client.gsi.components.text.render;

import sonar.logistics.client.gsi.components.text.StyledTextString;
import sonar.logistics.util.vectors.Vector2F;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StyledTextPages {

    ///saved
    public StyledTextString text;

    ///cached
    public List<List<StyledTextLine>> styledPages;
    public List<StyledTextLine> styledLines;
    public List<GlyphRenderInfo> styledGlyphs;

    public int page;
    public IGlyphRenderer specialGlyphRenderer = null;

    public StyledTextPages(StyledTextString text){
        this.text = text;
        this.styledPages = new ArrayList<>();
        this.styledLines = new ArrayList<>();
        this.styledGlyphs = new ArrayList<>();
    }

    public void clearCache(){
        styledPages.clear();
        styledLines.clear();
        styledGlyphs.clear();
    }

    public List<StyledTextLine> getCurrentPage(){
        if(styledPages.size() > page) {
            return styledPages.get(page);
        }
        return new ArrayList<>();
    }

    ////

    public int getPageNumber(int index){
        int pageNumber = 0;
        for(List<StyledTextLine> page : styledPages){
            if(index >= page.get(0).getStartIndex() && index <= page.get(page.size() -1).getEndIndex()){
                return pageNumber;
            }
            pageNumber++;
        }
        return -1;
    }

    public List<StyledTextLine> getStyledPage(int index){
        for(List<StyledTextLine> page : styledPages){
            if(index >= page.get(0).getStartIndex() && index <= page.get(page.size() -1).getEndIndex()){
                return page;
            }
        }
        return null;
    }

    ////

    @Nullable
    public StyledTextLine getStyledLine(int index){
        for(StyledTextLine line : styledLines){
            if(index >= line.getStartIndex() && index <= line.getEndIndex()){
                return line;
            }
        }
        return null;
    }

    @Nonnull
    public StyledTextLine getNextStyledLine(int index){
        StyledTextLine line = getStyledLine(index);
        return styledLines.get(Math.min(styledLines.size()-1, styledLines.indexOf(line) + 1));
    }

    @Nullable
    public StyledTextLine getNextStyledLine(int index, Function<StyledTextLine, Boolean> func){
        StyledTextLine line = getStyledLine(index);

        for(int i = styledLines.indexOf(line) + 1; i < styledLines.size(); i++){
            StyledTextLine renderInfo = styledLines.get(i);
            if(func.apply(renderInfo)){
                return renderInfo;
            }
        }
        return null;
    }

    @Nonnull
    public StyledTextLine getPrevStyledLine(int index){
        StyledTextLine line = getStyledLine(index);
        return styledLines.get(Math.max(0, styledLines.indexOf(line) - 1));
    }

    @Nullable
    public StyledTextLine getPrevStyledLine(int index, Function<StyledTextLine, Boolean> func){
        StyledTextLine line = getStyledLine(index);

        for(int i = styledLines.indexOf(line) - 1; i >= 0; i--){
            StyledTextLine renderInfo = styledLines.get(i);
            if(func.apply(renderInfo)){
                return renderInfo;
            }
        }
        return null;
    }

    ////

    @Nullable
    public GlyphRenderInfo getGlyphInfo(int index){
        if(styledGlyphs.isEmpty()){
            return null;
        }
        return styledGlyphs.get(index);
    }

    @Nonnull
    public GlyphRenderInfo getNextGlyphInfo(int index){
        return styledGlyphs.get(Math.min(styledGlyphs.size()-1, index));
    }

    @Nullable
    public GlyphRenderInfo getNextGlyphInfo(int index, Function<GlyphRenderInfo, Boolean> func){
        if(styledGlyphs.isEmpty()){
            return null;
        }
        for(int i = index+1; i < styledGlyphs.size(); i++){
            GlyphRenderInfo renderInfo = styledGlyphs.get(i);
            if(func.apply(renderInfo)){
                return renderInfo;
            }
        }
        return null;
    }


    @Nonnull
    public GlyphRenderInfo getPrevGlyphInfo(int index){
        return styledGlyphs.get(Math.max(0, index));
    }

    @Nullable
    public GlyphRenderInfo getPrevGlyphInfo(int index, Function<GlyphRenderInfo, Boolean> func){
        if(styledGlyphs.isEmpty()){
            return null;
        }
        for(int i = index-1; i >= 0; i--){
            GlyphRenderInfo renderInfo = styledGlyphs.get(i);
            if(func.apply(renderInfo)){
                return renderInfo;
            }
        }
        return null;
    }

    public List<GlyphRenderInfo> getSelection(int startIndex, int endIndex){
        List<GlyphRenderInfo> selection = new ArrayList<>();
        for(int i = startIndex; i <= Math.min(endIndex, styledGlyphs.size()-1) ; i++){
            selection.add(styledGlyphs.get(i));
        }
        return selection;
    }

    ///// interaction methods

    public final static Function<GlyphRenderInfo, Boolean> FILTER_VISIBLE = glyphRenderInfo -> glyphRenderInfo.quad.width != 0 && glyphRenderInfo.glyph.isVisible();
    public final static Function<GlyphRenderInfo, Boolean> FILTER_INVISIBLE = glyphRenderInfo -> !glyphRenderInfo.glyph.isVisible();

    public StyledTextLine getInteractedLine(Vector2F textHit){
        for (StyledTextLine line : getCurrentPage()) {
            if (line.renderSize.contains(textHit)) {
                return line;
            }
        }
        return null;
    }

    @Nullable
    public GlyphRenderInfo getGlyphHit(Vector2F textHit, Function<GlyphRenderInfo, Boolean> filter){
        StyledTextLine line = getInteractedLine(textHit);
        if(line != null){
            return getGlyphHit(line, filter);
        }
        return null;
    }

    @Nullable
    public GlyphRenderInfo getGlyphHit(StyledTextLine line, Function<GlyphRenderInfo, Boolean> filter){
        for(GlyphRenderInfo glyphInfo : line.glyphInfo){
            if(filter.apply(glyphInfo)){
                return glyphInfo;
            }
        }
        return null;
    }

}
