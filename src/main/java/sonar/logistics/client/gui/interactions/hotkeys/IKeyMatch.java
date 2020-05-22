package sonar.logistics.client.gui.interactions.hotkeys;

public interface IKeyMatch {

    boolean canTrigger(int key, int scanCode, int modifiers);

}
