package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.components.text.StyledTextString;
import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.render.StyledTextPages;

public interface ITextComponent extends IComponent {

    StyledTextPages pages();

    default StyledTextString text(){
        return pages().text;
    }

    default boolean canStyle(){
        return true;
    }

    default boolean canAddGlyph(Glyph glyph){
        return true;
    }

    default void onTextChanged(){}

    default void onStylingChanged(){}
}
