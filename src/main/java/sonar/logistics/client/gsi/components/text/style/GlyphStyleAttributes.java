package sonar.logistics.client.gsi.components.text.style;

import sonar.logistics.client.gsi.style.properties.ColourProperty;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum GlyphStyleAttributes {
    FONT_HEIGHT(Float.class, 0.0625F, (s) -> s.fontHeight, (s, o) -> s.fontHeight = (Float)o),
    TEXT_COLOUR(Integer.class, new ColourProperty(255, 255, 255).rgba, (s) -> s.textColour.rgba, (s, o) -> s.textColour = new ColourProperty((Integer)o)),
    BACKGROUND_COLOUR(Integer.class, new ColourProperty(0, 0, 0).rgba, (s) -> s.backgroundColour.rgba, (s, o) -> s.backgroundColour = new ColourProperty((Integer)o)),
    TRIGGER_ID(Integer.class, -1, (s) -> s.triggerId, (s, o) -> s.triggerId = ((Integer)o)),
    BOLD(Boolean.class, false, (s) -> s.bold, (s, o) -> s.bold = ((Boolean)o)),
    UNDERLINE(Boolean.class, false, (s) -> s.underlined, (s, o) -> s.underlined = ((Boolean)o)),
    ITALIC(Boolean.class, false, (s) -> s.italic, (s, o) -> s.italic = ((Boolean)o)),
    STRIKETHROUGH(Boolean.class, false, (s) -> s.strikethrough, (s, o) -> s.strikethrough = ((Boolean)o)),
    OBFUSCATED(Boolean.class, false, (s) -> s.obfuscated, (s, o) -> s.obfuscated = ((Boolean)o)),
    SHADOW(Boolean.class, false, (s) -> s.shadow, (s, o) -> s.shadow = ((Boolean)o));

    private Function<GlyphStyle, Object> get;
    private BiConsumer<GlyphStyle, Object> set;
    private Class<?> valueClass;
    private Object def;

    GlyphStyleAttributes(Class<?> valueClass, Object def, Function<GlyphStyle, Object> get, BiConsumer<GlyphStyle, Object> set){
        this.valueClass = valueClass;
        this.def = def;
        this.get = get;
        this.set = set;
    }

    public void set(GlyphStyle style, Object obj){
        if(isValid(obj)){
            set.accept(style, obj);
        }
    }

    public Object get(GlyphStyle style){
        return get.apply(style);
    }

    public boolean isValid(Object obj){
        return valueClass.isInstance(obj);
    }

    public Object getDefault(){
        return def;
    }

}