/*
package sonar.logistics.multiparts.displays.old.gsi.storage;


import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.multiparts.displays.old.info.elements.DisplayElementHelper;
import sonar.logistics.multiparts.displays.old.info.elements.base.IClickableElement;
import sonar.logistics.multiparts.displays.old.info.elements.base.IDisplayElement;
import sonar.logistics.multiparts.displays.old.info.elements.base.IElementStorageHolder;
import sonar.logistics.multiparts.displays.old.info.elements.base.ILookableElement;
import sonar.logistics.utils.ListHelper;
import sonar.logistics.utils.network.EnumSyncType;
import sonar.logistics.utils.network.ISyncable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

public class ElementStorage implements ISyncable, Iterable<IDisplayElement> {

	public static final String TAG_NAME = "element_storage";

	protected Map<Integer, List<IDisplayElement>> elements = new HashMap<>();
	protected List<IClickableElement> clickables = new ArrayList<>();
	protected List<ILookableElement> lookables = new ArrayList<>();
	protected List<IElementStorageHolder> holders = new ArrayList<>();
	public IElementStorageHolder holder;
	public int elementCount;

	public ElementStorage(IElementStorageHolder holder) {
		this.holder = holder;
	}

	public void addElement(IDisplayElement e) {
		int id = DisplayElementHelper.getRegisteredID(e);
		elements.putIfAbsent(id, new ArrayList<>());
		if (ListHelper.addWithCheck(elements.get(id), e)) {
			if (e instanceof IClickableElement) {
				ListHelper.addWithCheck(clickables, (IClickableElement) e);
			}
			onElementAdded(e);
		}
	}

	public void removeElement(IDisplayElement e) {
		int id = DisplayElementHelper.getRegisteredID(e);
		if (elements.getOrDefault(id, new ArrayList<>()).remove(e)) {
			if (e instanceof IClickableElement) {
				clickables.remove(e);
			}
			onElementRemoved(e);
		}
	}

	public IDisplayElement getElementFromIdentity(int identity) {
		for (IDisplayElement e : this) {
			if (e.getElementIdentity() == identity) {
				return e;
			}
		}
		for (IElementStorageHolder holder : this.getSubHolders()) {
			IDisplayElement e = holder.getElements().getElementFromIdentity(identity);
			if (e != null) {
				return e;
			}
		}
		return null;
	}

	*/
/** the replaced element *//*

	public IDisplayElement replaceElement(IDisplayElement element, int identity) {
		int id = DisplayElementHelper.getRegisteredID(element);
		elements.putIfAbsent(id, new ArrayList<>());
		IDisplayElement toReplace = null;
		for (IDisplayElement e : holder.getElements()) {
			if (e.getElementIdentity() == identity) {
				toReplace = e;
				break;
			}
		}
		addElement(element);
		if (toReplace != null) {
			removeElement(toReplace);
		}
		return null;
	}

	public void onElementAdded(IDisplayElement e) {
		elementCount++;
		e.setHolder(holder);
		if (e instanceof IClickableElement) {
			clickables.add((IClickableElement) e);
		}
		if (e instanceof ILookableElement) {
			lookables.add((ILookableElement) e);
		}
		if (e instanceof IElementStorageHolder) {
			holders.add((IElementStorageHolder) e);
		}

		holder.onElementAdded(e);
	}

	public void onElementRemoved(IDisplayElement e) {
		elementCount--;
		if (e instanceof IClickableElement) {
			clickables.remove(e);
		}
		if (e instanceof ILookableElement) {
			lookables.remove(e);
		}
		if (e instanceof IElementStorageHolder) {
			holders.remove(e);
		}
		holder.onElementRemoved(e);
	}

	public void forEachElement(Consumer<IDisplayElement> action) {
		elements.values().forEach(l -> l.forEach(action));
	}

	@Override
	public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
		List<Integer> loaded = new ArrayList<>();
		ListNBT tagList = tag.getList(TAG_NAME, Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.size(); i++) {
			CompoundNBT elementsTag = tagList.getCompound(i);
			if (!elementsTag.isEmpty()) {
				int registryID = elementsTag.getInt("rID");
				Class<? extends IDisplayElement> currentClass = DisplayElementHelper.getElementClass(registryID);

				ListNBT subList = elementsTag.getList("list", Constants.NBT.TAG_COMPOUND);
				for (int s = 0; s < subList.size(); s++) {
					CompoundNBT eTag = subList.getCompound(s);
					int iden = eTag.getInt("identity");
					IDisplayElement ide = getElementFromIdentity(iden);
					if (ide != null) {
						ide.read(eTag, syncType);
					} else {
						IDisplayElement e = DisplayElementHelper.loadElement(eTag, holder);
						if (e != null) {
							addElement(e);
						}
					}
					loaded.add(iden);
				}
			}
		}
		List<IDisplayElement> toRemove = new ArrayList<>();
		forEach(element -> {
			if (!loaded.contains(element.getElementIdentity())) {
				toRemove.add(element);
			}
		});
		toRemove.forEach(this::removeElement);
		return tag;
	}

	@Override
	public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
		ListNBT tagList = new ListNBT();
		for (Map.Entry<Integer, List<IDisplayElement>> map : elements.entrySet()) {
			if (map.getValue().isEmpty()) {
				continue;
			}
			ListNBT subList = new ListNBT();
			for (IDisplayElement e : map.getValue()) {
				CompoundNBT eTag = DisplayElementHelper.saveElement(new CompoundNBT(), e, syncType);
				subList.add(eTag);
			}
			if (!subList.isEmpty()) {
				CompoundNBT elementsTag = new CompoundNBT();
				elementsTag.putInt("rID", map.getKey());
				elementsTag.put("list", subList);
				tagList.add(elementsTag);
			}
		}
		tag.put(TAG_NAME, tagList);
		return tag;
	}

	public int getElementCount() {
		return elementCount;
	}

	public boolean hasClickables() {
		if (!clickables.isEmpty()) {
			return true;
		}
		List<IElementStorageHolder> allHolders = getAllSubHolders(new ArrayList<>());
		for (IElementStorageHolder holder : allHolders) {
			if (!holder.getElements().getClickables().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public List<IClickableElement> getClickables() {
		return clickables;
	}

	public boolean hasLookables() {
		if (!lookables.isEmpty()) {
			return true;
		}
		List<IElementStorageHolder> allHolders = getAllSubHolders(new ArrayList<>());
		for (IElementStorageHolder holder : allHolders) {
			if (!holder.getElements().getClickables().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public List<ILookableElement> getLookables() {
		return lookables;
	}

	public boolean hasSubHolders() {
		return !holders.isEmpty();
	}

	public List<IElementStorageHolder> getSubHolders() {
		return holders;
	}

	public List<IElementStorageHolder> getAllSubHolders(List<IElementStorageHolder> holders) {
		getSubHolders().forEach(holder -> {
			ListHelper.addWithCheck(holders, holder);
			holder.getElements().getAllSubHolders(holders);
		});
		return holders;
	}

	@Nonnull
    @Override
	public Iterator<IDisplayElement> iterator() {
		return new StorageIterator(this);
	}

	public class StorageIterator implements Iterator<IDisplayElement> {
		public Iterator<Map.Entry<Integer, List<IDisplayElement>>> entries;
		public Iterator<IDisplayElement> elements;

		public StorageIterator(ElementStorage s) {
			this.entries = s.elements.entrySet().iterator();
		}

		@Override
		public boolean hasNext() {
			if (elements != null && elements.hasNext()) {
				return true;
			}
			Iterator<IDisplayElement> nextList = null;
			while (nextList == null || !nextList.hasNext()) {
				if (entries.hasNext()) {
					nextList = entries.next().getValue().iterator();
				} else {
					return false;
				}
			}
			elements = nextList;
			return true;
		}

		@Override
		public IDisplayElement next() {
			return elements.next();
		}

	}

}
*/
