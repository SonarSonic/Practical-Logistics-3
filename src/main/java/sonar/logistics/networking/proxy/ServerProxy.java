package sonar.logistics.networking.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import sonar.logistics.common.multiparts.base.IMultipartRenderer;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.server.data.DataManager;

import javax.annotation.Nullable;

public class ServerProxy implements IProxy {

    DataManager dataManager = new DataManager();

    @Override
    public void init() {
        dataManager.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void clientOnly() {}

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Only run this on the client");
    }

    @Override
    public PlayerEntity getClientPlayer() {
        throw new IllegalStateException("Only run this on the client");
    }

    @Override
    public DataManager getDataManager() {
        return dataManager;
    }

    @Nullable
    @Override
    public <T extends MultipartTile> IMultipartRenderer<T> getMultipartRenderer(T tile){
        return null;
    }

}