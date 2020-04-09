package sonar.logistics.multiparts.utils;

import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;

public class MultipartItemUseContext extends BlockItemUseContext {

    public BlockPos internalPos;
    public boolean useInternal = false;

    public MultipartItemUseContext(ItemUseContext context) {
        super(context);
        this.internalPos = context.getPos();
    }

    public MultipartItemUseContext useInternal(boolean useInternal) {
        this.useInternal = useInternal;
        return this;
    }

    public BlockPos getPos() {
        return useInternal ? internalPos : super.getPos();
    }
}
