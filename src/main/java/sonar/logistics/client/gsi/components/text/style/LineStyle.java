package sonar.logistics.client.gsi.components.text.style;

public class LineStyle {

    public double lineSpacing = 0;
    public double charSpacing = 0;///TODO
    public WrappingType wrappingType = WrappingType.WRAP_OFF;
    public AlignType alignType = AlignType.ALIGN_TEXT_LEFT;
    public BreakPreference breakPreference = BreakPreference.SPACES;

    public LineStyle(){}

    public LineStyle copy() {
        LineStyle copy = new LineStyle();
        copy.lineSpacing = lineSpacing;
        copy.charSpacing = charSpacing;
        copy.wrappingType = wrappingType;
        copy.alignType = alignType;
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

    public enum AlignType{
        ALIGN_TEXT_LEFT, ///aligns the line to the left
        CENTER, //aligns the line to the centre
        ALIGN_TEXT_RIGHT, //aligns the line to the right
        JUSTIFY; ///changes the size of the spaces, to justify the text
    }

    public enum BreakPreference {
        ANY, //the line will break as soon as a glyph extends the lines limit
        SPACES, //when a glyph, extends the lines limit the it will attempt to find a space in the line and break here instead, the space will not appear on either line.
        ELEMENTS; // will break the line if the line

        public boolean shouldBreakAtSpace(){
            return this == SPACES;
        }
    }
}
