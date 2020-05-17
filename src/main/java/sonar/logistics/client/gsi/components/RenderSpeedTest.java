package sonar.logistics.client.gsi.components;

import net.minecraft.item.ItemStack;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.scaleables.AbstractStyledScaleable;
import sonar.logistics.common.blocks.PL3Blocks;

public class RenderSpeedTest extends AbstractStyledScaleable implements IScaleableComponent {

    @Override
    public void render(ScaleableRenderContext context, DisplayInteractionHandler interact) {
        super.render(context, interact);
        ItemStack stack = new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK);
        context.itemStackRenderer.addBatchedItemStack(stack, bounds.renderBounds());
    }
}
