package sonar.logistics.client.gsi.components.text.glyph;

import sonar.logistics.client.gsi.components.text.render.GlyphRenderContext;
import sonar.logistics.client.gsi.components.text.render.GlyphRenderInfo;
import sonar.logistics.client.gsi.components.text.api.IGlyphType;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.StyledTextString;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.properties.ColourProperty;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AttributeGlyph implements IGlyphType {

    public AttributeGlyph(){}

    @Override
    public float getRenderWidth(ScaledFontType fontType, GlyphStyle parentStyling) {
        return 0;
    }

    @Override
    public float getRenderHeight(ScaledFontType fontType, GlyphStyle parentStyling) {
        return 0;
    }

    @Override
    public void render(GlyphRenderContext context, GlyphRenderInfo glyphInfo) {}

    public abstract GlyphStyle alterStyle(GlyphStyle parentStyling);

    public abstract void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle);

    @Override
    public boolean isVisible(){
        return false;
    }

    ////

    public static class FontHeight extends AttributeGlyph{

        float fontHeight;

        public FontHeight(float fontHeight){
            this.fontHeight = fontHeight;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.fontHeight = fontHeight;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {
            ///remove all of the given type
            selected.stream().filter(i -> i.glyph instanceof FontHeight).forEach(i -> string.glyphs.remove(i.glyph));

            if(startStyle.fontHeight != fontHeight){
                string.glyphs.add(selected.get(0).index, this);
            }
            if(endStyle.fontHeight != fontHeight){
                string.glyphs.add(selected.get(selected.size()-1).index, new FontHeight(endStyle.fontHeight));
            }
        }
    }

    ////

    public static class TextColour extends AttributeGlyph{

        ColourProperty textColour;

        public TextColour(ColourProperty textColour){
            this.textColour = textColour;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.textColour = textColour;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {
            ///remove all of the given type
            selected.stream().filter(i -> i.glyph instanceof FontHeight).forEach(i -> string.glyphs.remove(i.glyph));

            if(!startStyle.textColour.equals(textColour)){
                string.glyphs.add(selected.get(0).index, this);
            }
            if(!endStyle.textColour.equals(textColour)){
                string.glyphs.add(selected.get(selected.size()-1).index, new TextColour(endStyle.textColour));
            }
        }
    }

    ////

    public static class BackgroundColour extends AttributeGlyph{

        ColourProperty backgroundColour;

        public BackgroundColour(ColourProperty backgroundColour){
            this.backgroundColour = backgroundColour;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.backgroundColour = backgroundColour;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {
            ///remove all of the given type
            selected.stream().filter(i -> i.glyph instanceof BackgroundColour).forEach(i -> string.glyphs.remove(i.glyph));

            if(!startStyle.textColour.equals(backgroundColour)){
                string.glyphs.add(selected.get(0).index, this);
            }
            if(!endStyle.textColour.equals(backgroundColour)){
                string.glyphs.add(selected.get(selected.size()-1).index, new BackgroundColour(endStyle.textColour));
            }
        }
    }

    ////

    public static class ActionID extends AttributeGlyph{

        int actionId;

        public ActionID(int actionId){
            this.actionId = actionId;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.actionId = actionId;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {
            ///remove all of the given type
            selected.stream().filter(i -> i.glyph instanceof ActionID).forEach(i -> string.glyphs.remove(i.glyph));

            if(startStyle.actionId != actionId){
                string.glyphs.add(selected.get(0).index, this);
            }
            if(endStyle.actionId != actionId){
                string.glyphs.add(selected.get(selected.size()-1).index, new ActionID(endStyle.actionId));
            }
        }
    }

    ////

    public static class Bold extends AttributeGlyph{

        boolean set;

        public Bold(boolean set){
            this.set = set;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.bold = set;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {

            List<GlyphRenderInfo> old = selected.stream().filter(i -> i.glyph instanceof Bold).collect(Collectors.toList());

            if(endStyle.bold != set){
                string.glyphs.add(selected.get(selected.size()-1).index, new Bold(endStyle.bold));
            }

            if(startStyle.bold != set){
                string.glyphs.add(selected.get(0).index, this);
            }

            old.forEach(i -> string.glyphs.remove(i.glyph));
        }
    }

    ////

    public static class Underline extends AttributeGlyph{

        boolean set;

        public Underline(boolean set){
            this.set = set;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.underlined = set;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {

            List<GlyphRenderInfo> old = selected.stream().filter(i -> i.glyph instanceof Underline).collect(Collectors.toList());

            if(endStyle.underlined != set){
                string.glyphs.add(selected.get(selected.size()-1).index, new Underline(endStyle.underlined));
            }

            if(startStyle.underlined != set){
                string.glyphs.add(selected.get(0).index, this);
            }

            old.forEach(i -> string.glyphs.remove(i.glyph));
        }
    }

    ////

    public static class Italic extends AttributeGlyph{

        boolean set;

        public Italic(boolean set){
            this.set = set;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.italic = set;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {
            List<GlyphRenderInfo> old = selected.stream().filter(i -> i.glyph instanceof Italic).collect(Collectors.toList());

            if(endStyle.italic != set){
                string.glyphs.add(selected.get(selected.size()-1).index, new Italic(endStyle.italic));
            }

            if(startStyle.italic != set){
                string.glyphs.add(selected.get(0).index, this);
            }

            old.forEach(i -> string.glyphs.remove(i.glyph));
        }
    }

    ////

    public static class Strikethrough extends AttributeGlyph{

        boolean set;

        public Strikethrough(boolean set){
            this.set = set;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.strikethrough = set;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {
            ///remove all of the given type
            selected.stream().filter(i -> i.glyph instanceof Strikethrough).forEach(i -> string.glyphs.remove(i.glyph));

            if(startStyle.strikethrough != set){
                string.glyphs.add(selected.get(0).index, this);
            }
            if(endStyle.strikethrough != set){
                string.glyphs.add(selected.get(selected.size()-1).index, new Strikethrough(endStyle.strikethrough));
            }
        }
    }

    ////

    public static class Obfuscated extends AttributeGlyph{

        boolean set;

        public Obfuscated(boolean set){
            this.set = set;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.obfuscated = set;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {
            ///remove all of the given type
            selected.stream().filter(i -> i.glyph instanceof Obfuscated).forEach(i -> string.glyphs.remove(i.glyph));

            if(startStyle.obfuscated != set){
                string.glyphs.add(selected.get(0).index, this);
            }
            if(endStyle.obfuscated != set){
                string.glyphs.add(selected.get(selected.size()-1).index, new Obfuscated(endStyle.obfuscated));
            }
        }
    }

    ////

    public static class Shadow extends AttributeGlyph{

        boolean set;

        public Shadow(boolean set){
            this.set = set;
        }

        @Override
        public GlyphStyle alterStyle(GlyphStyle parentStyling) {
            parentStyling.shadow = set;
            return parentStyling;
        }

        @Override
        public void alterString(StyledTextString string, List<GlyphRenderInfo> selected, GlyphStyle startStyle, GlyphStyle endStyle) {
            ///remove all of the given type
            selected.stream().filter(i -> i.glyph instanceof Shadow).forEach(i -> string.glyphs.remove(i.glyph));

            if(startStyle.shadow != set){
                string.glyphs.add(selected.get(0).index, this);
            }
            if(endStyle.shadow != set){
                string.glyphs.add(selected.get(selected.size()-1).index, new Shadow(endStyle.shadow));
            }
        }
    }

}
