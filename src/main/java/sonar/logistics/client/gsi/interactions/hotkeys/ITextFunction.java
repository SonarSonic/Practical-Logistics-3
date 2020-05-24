package sonar.logistics.client.gsi.interactions.hotkeys;

import sonar.logistics.client.gsi.api.ITextComponent;
import sonar.logistics.client.gsi.interactions.EditStandardTextInteraction;

public interface ITextFunction {

    void trigger(EditStandardTextInteraction<ITextComponent> textInteraction);

}
