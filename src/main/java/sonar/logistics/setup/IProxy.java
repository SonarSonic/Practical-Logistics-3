package sonar.logistics.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import sonar.logistics.multiparts.base.IMultipartRenderer;
import sonar.logistics.multiparts.base.MultipartTile;
import sonar.logistics.server.data.DataManager;

import javax.annotation.Nullable;

public interface IProxy {

    void init();

    void clientOnly();

    World getClientWorld();

    PlayerEntity getClientPlayer();

    DataManager getDataManager();

    @Nullable
    <T extends MultipartTile> IMultipartRenderer<T> getMultipartRenderer(T tile);

}
