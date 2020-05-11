/*
package sonar.logistics.common.multiparts.displays.old.info.elements;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.helpers.RenderHelper;
import sonar.logistics.PL2;
import sonar.logistics.api.core.tiles.displays.info.IInfo;
import sonar.logistics.api.core.tiles.displays.info.InfoUUID;
import sonar.logistics.api.core.tiles.displays.info.lists.AbstractChangeableList;
import sonar.logistics.core.tiles.displays.gsi.interaction.DisplayScreenClick;
import sonar.logistics.core.tiles.displays.info.InfoRenderHelper;
import sonar.logistics.core.tiles.displays.info.elements.base.ElementFillType;
import sonar.logistics.core.tiles.displays.info.elements.base.IClickableElement;
import sonar.logistics.core.tiles.displays.info.elements.base.ILookableElement;
import sonar.logistics.core.tiles.displays.info.types.LogicInfoList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.renderer.GlStateManager.*;

public abstract class NetworkGridElement<L> extends AbstractInfoElement<LogicInfoList> implements IClickableElement, ILookableElement {

	public int pageCount = 0;
	public int xSlots, ySlots, perPage = 0;
	public List<L> cachedList = null;
	public double element_size = 7 * 0.0625;
	public int text_colour = 16777215;
	public double grid_fill_percentage = 0.75;

	public NetworkGridElement() {
		super();
	}

	public NetworkGridElement(InfoUUID uuid) {
		super(uuid);
	}

	public abstract double getRenderWidth();

	public abstract double getRenderHeight();

	public abstract void renderGridElement(L stack, int index);

	public abstract void onGridElementClicked(DisplayScreenClick click, LogicInfoList list, @Nullable L stack);

	public double width, height, X_SPACING, Y_SPACING, centreX, centreY;
	public int start, stop;

	public void render(LogicInfoList list) {
		info = getGSI().getCachedInfo(uuid);
		cachedList = getCachedList(list, uuid);
		if (cachedList.isEmpty()) {
			InfoRenderHelper.renderCenteredStringsWithAdaptiveScaling(getActualScaling()[WIDTH], getActualScaling()[HEIGHT], getActualScaling()[SCALE], 0, 0.5, -1, Lists.newArrayList("EMPTY LIST"));
			return;
		}
		width = getRenderWidth();
		height = getRenderHeight();
		centreX = (width / 2) - ((width * grid_fill_percentage) / 2);
		centreY = (height / 2) - ((height * grid_fill_percentage) / 2);

		xSlots = (int) Math.floor(getActualScaling()[WIDTH] / width);
		ySlots = (int) Math.floor(getActualScaling()[HEIGHT] / height);

		X_SPACING = (getActualScaling()[WIDTH] - (xSlots * width)) / xSlots;
		Y_SPACING = (getActualScaling()[HEIGHT] - (ySlots * height)) / ySlots;

		//System.out.println(element_size);

		perPage = xSlots * ySlots;
		boolean needsPages = perPage < cachedList.size();
		if (needsPages && perPage != 0) {
			double adjusted_height = getActualScaling()[HEIGHT] - (getActualScaling()[HEIGHT] / 8);
			ySlots = (int) Math.floor(adjusted_height / height);
			Y_SPACING = (adjusted_height - (ySlots * height)) / ySlots;
		}
		perPage = xSlots * ySlots;
		if(perPage==0){
			InfoRenderHelper.renderCenteredStringsWithAdaptiveScaling(getActualScaling()[WIDTH], getActualScaling()[HEIGHT], getActualScaling()[SCALE], 0, 0.5, -1, Lists.newArrayList("ADJUST SCALING"));
		}
		int totalPages = (int) (Math.ceil((double) cachedList.size() / (double) perPage));
		if (pageCount >= totalPages) {
			pageCount = totalPages - 1;
		}

		start = Math.max(perPage * pageCount, 0);
		stop = Math.max(Math.min(perPage + perPage * pageCount, cachedList.size()), 0);
		preListRender();
		for (int i = start; i < stop; i++) {
			pushMatrix();
			int index = i - start;
			int xLevel = (int) (index - ((Math.floor((index / xSlots))) * xSlots));
			int yLevel = (int) (Math.floor((index / xSlots)));
			translate((xLevel * width) + centreX + (X_SPACING * (xLevel + 0.5D)), (yLevel * height) + centreY + (Y_SPACING * (yLevel + 0.5D)), 0.005);
			renderGridElement(cachedList.get(i), index);
			popMatrix();
		}
		postListRender();

		if (needsPages && perPage != 0) {
			DisplayElementHelper.renderPageButons(getActualScaling(), this.pageCount + 1, totalPages);
		}

	}

	public void preListRender() {
		pushMatrix();
		color(1.0F, 1.0F, 1.0F, 1.0F);
		scale(1, 1, -1);
		enableRescaleNormal();
		disableLighting();
	}

	public void postListRender() {
		enableLighting();
		disableRescaleNormal();
		RenderHelper.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		popMatrix();
	}

	public List<L> getCachedList(LogicInfoList info, InfoUUID id) {
		if (cachedList == null || info.listChanged) {
			info.listChanged = false;
			AbstractChangeableList<?> list = PL2.proxy.getInfoManager(true).getChangeableListMap().get(id);
			cachedList = list != null ? (ArrayList<L>) list.createSaveableList(info.listSorter) : new ArrayList<>();
			if (cachedList.size() < perPage * pageCount - 1) {
				pageCount = 0;
			}
		}
		AbstractChangeableList<?> list = PL2.proxy.getInfoManager(true).getChangeableListMap().get(id);
		cachedList = list != null ? (ArrayList<L>) list.createSaveableList(info.listSorter) : new ArrayList<>();
		return cachedList;
	}

	@Override
	public boolean isType(IInfo info) {
		return info instanceof LogicInfoList;
	}

	@Override
	public ElementFillType getFillType() {
		return ElementFillType.FILL_CONTAINER;
	}

	@Override
	public int onGSIClicked(DisplayScreenClick click, EntityPlayer player, double subClickX, double subClickY) {
		double[] scaling = getActualScaling();
		boolean needsPages = perPage < cachedList.size();
		if (needsPages && subClickY > scaling[HEIGHT] - (scaling[HEIGHT] / 8)) {
			int totalPages = (int) (Math.ceil((double) cachedList.size() / (double) perPage));
			pageCount = DisplayElementHelper.doPageClick(subClickX, subClickY, getActualScaling(), pageCount, totalPages);
			return -1;
		}

		DisplayScreenClick subClick = new DisplayScreenClick().setClickPosition(new double[] { click.clickX - 0.5, click.clickY });
		subClick.identity = click.identity;
		subClick.doubleClick = click.doubleClick;
		subClick.gsi = click.gsi;
		subClick.type = click.type;
		subClick.intersect = click.intersect;

		if (cachedList == null || cachedList.isEmpty()) {
			if (info instanceof LogicInfoList) {
				onGridElementClicked(subClick, (LogicInfoList) info, null);
			}
			return -1;
		}
		int xSlot = 0, ySlot = 0;
		for (int x = 0; x < xSlots; x++) {
			double xStart = (x * width) + centreX + (X_SPACING * (x + 0.5D));
			double xStop = xStart + width;
			if (subClickX >= xStart && subClickX < xStop) {
				xSlot = x;
				break;
			}
		}
		for (int y = 0; y < ySlots; y++) {
			double yStart = (y * height) + centreY + (Y_SPACING * (y + 0.5D));
			double yStop = yStart + height;
			if (subClickY >= yStart && subClickY < yStop) {
				ySlot = y;
				break;
			}
		}
		int slot = ((ySlot * xSlots) + xSlot) + start;
		if (info instanceof LogicInfoList) {
			LogicInfoList list = (LogicInfoList) info;
			L stack = slot < cachedList.size() ? cachedList.get(slot) : null;
			onGridElementClicked(subClick, list, stack);
		}
		return -1;
	}

	@Override
	public void readData(NBTTagCompound nbt, SyncType type) {
		super.readData(nbt, type);
		element_size = nbt.getDouble("sizing");
		text_colour = nbt.getInteger("colour");
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound nbt, SyncType type) {
		super.writeData(nbt, type);
		nbt.setDouble("sizing", element_size);
		nbt.setInteger("colour", text_colour);
		return nbt;
	}

}
*/
