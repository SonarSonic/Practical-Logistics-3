package sonar.logistics.blocks.hammer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import sonar.logistics.blocks.PL3Blocks;
import sonar.logistics.utils.items.ExternalItemWrapper;


public class ForgingHammerContainer extends Container {

    public ForgingHammerTile tileEntity;
    private IItemHandler playerInventory;
    private IItemHandler internalInventory;

    public ForgingHammerContainer(int windowID, PlayerInventory playerInventory, BlockPos pos, World world) {
        super(PL3Blocks.FORGING_HAMMER_CONTAINER, windowID);
        this.playerInventory = new InvWrapper(playerInventory);
        this.tileEntity = (ForgingHammerTile) world.getTileEntity(pos);
        this.internalInventory = new ExternalItemWrapper(tileEntity.inventory, true);
        addSlot(new SlotItemHandler(internalInventory, 0, 53, 24));
        addSlot(new SlotItemHandler(internalInventory, 1, 107, 24));
        layoutPlayerInventorySlots(8, 62);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), player, PL3Blocks.FORGING_HAMMER_BLOCK);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        int INVENTORY = 2, HOTBAR = 9, PLAYER_INV = 9*3;
        int TOTAL_SIZE = INVENTORY + HOTBAR + PLAYER_INV;

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index < INVENTORY) {
                if (!this.mergeItemStack(stack, INVENTORY, INVENTORY + HOTBAR + PLAYER_INV, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {
                if (ForgingHammerRecipes.INSTANCE.getMatchingRecipe(stack) != null) {
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < INVENTORY + PLAYER_INV) {
                    if (!this.mergeItemStack(stack, INVENTORY + PLAYER_INV, TOTAL_SIZE, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < TOTAL_SIZE && !this.mergeItemStack(stack, INVENTORY, INVENTORY + PLAYER_INV, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }


     private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
