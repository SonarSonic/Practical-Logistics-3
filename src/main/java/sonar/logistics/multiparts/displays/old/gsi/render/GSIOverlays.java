/*
package sonar.logistics.multiparts.displays.old.gsi.render;

import net.minecraft.util.math.BlockPos;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;
import sonar.logistics.multiparts.displays.api.IDisplay;
import sonar.logistics.multiparts.displays.old.gsi.DisplayGSI;
import sonar.logistics.multiparts.displays.old.gsi.interaction.DisplayScreenLook;

public class GSIOverlays {

	public static DisplayScreenLook currentLook = null;
	public static BlockPos lastPos = null;

	@Nullable
	public static DisplayScreenLook getCurrentLook(DisplayGSI container) {
		if (currentLook != null && currentLook.identity == container.getDisplayGSIIdentity()) {
			return currentLook;
		}
		return null;
	}

	@Nullable
	public static DisplayScreenLook getCurrentLook(int identity) {
		if (currentLook != null && currentLook.identity == identity) {
			return currentLook;
		}
		return null;
	}

	public static void tick(DrawBlockHighlightEvent evt) {
		EnumFacing face = evt.getTarget().sideHit;
		if (face != null) {
			BlockPos clickPos = evt.getTarget().getBlockPos();
			IPartSlot slot = EnumDisplayFaceSlot.fromFace(face);
			Optional<IMultipartTile> tile = MultipartHelper.getPartTile(evt.getPlayer().getEntityWorld(), clickPos, slot);
			if (tile.isPresent() && tile.get() instanceof IDisplay) {
				IDisplay display = (IDisplay) tile.get();
				if(display.getGSI() != null) {
					currentLook = DisplayVectorHelper.createLook(evt.getPlayer(), display);
					return;
				}
			}

		}
		currentLook = null;

	}

}
*/
