/*
package sonar.logistics.multiparts.displays.old.info.elements;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.logistics.api.core.tiles.displays.info.IInfo;
import sonar.logistics.api.core.tiles.displays.info.InfoUUID;
import sonar.logistics.core.tiles.displays.info.InfoRenderHelper;
import sonar.logistics.core.tiles.displays.info.elements.base.IInfoReferenceElement;

import java.util.List;

public abstract class AbstractInfoElement<T extends IInfo> extends AbstractDisplayElement implements IInfoReferenceElement {

	public InfoUUID uuid;
	public IInfo info;

	public AbstractInfoElement() {}

	public AbstractInfoElement(InfoUUID uuid) {
		this.uuid = uuid;
	}

	public void render() {		
		this.info = getGSI().getCachedInfo(uuid);		
		if (info != null && isType(info)) {
			render((T) info);
		}else{
			InfoRenderHelper.renderCenteredStringsWithAdaptiveScaling(getActualScaling()[WIDTH], getActualScaling()[HEIGHT], getActualScaling()[SCALE], 0, 0.5, -1, Lists.newArrayList("NO DATA"));
		}
	}

	public abstract boolean isType(IInfo info);

	public abstract void render(T info);

	@Override
	public List<InfoUUID> getInfoReferences() {
		return Lists.newArrayList(uuid);
	}

	@Override
	public String getRepresentiveString() {
		return uuid.toString();
	}

	@Override
	public void readData(NBTTagCompound nbt, SyncType type) {
		super.readData(nbt, type);
		(uuid = new InfoUUID()).readData(nbt, type);
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound nbt, SyncType type) {
		super.writeData(nbt, type);
		uuid.writeData(nbt, type);
		return nbt;
	}

}
*/
