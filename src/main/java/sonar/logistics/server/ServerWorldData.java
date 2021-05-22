package sonar.logistics.server;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import sonar.logistics.util.WorldUtils;

public class ServerWorldData extends WorldSavedData {

    public static final String ID = "PL3ServerWorldData";
    private int nextIdentity;

    public ServerWorldData() {
        super(ID);
    }

    public static ServerWorldData get(){
        ServerWorld saveWorld = WorldUtils.getWorld(DimensionType.OVERWORLD);
        return saveWorld.getSavedData().getOrCreate(ServerWorldData::new, ID);
    }

    public int getNextIdentity(){
        markDirty();
        return nextIdentity++;
    }

    @Override
    public void read(CompoundNBT nbt) {
        nextIdentity = nbt.getInt("identity");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("identity", nextIdentity);
        return nbt;
    }
}
