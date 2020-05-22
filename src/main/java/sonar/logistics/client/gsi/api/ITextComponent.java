package sonar.logistics.client.gsi.api;

import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.render.StyledTextPages;

public interface ITextComponent extends IComponent {

    StyledTextPages pages();

    default boolean canStyle(){
        return true;
    }

    default boolean canAddGlyph(Glyph glyph){
        return true;
    }

}
