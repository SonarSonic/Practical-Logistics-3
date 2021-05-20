package sonar.logistics.util.network;

import net.minecraft.network.PacketBuffer;

public interface IByteBufSaveable {

    void read(PacketBuffer buffer);

    void write(PacketBuffer buffer);

}
