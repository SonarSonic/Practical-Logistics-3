package sonar.logistics.client.gsi.components;

import net.minecraft.item.ItemStack;
import sonar.logistics.blocks.PL3Blocks;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;

public class RenderSpeedTest extends AbstractStyledScaleable implements IScaleableComponent {

    @Override
    public void render(ScaleableRenderContext context) {
        super.render(context);
        ItemStack stack = new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK);
        context.itemStackRenderer.addBatchedItemStack(stack, alignment.getRenderBounds());
    }
}
