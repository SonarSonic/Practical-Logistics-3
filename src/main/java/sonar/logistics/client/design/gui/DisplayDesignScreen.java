package sonar.logistics.client.design.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import sonar.logistics.multiparts.displays.api.IDisplay;

public class DisplayDesignScreen extends Screen {

    public final IDisplay display;

    protected DisplayDesignScreen(IDisplay display, ITextComponent text) {
        super(text);
        this.display = display;
    }


}
