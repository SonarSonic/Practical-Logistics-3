package sonar.logistics.client.design.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.renderer.Matrix3f;
import sonar.logistics.client.design.api.IFlexibleGuiEventListener;
import sonar.logistics.client.design.api.IInteractWidget;
import sonar.logistics.client.design.api.ISimpleWidget;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.vectors.Quad2D;

//used for rendering a GSI within a GUI - this default implementation has no scaling / scissoring.
public class GSIWidget extends AbstractGui implements IFlexibleGuiEventListener, ISimpleWidget, IInteractWidget {

    public Quad2D bounds;
    public GSI gsi;
    public DisplayInteractionHandler internalHandler;

    public GSIWidget(){
        this.gsi = new GSI(this::getQuad);
        this.internalHandler = new DisplayInteractionHandler(gsi, Minecraft.getInstance().player, true);
    }

    public GSIWidget(GSI gsi, DisplayInteractionHandler internalHandler){
        this.gsi = gsi;
        this.internalHandler = internalHandler;
    }

    public void build(Quad2D bounds){
        this.bounds = bounds;
        this.gsi.build();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        ScaleableRenderContext context = new ScaleableRenderContext(gsi, partialTicks, new MatrixStack());
        internalHandler.updateMouseFromGui(mouseX - bounds.x, mouseY - bounds.y);
        RenderSystem.enableDepthTest();
        context.matrix.scale(1, 1, -1); //GUI'S are rendered back to front.
        context.matrix.translate(bounds.x, bounds.y, 0);
        context.scaleNormals(1, 1, -1);
        gsi.render(context, internalHandler);
        context.matrix.scale(1, 1, -1); //revert the scaling
        RenderSystem.disableDepthTest();
    }

    @Override
    public IGuiEventListener getEventListener() {
        return internalHandler;
    }

    @Override
    public Quad2D getQuad() {
        return bounds;
    }
}
