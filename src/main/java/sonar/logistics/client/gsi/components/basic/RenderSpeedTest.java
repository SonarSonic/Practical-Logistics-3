package sonar.logistics.client.gsi.components.basic;

import net.minecraft.item.ItemStack;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.common.blocks.PL3Blocks;

public class RenderSpeedTest extends AbstractComponent implements IComponent {

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        ItemStack stack = new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK);
        context.itemStackRenderer.addBatchedItemStack(stack, bounds.renderBounds());
    }
}
