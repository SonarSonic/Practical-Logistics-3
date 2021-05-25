package sonar.logistics.server.data.types.energy;

import sonar.logistics.server.data.api.IData;

public class EnergyStorageData implements IData {

    public EnumEnergyType type = EnumEnergyType.FE;
    public long energy, capacity;
    public boolean hasUpdated;

    public void preUpdate(){
        hasUpdated = false;
    }

    public boolean hasUpdated(){
        return hasUpdated;
    }

    @Override
    public String toString() {
        return String.format("%s %s / %s %s", energy, type.toString(), capacity, type.toString());
    }
}
