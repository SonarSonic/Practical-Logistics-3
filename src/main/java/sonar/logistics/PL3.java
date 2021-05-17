package sonar.logistics;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sonar.logistics.common.blocks.ores.SapphireOreGen;
import sonar.logistics.common.multiparts.PL3Multiparts;
import sonar.logistics.networking.PL3PacketHandler;
import sonar.logistics.networking.proxy.ClientProxy;
import sonar.logistics.networking.proxy.IProxy;
import sonar.logistics.networking.proxy.ServerProxy;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.caches.network.PL3NetworkManager;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.server.data.DataRegistry;

@Mod(PL3.MODID)
public class PL3 {

    ///CONSTANTS
    public static final String MODID = "practicallogistics3";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    public static final Logger LOGGER = LogManager.getLogger();

    public PL3() {
        LOGGER.info("PL3: Practical Logistics 3 - Init");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setupCommon(final FMLCommonSetupEvent event) {
        LOGGER.info("FMLCommonSetupEvent");

        LOGGER.info("Initialising Proxy");
        proxy.init();
        LOGGER.info("Registering Sapphire Ore Gen");
        SapphireOreGen.register();
        LOGGER.info("Registering Multipart Registry");
        PL3Multiparts.register();
        LOGGER.info("Registering Packet Handler");
        PL3PacketHandler.registerPackets();

        LOGGER.info("Registering Data Types");
        DataRegistry.INSTANCE.init();
        //TODO RECIPES
    }

    private void setupClient(final FMLClientSetupEvent event) {
        LOGGER.info("FMLClientSetupEvent");
        proxy.clientOnly();
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event){
        LOGGER.info("PL3: - SERVER STARTED EVENT");
        PL3NetworkManager.INSTANCE.clear();
        DataManager.instance().clear();
    }

    @SubscribeEvent
    public void onServerStopped(FMLServerStoppedEvent event){
        LOGGER.info("PL3: - SERVER STOPPED EVENT");
        PL3NetworkManager.INSTANCE.clear();
        DataManager.instance().clear();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        switch (event.phase){
            case START:
                break;
            case END:
                PL3NetworkManager.INSTANCE.cached.values().forEach(PL3Network::tick);

                DataManager.instance().constructingPhase();
                DataManager.instance().updatingPhase();
                DataManager.instance().notifyingPhase();
                break;
        }
    }
}
