package sonar.logistics.client.design.gui.interactions.hotkeys;

public interface IKeyMatch {

    boolean canTrigger(int key, int scanCode, int modifiers);

}
