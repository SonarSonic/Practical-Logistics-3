package sonar.logistics.client.gsi.elements;

import net.minecraftforge.fluids.FluidStack;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.util.vectors.Quad2F;

public class FluidStackElement implements IRenderableElement {

    public FluidStack stack;

    public FluidStackElement(FluidStack stack){
        this.stack = stack;
    }

    @Override
    public boolean canRender(GSIRenderContext context) {
        return !stack.isEmpty();
    }

    @Override
    public void render(GSIRenderContext context, Quad2F bounds) {
        GSIRenderHelper.renderScaledFluidStack(context, stack, 0, 0, (float) bounds.getX(), (float) bounds.getY());
    }

    @Override
    public String toString() {
        return "Fluid: " + stack.getFluid().getRegistryName() + "Amount: " + stack.getAmount();
    }
}
