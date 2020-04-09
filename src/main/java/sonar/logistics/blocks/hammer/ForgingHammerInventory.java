package sonar.logistics.blocks.hammer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import sonar.logistics.utils.items.IInternalItemHandler;

import javax.annotation.Nonnull;

public class ForgingHammerInventory extends ItemStackHandler implements IInternalItemHandler {

        private ForgingHammerTile tile;

        public ForgingHammerInventory(ForgingHammerTile tile){
            super(2);
            this.tile = tile;
        }


        protected void onLoad() {
            super.onLoad();
            tile.invChanged = true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            tile.invChanged = true;
            tile.markDirty();
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == 0 ? ItemStack.EMPTY : extractItemInternal(slot, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return slot == 1 ? stack : insertItemInternal(slot, stack, simulate);
        }

        @Nonnull
        public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
            return super.extractItem(slot, amount, simulate);
        }

        @Nonnull
        public ItemStack insertItemInternal(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if(slot == 0 && ForgingHammerRecipes.INSTANCE.getMatchingRecipe(stack) == null){
                return false;
            }
            return super.isItemValid(slot, stack);
        }
}
