/*
package sonar.logistics.multiparts.displays.old.gsi.interaction;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.Vec3d;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;
import sonar.logistics.multiparts.displays.gsi.CommonGSI;
import sonar.logistics.multiparts.displays.containers.ScaleableElement;
import sonar.logistics.multiparts.displays.old.gsi.storage.DisplayElementContainer;
import sonar.logistics.utils.network.EnumSyncType;

public class DisplayScreenClick {

	public CommonGSI gsi;
	public BlockInteractionType type;
	public double clickX, clickY;
	public Vec3d intersect;
	public int identity;
	public boolean doubleClick = false;
	public boolean fakeGuiClick = false;

	public ScaleableElement clickedElement = null;
	public double subClickX = 0, subClickY = 0;
	public DisplayElementContainer clickedContainer = null;

    public DisplayScreenClick setClickPosition(double[] clickPosition){
		this.clickX = clickPosition[0];
		this.clickY = clickPosition[1];
		return this;
	}
	
	public static CompoundNBT writeClick(DisplayScreenClick click, CompoundNBT tag){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("identity", click.identity);
		nbt.putInt("type", click.type.ordinal());
		nbt.putDouble("clickX", click.clickX);
		nbt.putDouble("clickY", click.clickY);
		DisplayVectorHelper.writeVec3d(tag, "intersect", click.intersect);
		nbt.putBoolean("doubleClick", click.doubleClick);
		tag.put("displayclick", nbt);
		return tag;
		
	}
	
	public static DisplayScreenClick readClick(CompoundNBT tag){
		DisplayScreenClick click = new DisplayScreenClick();
		CompoundNBT nbt = tag.getCompound("displayclick");
		click.identity = nbt.getInt("identity");
		click.type = BlockInteractionType.values()[nbt.getInt("type")];
		click.clickX = nbt.getDouble("clickX");
		click.clickY = nbt.getDouble("clickY");
		click.intersect = DisplayVectorHelper.readVec3d(tag, "intersect");
		click.doubleClick = nbt.getBoolean("doubleClick");		
		return click;
		
	}
	
}
*/
