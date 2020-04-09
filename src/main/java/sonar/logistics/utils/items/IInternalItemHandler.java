package sonar.logistics.utils.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public interface IInternalItemHandler extends IItemHandlerModifiable {

    @Nonnull
    ItemStack insertItemInternal(int slot, @Nonnull ItemStack stack, boolean simulate);

    @Nonnull
    ItemStack extractItemInternal(int slot, int amount, boolean simulate);

}
