package sonar.logistics.client.gsi.elements;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.api.IRenderableElement;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.vectors.Quad2D;

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
    public void render(ScaleableRenderContext context, Quad2D bounds) {
        context.itemStackRenderer.addBatchedItemStack(stack, bounds);
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
