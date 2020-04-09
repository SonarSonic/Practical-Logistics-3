/*
package sonar.logistics.multiparts.displays.old.info.elements.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sonar.core.api.nbt.INBTSyncable;
import sonar.core.helpers.FontHelper;
import sonar.logistics.core.tiles.displays.gsi.DisplayGSI;
import sonar.logistics.core.tiles.displays.info.elements.DisplayElementHelper;
import sonar.logistics.core.tiles.displays.tiles.TileAbstractDisplay;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;
import sonar.logistics.multiparts.displays.gsi.CommonGSI;
import sonar.logistics.utils.network.ISyncable;

public interface IDisplayElement extends IDisplayRenderable, ISyncable {

	*/
/**the id this {@link IDisplayElement} is registered to*//*

	String getRegisteredName();	

	*/
/**a string to represent what this {@link IDisplayElement} is in a GUI*//*

	String getRepresentiveString();
	
	*/
/**sets the {@link IElementStorageHolder} of this {@link IDisplayElement}*//*

	IElementStorageHolder setHolder(IElementStorageHolder c);

	*/
/**gets the {@link IElementStorageHolder} of this {@link IDisplayElement}*//*

	IElementStorageHolder getHolder();

	*/
/**the unique  identity of this element, used to identify the element between server and client*//*

	int getElementIdentity();

	*/
/**gets the {@link DisplayGSI} [Guided Screen Interface] which features this Display Element*//*

	default DisplayGSI getGSI() {
		return getHolder().getContainer().getGSI();
	}

	//// IDisplayRenderable \\\\
	
	default void render() {
		FontHelper.text(getRepresentiveString(), 0, 0, getHolder().getContainer().getDefaultColour());
	}

	default void updateRender() {
		// CAN BE USED TO MARK THE ELEMENT AS CHANGED
	}

	void onElementChanged();

	double getPercentageFill();

	double setPercentageFill(double fill);


	*/
/** the width/height in pixels, returns 0,0 if this element fills the entire container *//*

	default ElementFillType getFillType() {
		return ElementFillType.CUSTOM_SIZE;
	}

	Vec3d getUnscaledWidthHeight();

	default Vec3d getAlignmentTranslation(Vec3d actualScaling, Vec3d maxScaling) {
		return DisplayVectorHelper.alignArrayWithin(maxScaling, actualScaling, getXAlignment(), getYAlignment(), getZAlignment());
	}
	
	ElementAlignment getXAlignment();

	ElementAlignment setXAlignment(ElementAlignment alignX);

	ElementAlignment getYAlignment();

	ElementAlignment setYAlignment(ElementAlignment alignY);

	ElementAlignment getZAlignment();

	ElementAlignment setZAlignment(ElementAlignment alignZ);

	Vec3d setMaxScaling(double[] scaling);

	Vec3d getMaxScaling();

	Vec3d setActualScaling(double[] scaling);

	Vec3d getActualScaling();

	default Object getClientEditGui(TileAbstractDisplay obj, Object origin, World world, EntityPlayer player){
		return null;
	}
	
	default void validate(CommonGSI gsi){}
	
	default void invalidate(CommonGSI gsi){}

}
*/
