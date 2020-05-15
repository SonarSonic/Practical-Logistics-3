package sonar.logistics.client.gsi.components.text.render;

import sonar.logistics.client.gsi.components.text.StyledTextString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StyledTextPages {


    public final static Function<GlyphRenderInfo, Boolean> FILTER_VISIBLE = glyphRenderInfo -> glyphRenderInfo.quad.width != 0 && glyphRenderInfo.glyph.isVisible();
    public final static Function<GlyphRenderInfo, Boolean> FILTER_INVISIBLE = glyphRenderInfo -> !glyphRenderInfo.glyph.isVisible();

    public StyledTextString string;

    public List<List<StyledTextLine>> pages;
    public List<StyledTextLine> lines;
    public List<GlyphRenderInfo> glyphs;

    public StyledTextPages(StyledTextString string){
        this.string = string;
        this.pages = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.glyphs = new ArrayList<>();
    }

    public List<StyledTextLine> getCurrentPage(int pageNumber){
        if(pages.size() > pageNumber) {
            return pages.get(pageNumber);
        }
        return new ArrayList<>();
    }

    ////

    public List<StyledTextLine> getStyledPage(int index){
        for(List<StyledTextLine> page : pages){
            if(index >= page.get(0).getStartIndex() && index <= page.get(page.size() -1).getEndIndex()){
                return page;
            }
        }
        return null;
    }

    /*
    public List<StyledTextLine> getNextStyledPage(int index){
        return pages.get(Math.min(pages.size()-1, index));
    }

    public List<StyledTextLine> getPrevStyledPage(int index){
        return pages.get(Math.max(0, index));
    }
    */

    ////

    public StyledTextLine getStyledLine(int index){
        for(StyledTextLine line : lines){
            if(index >= line.getStartIndex() && index <= line.getEndIndex()){
                return line;
            }
        }
        return null;
    }

    public StyledTextLine getNextStyledLine(int index){
        StyledTextLine line = getStyledLine(index);
        return lines.get(Math.min(lines.size()-1, lines.indexOf(line) + 1));
    }

    @Nullable
    public StyledTextLine getNextStyledLine(int index, Function<StyledTextLine, Boolean> func){
        StyledTextLine line = getStyledLine(index);

        for(int i = lines.indexOf(line) + 1; i < lines.size(); i++){
            StyledTextLine renderInfo = lines.get(i);
            if(func.apply(renderInfo)){
                return renderInfo;
            }
        }
        return null;
    }

    public StyledTextLine getPrevStyledLine(int index){
        StyledTextLine line = getStyledLine(index);
        return lines.get(Math.max(0, lines.indexOf(line) - 1));
    }

    @Nullable
    public StyledTextLine getPrevStyledLine(int index, Function<StyledTextLine, Boolean> func){
        StyledTextLine line = getStyledLine(index);

        for(int i = lines.indexOf(line) - 1; i >= 0; i--){
            StyledTextLine renderInfo = lines.get(i);
            if(func.apply(renderInfo)){
                return renderInfo;
            }
        }
        return null;
    }

    ////

    public GlyphRenderInfo getGlyphInfo(int index){
        return glyphs.get(index);
    }

    public GlyphRenderInfo getNextGlyphInfo(int index){
        return glyphs.get(Math.min(glyphs.size()-1, index));
    }

    @Nullable
    public GlyphRenderInfo getNextGlyphInfo(int index, Function<GlyphRenderInfo, Boolean> func){
        for(int i = index+1; i < glyphs.size(); i++){
            GlyphRenderInfo renderInfo = glyphs.get(i);
            if(func.apply(renderInfo)){
                return renderInfo;
            }
        }
        return null;
    }

    public GlyphRenderInfo getPrevGlyphInfo(int index){
        return glyphs.get(Math.max(0, index));
    }

    @Nullable
    public GlyphRenderInfo getPrevGlyphInfo(int index, Function<GlyphRenderInfo, Boolean> func){
        for(int i = index-1; i >= 0; i--){
            GlyphRenderInfo renderInfo = glyphs.get(i);
            if(func.apply(renderInfo)){
                return renderInfo;
            }
        }
        return null;
    }

    public List<GlyphRenderInfo> getSelection(int startIndex, int endIndex){
        List<GlyphRenderInfo> selection = new ArrayList<>();
        for(int i = startIndex; i < endIndex ; i++){
            selection.add(glyphs.get(i));
        }
        return selection;
    }

}
