package sonar.logistics.client.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import sonar.logistics.client.gui.api.IFlexibleGuiEventListener;
import sonar.logistics.client.gui.api.IInteractWidget;
import sonar.logistics.client.gui.api.ISimpleWidget;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.vectors.Quad2D;

//used for rendering a GSI within a GUI - this default implementation has no scaling / scissoring.
public class GSIWidget extends AbstractGui implements IFlexibleGuiEventListener, ISimpleWidget, IInteractWidget {

    public Quad2D bounds;
    public GSI gsi;

    public GSIWidget(){
        this.gsi = new GSI();
    }

    public void setBoundsAndRebuild(Quad2D bounds){
        this.bounds = bounds;
        this.gsi.setBoundsAndRebuild(new Quad2D(0, 0, bounds.width, bounds.height));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        GSIRenderContext context = new GSIRenderContext(gsi, partialTicks, new MatrixStack());
        gsi.interactionHandler.updateMouseFromGui(mouseX - bounds.x, mouseY - bounds.y);

        RenderSystem.enableDepthTest();
        context.matrix.scale(1, 1, -1); //GUI'S are rendered back to front.
        context.matrix.translate(bounds.x, bounds.y, 0);
        context.scaleNormals(1, 1, -1);
        gsi.render(context);
        context.matrix.scale(1, 1, -1); //revert the scaling
        RenderSystem.disableDepthTest();
    }

    @Override
    public IGuiEventListener getEventListener() {
        return gsi.interactionHandler;
    }

    @Override
    public Quad2D getQuad() {
        return bounds;
    }
}
