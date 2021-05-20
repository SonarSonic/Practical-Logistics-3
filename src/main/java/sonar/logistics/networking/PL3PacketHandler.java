package sonar.logistics.networking;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sonar.logistics.PL3;
import sonar.logistics.networking.packets.*;

public class PL3PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(PL3.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static int packetID = 0;

    public static void registerPackets(){
        INSTANCE.registerMessage(packetID++, MultipartRemovePacket.class, MultipartRemovePacket::encode, MultipartRemovePacket::new, MultipartRemovePacket::handle);
        INSTANCE.registerMessage(packetID++, TileSyncPacket.class, TileSyncPacket::encode, TileSyncPacket::new, TileSyncPacket::handle);
        INSTANCE.registerMessage(packetID++, NetworkRequestPacket.class, NetworkRequestPacket::encode, NetworkRequestPacket::new, NetworkRequestPacket::handle);
        INSTANCE.registerMessage(packetID++, NetworkGeneralPacket.class, NetworkGeneralPacket::encode, NetworkGeneralPacket::new, NetworkGeneralPacket::handle);
        INSTANCE.registerMessage(packetID++, DataSyncPacket.class, DataSyncPacket::encode, DataSyncPacket::new, DataSyncPacket::handle);
        INSTANCE.registerMessage(packetID++, DataUpdatePacket.class, DataUpdatePacket::encode, DataUpdatePacket::new, DataUpdatePacket::handle);
    }

}
