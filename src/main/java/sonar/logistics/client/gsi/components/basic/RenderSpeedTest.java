package sonar.logistics.client.gsi.components.basic;

import net.minecraft.item.ItemStack;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.common.blocks.PL3Blocks;

public class RenderSpeedTest extends Component {

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        ItemStack stack = new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK);
        context.itemStackRenderer.addBatchedItemStack(stack, bounds.innerSize());
    }
}
