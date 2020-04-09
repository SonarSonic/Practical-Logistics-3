package sonar.logistics.server.data.api;

import com.google.common.base.Objects;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.utils.network.EnumSyncType;
import sonar.logistics.utils.network.ISyncable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/** used to identify data and find the source it originated from */
@Nonnull
public class DataUUID implements ISyncable {

    public int source;
    public int method;

    public DataUUID() {}

    /*
    public DataUUID(INetworkTile tile, int channelID) {
        this.source = tile.getIdentity();
        this.method = channelID;
    }
    */
    public DataUUID(int identity, int channelID) {
        this.source = identity;
        this.method = channelID;
    }

    public static DataUUID newEmpty() {
        return new DataUUID(-1, -1);
    }

    public static DataUUID newInvalid() {
        return new DataUUID(-2, -2);
    }

    public int getSource() {
        return source;
    }

    public int getMethod() {
        return method;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DataUUID) {
            return this.source == ((DataUUID) obj).source && this.method == ((DataUUID) obj).method;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(source, method);
    }

    public static boolean valid(DataUUID uuid) {
        return uuid != null && uuid.source >= 0 && uuid.method >= 0;
    }

    /** if this uuid has been received from the server yet */
    public static boolean shouldRender(DataUUID uuid) {
        return uuid.source != -2;
    }

    public static DataUUID getUUID(ByteBuf buf) {
        return new DataUUID(buf.readInt(), buf.readInt());
    }

    public void writeToBuf(ByteBuf buf) {
        buf.writeInt(source);
        buf.writeInt(method);
    }

    public String toString() {
        return source + ":" + method;
    }

    public static DataUUID fromString(String string) {
        String[] ids = string.split(":");
        return new DataUUID(Integer.valueOf(ids[0]), Integer.valueOf(ids[1]));
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType) {
        source = tag.getInt("hash");
        method = tag.getInt("pos");
        return tag;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
        tag.putInt("hash", source);
        tag.putInt("pos", method);
        return tag;
    }
    /*
    public static List<DataUUID> readInfoList(CompoundNBT nbt, String tagName) {
        List<DataUUID> newUUIDs = new ArrayList<>();
        NBTTagList tagList = nbt.getTagList(tagName, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            DataUUID loaded = new DataUUID();
            loaded.readData(tagList.getCompoundTagAt(i), SyncType.SAVE);
            newUUIDs.add(loaded);
        }
        return newUUIDs;
    }

    public static NBTTagCompound writeInfoList(NBTTagCompound nbt, List<DataUUID> uuids, String tagName) {
        NBTTagList tagList = new NBTTagList();
        uuids.forEach(obj -> tagList.appendTag(obj.writeData(new NBTTagCompound(), SyncType.SAVE)));
        nbt.setTag(tagName, tagList);
        return nbt;
    }
    */
}