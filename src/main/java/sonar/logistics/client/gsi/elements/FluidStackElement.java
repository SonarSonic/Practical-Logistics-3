package sonar.logistics.client.gsi.elements;

import net.minecraftforge.fluids.FluidStack;
import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;
import sonar.logistics.client.vectors.Quad2D;

public class FluidStackElement implements IRenderableElement {

    public FluidStack stack;

    public FluidStackElement(FluidStack stack){
        this.stack = stack;
    }

    @Override
    public boolean canRender(ScaleableRenderContext context) {
        return !stack.isEmpty();
    }

    @Override
    public void render(ScaleableRenderContext context, Quad2D bounds) {
        ScaleableRenderHelper.renderScaledFluidStack(context, stack, 0, 0, (float) bounds.getX(), (float) bounds.getY());
    }

    @Override
    public String toString() {
        return "Fluid: " + stack.getFluid().getRegistryName() + "Amount: " + stack.getAmount();
    }
}
