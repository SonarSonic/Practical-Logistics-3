package sonar.logistics.client.gsi.api;

import sonar.logistics.util.network.ISyncable;

import java.util.List;

//TODO REPLACE WITH NORMAL COMPONENT
public interface IScaleableContainer extends IScaleable, ISyncable {

    int getContainerID();

    List<IScaleableComponent> getElements();

    boolean isSizingLocked();

}
