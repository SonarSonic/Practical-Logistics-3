/*
package sonar.logistics.common.multiparts.displays.old.gsi.interaction;

import net.minecraft.util.Tuple;
import sonar.logistics.common.multiparts.displays.gsi.CommonGSI;
import sonar.logistics.common.multiparts.displays.old.info.elements.DisplayElementHelper;

public class GSIInteractionHelper {

    //// GRID SELECTION MODE \\\\\

    public static double getGridXScale(CommonGSI gsi) {
    	if(gsi.display.getWidth()<0.5){
    		return gsi.display.getWidth() / 4;
		}
        return gsi.display.getWidth() / 8;
    }

    public static double getGridYScale(CommonGSI gsi) {
		if(gsi.display.getHeight()<0.5){
			return gsi.display.getHeight() / 4;
		}
		return gsi.display.getHeight() / 8;
    }

    public static double getGridXPosition(CommonGSI gsi, double x) {
        return DisplayElementHelper.toNearestMultiple(x, gsi.getDisplaySizing()[0], getGridXScale(gsi));
    }

    public static double getGridYPosition(CommonGSI gsi, double y) {
        return DisplayElementHelper.toNearestMultiple(y, gsi.getDisplaySizing()[1], getGridYScale(gsi));
    }


	public static boolean withinX(double x, double[] clickBox) {
		return x >= Math.min(clickBox[0], clickBox[2]) && x <= Math.max(clickBox[0], clickBox[2]);
	}

	public static boolean withinY(double y, double[] clickBox) {
		return y >= Math.min(clickBox[1], clickBox[3]) && y <= Math.max(clickBox[1], clickBox[3]);
	}

	public static boolean checkClick(double x, double y, double[] clickBox) {
		return withinX(x, clickBox) && withinY(y, clickBox);
	}

	*/
/*
	public static boolean overlapX(double x, double[] clickBox) {
		return x >= Math.min(clickBox[0], clickBox[2]) && x <= Math.max(clickBox[0], clickBox[2]);
	}

	public static boolean overlapY(double y, double[] clickBox) {
		return y >= Math.min(clickBox[1], clickBox[3]) && y <= Math.max(clickBox[1], clickBox[3]);
	}
	public static boolean checkOverlap(double[] elementBox, double[] clickBox) {
		if (elementBox.length != 4 || clickBox.length != 4) {
			return false;
		}
		boolean xOverlap = overlapX(elementBox[0], clickBox) || overlapX(elementBox[2], clickBox) || overlapX(clickBox[0], elementBox)|| overlapX(clickBox[1], elementBox);
		boolean yOverlap = overlapY(elementBox[1], clickBox) || overlapY(elementBox[3], clickBox) || overlapY(clickBox[1], elementBox) || overlapY(clickBox[3], elementBox);
		return xOverlap && yOverlap;
	}
	*//*


	/// ITEM & FLUID INTERACTION \\\\

	public enum ItemInteractionType {
		ADD, REMOVE
	}

	public static Tuple<Integer, ItemInteractionType> getItemsToRemove(BlockInteractionType type) {
		switch (type) {
			case LEFT:
				return new Tuple(1, ItemInteractionType.REMOVE);
			case RIGHT:
				return new Tuple(64, ItemInteractionType.ADD);
			case SHIFT_LEFT:
				return new Tuple(64, ItemInteractionType.REMOVE);
			default:
				return new Tuple(0, ItemInteractionType.ADD);
		}
	}
	*/
/*
	public static void screenItemStackClicked(int networkID, StoredItemStack storedItemStack, DisplayScreenClick click, EntityPlayer player, NBTTagCompound clickTag) {
		Pair<Integer, ItemInteractionType> toRemove = getItemsToRemove(click.type);
		ILogisticsNetwork network = LogisticsNetworkHandler.instance().getNetwork(networkID);
		if (toRemove.a != 0 && network.isValid()) {
			switch (toRemove.b) {
				case ADD:
					ItemStack stack = player.getHeldItem(player.getActiveHand()).copy();
					if (!stack.isEmpty()) {
						long prev = ItemHelper.getItemCount(stack, network);
						if (!click.doubleClick) {
							player.setHeldItem(player.getActiveHand(), ItemHelper.insertItemStack(network, stack, 64));
						} else {
							ItemHelper.transferPlayerInventoryToNetwork(player, network, IS -> StoredItemStack.isEqualStack(IS, stack), Integer.MAX_VALUE);
						}
						long after = ItemHelper.getItemCount(stack, network);
						if (prev != after) {
							PL2.network.sendTo(new PacketItemInteractionText(stack, after, (after- prev)), (EntityPlayerMP) player);
							InfoPacketHelper.createRapidItemUpdate(Lists.newArrayList(stack), networkID);
						}
					}
					break;
				case REMOVE:
					if (storedItemStack != null) {
						long prev = ItemHelper.getItemCount(storedItemStack.getItemStack(), network);
						ItemHelper.transferNetworkInventoryToPlayer(player, network, IS -> storedItemStack.equalStack(IS), toRemove.a);
						long after = ItemHelper.getItemCount(storedItemStack.getItemStack(), network);
						if (prev != after) {
							PL2.network.sendTo(new PacketItemInteractionText(storedItemStack.getItemStack(), after, -(prev-after)), (EntityPlayerMP) player);
							InfoPacketHelper.createRapidItemUpdate(Lists.newArrayList(storedItemStack.getItemStack()), networkID);
						}
					}
					break;
				default:
					break;
			}

		}
	}

	public static void onScreenFluidStackClicked(int networkID, StoredFluidStack fluidStack, DisplayScreenClick click, EntityPlayer player, NBTTagCompound clickTag) {
		ILogisticsNetwork network = LogisticsNetworkHandler.instance().getNetwork(networkID);
		if (network.isValid()) {
			IFluidHandler handler = new DummyFluidHandler(network, fluidStack);
			EnumHand hand = player.getActiveHand();
			ItemStack heldItem = player.getHeldItem(hand);
			FluidActionResult result = FluidActionResult.FAILURE;
			FluidStack toUpdate = fluidStack == null ? FluidUtil.getFluidContained(heldItem) : fluidStack.getFullStack();
			if (click.type == BlockInteractionType.RIGHT) {
				result = FluidUtil.tryEmptyContainer(heldItem, handler, Integer.MAX_VALUE, player, true);
			} else if (fluidStack != null && click.type == BlockInteractionType.LEFT) {
				result = FluidUtil.tryFillContainer(heldItem, handler, (int) Math.min(1000, fluidStack.stored), player, true);
			} else if (fluidStack != null && click.type == BlockInteractionType.SHIFT_LEFT) {
				result = FluidUtil.tryFillContainer(heldItem, handler, (int) Math.min(Integer.MAX_VALUE, fluidStack.stored), player, true);
			}
			if (result.isSuccess()) {
				player.setHeldItem(hand, result.getResult());
				if (toUpdate != null) {
					InfoPacketHelper.createRapidFluidUpdate(Lists.newArrayList(toUpdate), networkID);
				}
			}
		}
	}
	*//*

}*/
