package sonar.logistics.networking.proxy;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import sonar.logistics.common.blocks.PL3Blocks;
import sonar.logistics.common.blocks.hammer.ForgingHammerRenderer;
import sonar.logistics.common.blocks.hammer.ForgingHammerScreen;
import sonar.logistics.common.blocks.host.MultipartHostHelper;
import sonar.logistics.common.blocks.host.MultipartHostRenderer;
import sonar.logistics.common.blocks.host.MultipartHostTile;
import sonar.logistics.common.multiparts.base.IMultipartRenderer;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.common.multiparts.displays.DisplayScreenRenderer;
import sonar.logistics.common.multiparts.displays.DisplayScreenTile;
import sonar.logistics.common.multiparts.displays.LargeDisplayScreenTile;
import sonar.logistics.networking.PL3PacketHandler;
import sonar.logistics.networking.packets.MultipartRemovePacket;
import sonar.logistics.server.data.DataManager;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ClientProxy implements IProxy {

    public HashMap<Class<? extends MultipartTile>, IMultipartRenderer<?>> multipartRenderers = new HashMap<>();

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        ScreenManager.registerFactory(PL3Blocks.FORGING_HAMMER_CONTAINER, ForgingHammerScreen::new);
        multipartRenderers.put(DisplayScreenTile.class, new DisplayScreenRenderer());
        multipartRenderers.put(LargeDisplayScreenTile.class, new DisplayScreenRenderer());
    }

    @Override
    public void clientOnly() {
        ClientRegistry.bindTileEntityRenderer(PL3Blocks.FORGING_HAMMER_TILE, ForgingHammerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(PL3Blocks.MULTIPART_HOST_TILE, MultipartHostRenderer::new);
        RenderTypeLookup.setRenderLayer(PL3Blocks.FORGING_HAMMER_BLOCK, RenderType.getCutoutMipped());
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public DataManager getDataManager() {
        throw new NullPointerException("The Data Manager should only be used on the server side!");
    }

    @Nullable
    @Override
    public IMultipartRenderer<MultipartTile> getMultipartRenderer(MultipartTile tile){
        if(tile == null){
            return null;
        }
        return (IMultipartRenderer<MultipartTile>) multipartRenderers.get(tile.getClass());
    }

    public long timer = 0;
    public boolean isBreaking;
    public int timeToBreak = 2 * 1000; ////2 seconds
    public int timeToReset = 3 * 1000; ////3 seconds

    @SubscribeEvent
    public void onLeftClickEvent(PlayerInteractEvent.LeftClickBlock event){ //TODO reset block breaking when player looks at different multipart + optimise timer.
        BlockState state = event.getWorld().getBlockState(event.getPos());
        if(state.getBlock() == PL3Blocks.MULTIPART_HOST_BLOCK){
            if(!event.getWorld().isRemote){
                event.setCanceled(true);
                return;
            }
            boolean cancel = true;
            Minecraft.getInstance().playerController.resetBlockRemoving();
            long millis = System.currentTimeMillis();
            if(!event.getPlayer().isCreative() && (timer == 0 || timer + (timeToReset) < millis)){
                isBreaking = true;
                timer = millis;
            }else if(event.getPlayer().isCreative() || timer + timeToBreak < millis){
                timer = 0;
                isBreaking = false;
                MultipartEntry entry = MultipartHostHelper.getRayTraceMultipart(event.getWorld(), event.getPos(), event.getEntity());
                if(entry != null) {
                    state.addDestroyEffects(event.getWorld(), event.getPos(), Minecraft.getInstance().particles);
                    entry.getHost().doRemoveMultipart(entry);
                    PL3PacketHandler.INSTANCE.sendToServer(new MultipartRemovePacket(event.getPos(), entry.slot));
                }else{
                    MultipartHostTile tile = MultipartHostHelper.getMultipartHostTile(event.getWorld(), event.getPos());
                    if(tile == null || tile.MULTIPARTS.isEmpty()){
                        cancel = false;
                    }
                }
            }
            event.setCanceled(cancel);

        }else{
            timer = 0;
            isBreaking = false;
        }
    }

}
