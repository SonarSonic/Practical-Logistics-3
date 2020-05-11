/*
package sonar.logistics.common.multiparts.displays.old.gsi.storage;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.vectors.DisplayVectorHelper;
import sonar.logistics.common.multiparts.displays.VectorRenderHelper;
import sonar.logistics.common.multiparts.displays.gsi.CommonGSI;
import sonar.logistics.common.multiparts.displays.old.info.elements.DisplayElementHelper;
import sonar.logistics.common.multiparts.displays.old.info.elements.base.*;
import sonar.logistics.utils.network.EnumSyncType;
import sonar.logistics.utils.network.ISyncable;

public class DisplayElementContainer implements IElementStorageHolder, ISyncable {

	///// SAVED VALUES \\\\\
	public int containerIdentity;
	public ElementStorage elements = new ElementStorage(this);
	public Vec3d containerSavedTranslation;
	public Vec3d containerSavedSizing;
	public double percentageScale;
	public boolean locked = false; // if this is true the translation and max scaling will remain the same, even when new base added.

	///// INSTANCE VALUES \\\\\
	public CommonGSI gsi;
	public boolean isWithinScreenBounds;

	////// RENDERING VALUES \\\\\
	private Vec3d containerTranslation;
	private Vec3d containerActualSizing; // only for aligning purposes.
	private Vec3d containerMaxSizing, elementMaxSizing;
	protected int defaultColour = -1;

	public DisplayElementContainer() {}

	public DisplayElementContainer(CommonGSI gsi, double width, double height, double xPos, double yPos, double zPos, double pScale, int identity) {
		this(gsi, new Vec3d(width, height, 1), new Vec3d(xPos, yPos, zPos), pScale, identity);
	}

	public DisplayElementContainer(CommonGSI gsi, Vec3d scaling, Vec3d translation, double pScale, int identity) {
		this.gsi = gsi;
		this.containerIdentity = identity;
		this.resize(gsi.getDisplaySizing(), scaling, translation, pScale);
	}

	public void resize(Vec3d sizing, Vec3d scaling, Vec3d translation, double pScale) {
		this.containerSavedTranslation = DisplayVectorHelper.percentageFromScale(translation, sizing);
		this.containerSavedSizing = DisplayVectorHelper.percentageFromScale(scaling, sizing);
		this.percentageScale = pScale;

		this.containerTranslation = null;
		this.containerMaxSizing = null;
		this.elementMaxSizing = null;
	}

	///// HELPER METHODS \\\\\

	@Override
	public DisplayElementContainer getContainer() {
		return this;
	}

	@Override
	public ElementStorage getElements() {
		return elements;
	}

	public CommonGSI getGSI() {
		return gsi;
	}

	public int getContainerIdentity() {
		return containerIdentity;
	}

	public int setDefaultColour(int colour) {
		return defaultColour = colour;
	}

	public int getDefaultColour() {
		return defaultColour;
	}

	///// ELEMENT ADDITION \\\\\

	public void onElementAdded(IDisplayElement e) {
		e.setHolder(this);
		updateActualScaling();
		//gsi.onElementAdded(this, e);
	}

	public void onElementRemoved(IDisplayElement element) {
		element.setHolder(this);
		updateActualScaling();
		//gsi.onElementRemoved(this, element);
	}


	///// RENDERING \\\\\

	public boolean canRender() {
		return isWithinScreenBounds; //&& gsi.mode.renderElements();
	}

	public void render() {
		if (canRender()) {
			elements.forEach(IDisplayRenderable::updateRender);
			RenderSystem.pushMatrix();
			VectorRenderHelper.translate(getContainerTranslation());
			VectorRenderHelper.translate(getAlignmentTranslation());
			//DisplayElementHelper.renderElementStorageHolder(this);
			RenderSystem.popMatrix();
		}
	}

	@Override
	public void startElementRender(IDisplayElement e) {}

	@Override
	public void endElementRender(IDisplayElement e) {}

	@Override
	public Tuple<IDisplayElement, double[]> getClickBoxes(double x, double y) {
		return null;
	}

	public void resetRenderValues(){
		containerTranslation = null;
		containerMaxSizing = null;
		elementMaxSizing = null;
	}


	public void updateActualScaling() {
		resetRenderValues();
		elements.forEach(e -> {
			if (e instanceof IElementStorageHolder) {
				((IElementStorageHolder) e).updateActualScaling();
			} else {
				e.onElementChanged();
			}
		});

		containerActualSizing = DisplayVectorHelper.getPositiveMaxVector(elements, IDisplayElement::getActualScaling);

		if (locked) {
			double endX = getContainerTranslation().x + getActualScaling().x;
			double endY = getContainerTranslation().y + getActualScaling().y;
			isWithinScreenBounds = endX <= gsi.getDisplaySizing().x && endY <= gsi.getDisplaySizing().y;
		} else {
			isWithinScreenBounds = true;
		}

	}


	////// CONTAINER SCALING + TRANSLATION \\\\\\\

	public Vec3d getContainerTranslation() {
		if (containerTranslation == null) {
			if (locked) {
				containerTranslation = containerSavedTranslation;// DisplayElementHelper.toNearestPixel(createdTranslation, gsi.getDisplayScaling());
			} else {
				containerTranslation = DisplayVectorHelper.scaleFromPercentage(containerSavedTranslation, gsi.getDisplaySizing());
			}
		}
		return containerTranslation;
	}

	*/
/** the maximum scale of an element *//*

	public Vec3d getElementMaxScaling() {
		if (elementMaxSizing == null) {
			elementMaxSizing = getContainerMaxScaling().mul(percentageScale, percentageScale, 1);
		}
		return elementMaxSizing;
	}

	*/
/** the maximum scale of this container *//*

	public Vec3d getContainerMaxScaling() {
		if (containerMaxSizing == null) {
			if (locked) {
				containerMaxSizing = containerSavedSizing;
			} else {
				containerMaxSizing = DisplayVectorHelper.scaleFromPercentage(containerSavedSizing, gsi.getDisplaySizing());
			}
		}
		return containerMaxSizing;
	}

	*/
/**the scale covered by all of the elements*//*

	public Vec3d getActualScaling() {
		if (containerActualSizing == null) {
			updateActualScaling();
		}
		return containerActualSizing;
	}

	///// ELEMENT SCALING \\\\\

	@Override
	public Vec3d createMaxScaling(IDisplayElement element) {
		switch (element.getFillType()) {
		case FILL_CONTAINER:
			return getContainerMaxScaling();
		case FILL_SCALED_CONTAINER:
			return getElementMaxScaling();
		default:
			return DisplayVectorHelper.scaleFromUnscaledSize(element.getUnscaledWidthHeight(), getElementMaxScaling(), 1);
		}
	}

	@Override
	public Vec3d createActualScaling(IDisplayElement element) {
		switch (element.getFillType()) {
		case FILL_CONTAINER:
			return getContainerMaxScaling();
		case FILL_SCALED_CONTAINER:
			return getElementMaxScaling();
		default:
			return DisplayVectorHelper.scaleFromUnscaledSize(element.getUnscaledWidthHeight(), element.getMaxScaling(), 1);
		}
	}

	@Override
	public Vec3d getAlignmentTranslation() {
		return DisplayVectorHelper.alignArrayWithin(getContainerMaxScaling(), getElementMaxScaling(), ElementAlignment.LEFT, ElementAlignment.LEFT, ElementAlignment.LEFT);
	}

	public Vec3d getFullAlignmentTranslation(IDisplayElement e) {
		Vec3d containerAlign = getAlignmentTranslation();
		Vec3d holderAlign = (e.getHolder() instanceof IDisplayElement) ? getAlignmentTranslation((IDisplayElement) e.getHolder()) : DisplayVectorHelper.nullVector();
		Vec3d elementAlign = e.getHolder().getAlignmentTranslation(e);
		return DisplayVectorHelper.combineVectors(containerAlign, holderAlign, elementAlign);
	}

	@Override
	public Vec3d getAlignmentTranslation(IDisplayElement e) {
		return e.getAlignmentTranslation(e.getActualScaling(), getContainerMaxScaling());
	}


	///// NBT READ & WRITE \\\\\

	@Override
	public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
		containerIdentity = tag.getInt("iden");
		locked = tag.getBoolean("locked");
		percentageScale = tag.getDouble("percent");
		containerSavedTranslation = DisplayVectorHelper.readVec3d(tag, "trans");
		containerSavedSizing = DisplayVectorHelper.readVec3d(tag, "size");
		elements.read(tag, syncType);
		return tag;
	}

	@Override
	public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
		tag.putInt("iden", containerIdentity);
		tag.putBoolean("locked", locked);
		tag.putDouble("percent", percentageScale);
		DisplayVectorHelper.writeVec3d(tag, "trans", containerSavedTranslation);
		DisplayVectorHelper.writeVec3d(tag, "size", containerSavedSizing);
		elements.write(tag, syncType);
		return tag;
	}
}
*/
