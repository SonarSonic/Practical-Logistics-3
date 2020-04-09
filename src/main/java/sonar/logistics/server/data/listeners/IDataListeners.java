package sonar.logistics.server.data.listeners;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public interface IDataListeners {

    List<ServerPlayerEntity> getListeners();

}
