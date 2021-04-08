package sonar.logistics.client.gui.widgets;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import sonar.logistics.util.vectors.Quad2D;

public abstract class AbstractWidget extends AbstractGui implements IGuiEventListener, IRenderable {

    public AbstractWidget(){}

    public abstract void render(int mouseX, int mouseY, float partialTicks);

    public abstract Quad2D getBounds();

}
