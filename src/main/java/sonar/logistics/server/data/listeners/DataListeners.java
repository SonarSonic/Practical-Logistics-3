package sonar.logistics.server.data.listeners;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

/**simplification of the over complicated PL2ListenerList*/
public class DataListeners implements IDataListeners {

    public List<ServerPlayerEntity> listeners;

    @Override
    public List<ServerPlayerEntity> getListeners() {
        return listeners;
    }
}
