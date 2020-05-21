package sonar.logistics.client.gsi.components.text.style;

import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.render.StyledTextLine;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class LineStyle {

    public double lineSpacing = 0;//TODO
    public double charSpacing = 0;///TODO
    public WrappingType wrappingType = WrappingType.WRAP_OFF;
    public JustifyType justifyType = JustifyType.JUSTIFY_LEFT;
    public BreakPreference breakPreference = BreakPreference.SPACES;

    public LineStyle(){}

    public LineStyle copy() {
        LineStyle copy = new LineStyle();
        copy.lineSpacing = lineSpacing;
        copy.charSpacing = charSpacing;
        copy.wrappingType = wrappingType;
        copy.justifyType = justifyType;
        copy.breakPreference = breakPreference;
        return copy;
    }

    public enum WrappingType{
        WRAP_OFF, //stop at max width
        WRAP_ON, //wrap at max width
        SCALED_FIT, //rescale at max width
        SCALED_FILL; //always rescale to max width

        public boolean canRescale(){
            return this == SCALED_FIT || this == SCALED_FILL;
        }

        public boolean canWrap(){
            return this == WRAP_ON;
        }
    }

    public enum JustifyType {
        JUSTIFY_LEFT, ///aligns the line to the left
        JUSTIFY_CENTRE, //aligns the line to the centre
        JUSTIFY_RIGHT, //aligns the line to the right
        JUSTIFY; ///changes the size of the spaces, to justify the text

        public void justify(StyledTextLine line, double boundsWidth, boolean isLastLine){
            ListIterator<GlyphRenderInfo> it = line.glyphInfo.listIterator(line.glyphInfo.size());

            List<GlyphRenderInfo> lastSpaces = new ArrayList<>();
            double excessSpaces = 0;
            while(it.hasPrevious()){
                GlyphRenderInfo info = it.previous();
                if(info.glyph.isSpace()){
                    excessSpaces += info.quad.width;
                    lastSpaces.add(info);
                    continue;
                }
                break;
            }

            switch (this){
                case JUSTIFY_LEFT:
                    ///should already be aligned left....
                    break;
                case JUSTIFY_CENTRE:
                    line.renderSize.x += boundsWidth/2 - (line.renderSize.width - excessSpaces)/2;
                    break;
                case JUSTIFY_RIGHT:
                    line.renderSize.x += (float)boundsWidth - (line.renderSize.width - excessSpaces);
                    break;
                case JUSTIFY:
                    if(isLastLine){
                        break;
                    }
                    double totalSpaceSize = 0;
                    int spaceCount = 0;
                    for(GlyphRenderInfo info : line.glyphInfo){
                        if(info.glyph.isSpace() && !lastSpaces.contains(info)){
                            totalSpaceSize += info.quad.width;
                            spaceCount++;
                        }
                    }
                    float spaceSize = (float) (boundsWidth - (line.renderSize.width - totalSpaceSize)) / spaceCount;
                    line.glyphInfo.stream().filter(info -> info.glyph.isSpace() && !lastSpaces.contains(info)).forEach(info -> info.quad.width=spaceSize);
                    line.renderSize.width = (float)boundsWidth; //justified lines should always take up the max width
                    break;
            }
        }

    }

    public enum BreakPreference {
        ANY, //the line will break as soon as a glyph extends the lines limit
        SPACES, //when a glyph, extends the lines limit the it will attempt to find a space in the line and break here instead, the space will not appear on either line.
        ELEMENTS; // will break the line if the line

        public boolean isWordBreaker(Glyph glyph){
            return glyph.isSpace() || glyph.isWordBreaker();
        }

        @Deprecated
        public boolean shouldBreakAtSpace(){
            return this == SPACES;
        }
    }
}
