package sonar.logistics.client.gsi.components.text;

import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.render.StyledTextPages;

public abstract class AbstractTextComponent extends Component {

    public StyledTextPages pages = new StyledTextPages(new StyledTextString());

    public AbstractTextComponent() {
        super();
    }

    public StyledTextPages pages(){
        return pages;
    }

    public StyledTextString text(){
        return pages().text;
    }

    public boolean canStyle(){
        return true;
    }

    public boolean canAddGlyph(Glyph glyph){
        return true;
    }

    /**triggers a rebuild if required and reformats text*/
    public void onTextChanged(){
        rebuild();
    }

    /**triggers a rebuild if required and reformats text*/
    public void onStylingChanged(){
        rebuild();
    }

}
