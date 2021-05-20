package sonar.logistics.networking.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import sonar.logistics.common.multiparts.base.IMultipartRenderer;
import sonar.logistics.common.multiparts.base.MultipartTile;

import javax.annotation.Nullable;

public class ServerProxy implements IProxy {

    @Override
    public void init() {
    }

    ///remove this???

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

    @Nullable
    @Override
    public <T extends MultipartTile> IMultipartRenderer<T> getMultipartRenderer(T tile){
        return null;
    }
}