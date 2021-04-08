package sonar.logistics.client.gsi.components.text;

import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.interactions.api.INestedInteractionListener;

public interface IComponentHost extends INestedInteractionListener {

    GSI getGSI();

}