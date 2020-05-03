package sonar.logistics.server.cables;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.PL3;
import sonar.logistics.utils.network.EnumSyncType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GlobalCableData extends WorldSavedData {

    public static final String ID = "PL3GlobalCableData";
    private final Map<Integer, WorldCableData> worldDataMap = new HashMap<>();
    private int nextGlobalID;

    public GlobalCableData() {
        super(ID);
    }

    public void printData(String action) {
        PL3.LOGGER.debug("PL3: Global Cable Data - " + action);
        long estimated_bytes = 0;
        for(Map.Entry<Integer, WorldCableData> worldData :  worldDataMap.entrySet()){
            for(Map.Entry<EnumCableTypes, HashSet<LocalCableData>> localData : worldData.getValue().localCableDataMap.entrySet()){
                PL3.LOGGER.debug("PL3: Cable Type: {} Network Count: {}", localData.getKey(), localData.getValue().size());
                estimated_bytes += 5; //for NetworkCable Variables.
                for(LocalCableData cableData : localData.getValue()){
                    estimated_bytes += cableData.cables.size()*8;
                }
            }
        }
        PL3.LOGGER.debug("PL3: Estimated Size - {} bytes", estimated_bytes);
    }

    public static GlobalCableData get(World world){
        ServerWorld saveWorld = world.getServer().getWorld(DimensionType.OVERWORLD);
        return saveWorld.getSavedData().getOrCreate(GlobalCableData::new, ID);
    }

    public int getNextGlobalID(){
        markDirty();
        return nextGlobalID++;
    }

    public static WorldCableData getWorldData(World world) {
        GlobalCableData globalData = get(world);
        int dimension = world.getDimension().getType().getId();
        if (!globalData.worldDataMap.containsKey(dimension)) {
            globalData.worldDataMap.put(dimension, new WorldCableData(globalData, dimension));
            globalData.markDirty();
        }
        return globalData.worldDataMap.get(dimension);
    }

    @Override
    public void read(CompoundNBT nbt) {
        nextGlobalID = nbt.getInt("nextNetworkID");
        ListNBT list = nbt.getList("worlds", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < list.size(); i++){
            CompoundNBT tag = list.getCompound(i);
            int dimension = tag.getInt("dim");
            WorldCableData worldData = new WorldCableData(this, dimension);
            worldData.read(tag, EnumSyncType.SAVE);
            worldDataMap.put(dimension, worldData);
        }

        printData("LOADED FROM NBT");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("nextNetworkID", nextGlobalID);
        ListNBT list = new ListNBT();
        for(Map.Entry<Integer, WorldCableData> worldData : worldDataMap.entrySet()){
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("dim", worldData.getKey());
            worldData.getValue().write(tag, EnumSyncType.SAVE);
            list.add(tag);
        }
        nbt.put("worlds", list);

        printData("SAVING TO NBT");
        return nbt;
    }
}
