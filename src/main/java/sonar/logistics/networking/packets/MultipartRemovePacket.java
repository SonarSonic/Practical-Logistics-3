package sonar.logistics.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.common.blocks.host.MultipartHostTile;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.utils.EnumMultipartSlot;

public class MultipartRemovePacket extends MultipartAbstractPacket {

    public MultipartRemovePacket(BlockPos pos, EnumMultipartSlot slot) {
        super(pos, slot);
    }

    public MultipartRemovePacket(PacketBuffer b) {
        super(b);
    }

    @Override
    public void handle(MultipartHostTile host, MultipartEntry entry, NetworkEvent.Context ctx) {
        //TODO CAN PLAYER REMOVE ?
        host.doRemoveMultipart(entry);
        //TODO do multipart drops.
    }
}
