package sonar.logistics.client.gsi.elements;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;

public class ItemStackElement implements IRenderableElement {

    public ItemStack stack;
    public long stored;

    public ItemStackElement(ItemStack stack, long stored){
        this.stack = stack;
        this.stored = stored;
    }

    @Override
    public boolean isUniformScaling(ScaleableRenderContext context) {
        return true;
    }

    @Override
    public boolean canRender(ScaleableRenderContext context) {
        return !stack.isEmpty();
    }

    @Override
    public void render(ScaleableRenderContext context, Vec3d alignment, Vec3d scaling) {
        context.itemStackRenderer.addBatchedItemStack(stack, alignment, scaling);
    }

    @Override
    public Vec3d getUnscaledSize() {
        return new Vec3d(1, 1, 0);
    }

    @Override
    public String toString() {
        return "Item: " + stack.getItem().getRegistryName() + "Amount: " + stored;
    }
}
