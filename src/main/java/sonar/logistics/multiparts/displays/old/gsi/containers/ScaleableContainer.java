package sonar.logistics.multiparts.displays.old.gsi.containers;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;
import sonar.logistics.utils.network.EnumSyncType;

import java.util.ArrayList;
import java.util.List;

/***a container whose size is based on a percentage of it's host container*/
public class ScaleableContainer extends ScaleableAbstract implements IScaleableContainer {

    public final List<IScaleableElement> elements = new ArrayList<>();
    public int containerID;
    public boolean isSizingLocked;//TODO

    public ScaleableContainer(IScaleable host){
        super(host);
    }

    @Override
    public int getContainerID() {
        return containerID;
    }

    @Override
    public List<IScaleableElement> getElements() {
        return elements;
    }

    @Override
    public boolean isSizingLocked() {
        return isSizingLocked;
    }

    @Override
    public void updateScaleable() {
        this.containerTranslation = DisplayVectorHelper.scaleFromPercentage(containerTranslationPercentage, host.getSizing());
        this.containerSizing = DisplayVectorHelper.scaleFromPercentage(containerSizingPercentage, host.getSizing());
        elements.forEach(IScaleableElement::updateScaleable);
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType) {
        containerID = tag.getInt("id");
        isSizingLocked = tag.getBoolean("locked");
        containerTranslationPercentage = DisplayVectorHelper.readVec3d(tag, "translate");
        containerSizingPercentage = DisplayVectorHelper.readVec3d(tag, "sizing");
        updateScaleable();
        return tag;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType) {
        tag.putInt("id", containerID);
        tag.putBoolean("locked", isSizingLocked);
        DisplayVectorHelper.writeVec3d(tag, "translate", containerTranslationPercentage);
        DisplayVectorHelper.writeVec3d(tag, "sizing", containerSizingPercentage);
        return tag;
    }
}
