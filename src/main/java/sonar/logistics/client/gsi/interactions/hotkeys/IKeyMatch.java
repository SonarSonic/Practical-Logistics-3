package sonar.logistics.client.gsi.interactions.hotkeys;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;

public interface IKeyMatch {

    boolean canTrigger(GSIInteractionHandler handler, int key, int scanCode, int modifiers);

}
