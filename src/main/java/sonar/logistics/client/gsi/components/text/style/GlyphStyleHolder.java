package sonar.logistics.client.gsi.components.text.style;

import java.util.HashMap;
import java.util.Map;

public class GlyphStyleHolder {

    private Map<Integer, Object> attributeMap = new HashMap<>();

    public void setAttribute(GlyphStyleAttributes attribute, Object attributeObj){
        if(attribute.isValid(attributeObj)){
            if(attribute.getDefault().equals(attributeObj)){
                attributeMap.remove(attribute.ordinal());
            }else{
                attributeMap.put(attribute.ordinal(), attributeObj);
            }
        }
    }

    public Object getAttribute(GlyphStyleAttributes attribute){
        return attributeMap.getOrDefault(attribute.ordinal(), attribute.getDefault());
    }

    public boolean isDefault(){
        for(Map.Entry<Integer, Object> entry : attributeMap.entrySet()) {
            if (entry.getValue() != GlyphStyleAttributes.values()[entry.getKey()].getDefault()) {
                return false;
            }
        }
        return true;
    }

    public void setFromStyle(GlyphStyle style){
        for(GlyphStyleAttributes attribute : GlyphStyleAttributes.values()){
            Object attr = attribute.get(style);
            if(attr != attribute.getDefault()){
                attributeMap.put(attribute.ordinal(), attr);
            }else{
                attributeMap.remove(attribute.ordinal());
            }
        }
    }

    public GlyphStyle getStyle(){
        GlyphStyle style = new GlyphStyle();
        for(GlyphStyleAttributes attributes : GlyphStyleAttributes.values()){
            attributes.set(style, attributeMap.getOrDefault(attributes.ordinal(), attributes.getDefault()));
        }
        return style;
    }

}
