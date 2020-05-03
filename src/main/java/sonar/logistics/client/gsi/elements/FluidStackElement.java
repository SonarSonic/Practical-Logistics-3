package sonar.logistics.client.gsi.elements;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

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
    public void render(ScaleableRenderContext context, Vec3d alignment, Vec3d scaling) {
        ScaleableRenderHelper.renderScaledFluidStack(context, stack, 0, 0, (float)scaling.getX(), (float)scaling.getY());
    }

    @Override
    public String toString() {
        return "Fluid: " + stack.getFluid().getRegistryName() + "Amount: " + stack.getAmount();
    }
}
