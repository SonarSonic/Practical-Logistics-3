package sonar.logistics.multiparts.displays.old.gsi.containers;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.utils.network.ISyncable;

import java.util.List;

public interface IScaleableContainer extends IScaleable, ISyncable {

    int getContainerID();

    List<IScaleableElement> getElements();

    boolean isSizingLocked();

}
