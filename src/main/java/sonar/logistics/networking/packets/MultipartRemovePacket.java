package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.blocks.host.MultipartHostTile;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;
import sonar.logistics.multiparts.base.MultipartEntry;

public class MultipartRemovePacket extends MultipartAbstractPacket {

    public MultipartRemovePacket(BlockPos pos, EnumMultipartSlot slot) {
        super(pos, slot);
    }

    public MultipartRemovePacket(PacketBuffer b) {
        super(b);
    }

    @Override
    public void handle(MultipartHostTile host, MultipartEntry entry, NetworkEvent.Context ctx) {
        //CAN PLAYER REMOVE ?
        host.doRemoveMultipart(entry);
        //TODO do multipart drops.
    }
}
