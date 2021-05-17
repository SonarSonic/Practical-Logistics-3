package sonar.logistics.networking.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sonar.logistics.common.multiparts.base.IMultipartRenderer;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.caches.network.PL3NetworkManager;
import sonar.logistics.server.data.DataManager;

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