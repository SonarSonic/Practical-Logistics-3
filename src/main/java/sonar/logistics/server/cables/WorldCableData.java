package sonar.logistics.server.cables;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.server.networks.PL3NetworkManager;
import sonar.logistics.utils.ListHelper;
import sonar.logistics.utils.network.EnumSyncType;
import sonar.logistics.utils.network.ISyncable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class WorldCableData implements ISyncable {

    public final int dimension;
    protected final HashMap<Byte, HashSet<LocalCableData>> localCableDataMap = new HashMap<>();
    protected final GlobalCableData globalData;

    public WorldCableData(GlobalCableData globalData, int dimension) {
        this.globalData = globalData;
        this.dimension = dimension;
    }

    public boolean hasCableType(byte cableType){
        return localCableDataMap.containsKey(cableType);
    }

    public HashSet<LocalCableData> getOrCreateLocalCableDataList(byte cableType){
        localCableDataMap.putIfAbsent(cableType, new HashSet<>());
        return localCableDataMap.get(cableType);
    }

    @Nullable
    public LocalCableData getCableData(int networkID, byte cableType){
        if(!hasCableType(cableType)){
            return null;
        }

        for(LocalCableData localData : getOrCreateLocalCableDataList(cableType)){
            if(localData.globalNetworkID == networkID){
                return localData;
            }
        }
        return null;
    }

    @Nullable
    public LocalCableData getCableData(BlockPos pos, byte cableType){
        for(LocalCableData localData : getOrCreateLocalCableDataList(cableType)){
            if(localData.isConnected(pos)){
                return localData;
            }
        }
        return null;
    }


    public void addCable(World world, BlockPos pos, byte cableType){
        LocalCableData existing = getCableData(pos, cableType);
        if(existing != null){
            return;
        }

        List<LocalCableData> adjacent = new ArrayList<>();

        ///// FIND ADJACENT LOCALS \\\\\

        for(Direction dir : Direction.values()){
            BlockPos adjPos = pos.offset(dir);
            if(CableHelper.canConnect(world, pos, adjPos, dir, cableType)){
                LocalCableData data = getCableData(adjPos, cableType);
                if(data != null) {
                    ListHelper.addWithCheck(adjacent, data);
                }else{
                    //TODO FIX BROKEN NETWORK
                }
            }
        }


        ///// CONNECT TO THE LOCALS \\\\\

        if(adjacent.isEmpty()){
            ////no adjacent locals: create a new cable data
            int globalCableID = globalData.getNextGlobalID();
            LocalCableData localData  = new LocalCableData(this, globalCableID, cableType);
            getOrCreateLocalCableDataList(cableType).add(localData);

            localData.doAddCable(world, pos, cableType);

            onLocalDataCreated(localData.globalNetworkID, cableType);
        }else if(adjacent.size() == 1){
            ////one adjacent local: connect to it
            LocalCableData localData  = adjacent.get(0);
            localData.doAddCable(world, pos, cableType);

            onLocalDataGrown(localData.globalNetworkID, cableType);
        }else{
            ////multiple adjacent locals: join them together!
            LocalCableData localData  = null;
            List<Integer> merged = new ArrayList<>();
            for(LocalCableData adjacentData : adjacent){
                if(localData == null){
                    localData = adjacentData;
                }else{
                    localData.merge(adjacentData);
                    getOrCreateLocalCableDataList(cableType).remove(adjacentData);
                    merged.add(adjacentData.globalNetworkID);
                }
            }

            localData.doAddCable(world, pos, cableType);
            onLocalDataGrown(localData.globalNetworkID, cableType);

            for(Integer mergedId : merged){
                onLocalDataMerged(localData.globalNetworkID, mergedId, cableType);
            }

        }
        globalData.markDirty();
    }

    public void removeCable(World world, BlockPos pos, byte cableType){
        LocalCableData localData = getCableData(pos, cableType);
        if(localData == null){
            return;
        }
        localData.doRemoveCable(world, pos, cableType);
        if(localData.cables.isEmpty()){
            getOrCreateLocalCableDataList(cableType).remove(localData);
            onLocalDataDeleted(localData.globalNetworkID, cableType);
            return;
        }

        redistributeNetwork(world, cableType, localData);

        globalData.markDirty();
    }

    /**removes all of the cables on the network and adds them back*/
    public void redistributeNetwork(World world, byte cableType, LocalCableData localData){
        HashSet<BlockPos> oldCables = localData.cables;

        localData.clear();

        Iterator<BlockPos> oldCableIterator = Lists.newArrayList(oldCables).iterator();
        boolean addToExisting = true;

        while(oldCableIterator.hasNext() && !oldCables.isEmpty()){
            BlockPos nextPos = oldCableIterator.next();
            addRemainingLocalPos(world, nextPos, cableType, oldCables, addToExisting ? blockPos -> localData.doAddCable(world, blockPos, localData.cableType) : (blockPos) -> {
                addCable(world, blockPos, localData.cableType); // use the standard add method, this will also handle all network changing calls!
            });
            addToExisting = false;
        }
        onLocalDataShrunk(localData.globalNetworkID, cableType);
    }

    public void addRemainingLocalPos(World world, BlockPos origin, byte cableType, HashSet<BlockPos> oldPos, Consumer<BlockPos> add){
        add.accept(origin);
        oldPos.remove(origin);
        for(Direction dir : Direction.values()){
            BlockPos adjPos = origin.offset(dir);
            if(oldPos.contains(adjPos) && CableHelper.canConnect(world, origin, adjPos, dir, cableType)){
                addRemainingLocalPos(world, adjPos, cableType, oldPos, add);
            }
        }
    }

    /**only to be called when the connections of a specific cable have been changed.*/
    public void checkCableConnection(World world, BlockPos pos, Direction changedDir, byte cableType){
        LocalCableData existing = getCableData(pos, cableType);
        BlockPos adjPos = pos.offset(changedDir);
        boolean canConnect = CableHelper.canConnect(world, pos, adjPos, changedDir, cableType);
        LocalCableData cableData = getCableData(adjPos, cableType);

        if(cableData != null && canConnect && cableData != existing){
            removeCable(world, pos, cableType);
            addCable(world, pos, cableType);
        }
        if(!canConnect && cableData == existing){
            redistributeNetwork(world, cableType, existing);
        }
    }

    public void onCableAdded(World world, BlockPos pos, byte cableType){
        if(cableType == CableHelper.DATA_CABLE_TYPE){
            PL3NetworkManager.INSTANCE.onCableAdded(world, pos, cableType);
        }
    }

    public void onCableRemoved(World world, BlockPos pos, byte cableType){
        if(cableType == CableHelper.DATA_CABLE_TYPE){
            PL3NetworkManager.INSTANCE.onCableRemoved(world, pos, cableType);
        }
    }

    /** the global id of the new network.
     * @param globalID the global id of the new created network*/
    public void onLocalDataCreated(int globalID, byte cableType){
        ///network creation is handled by the first NetworkMultipartHost connecting
    }

    /** called when there are no longer any cables on the network
     * @param globalID the global id of the network which has been deleted */
    public void onLocalDataDeleted(int globalID, byte cableType){
        if(cableType == CableHelper.DATA_CABLE_TYPE){
            PL3NetworkManager.INSTANCE.deleteNetwork(globalID);
        }
    }

    /** called when a cable or cables have been added to the network
     * @param globalID the global id of the network which has grown */
    public void onLocalDataGrown(int globalID, byte cableType){
        ///normal network growth is handled by the NetworkMultipartHost connecting
    }

    /** called when a cable or cables have been removed from the network
     * @param globalID the global id of the network which has shrunk */
    public void onLocalDataShrunk(int globalID, byte cableType){
        if(cableType == CableHelper.DATA_CABLE_TYPE){
            PL3NetworkManager.INSTANCE.shrinkNetwork(globalID);
        }
    }

    /** called when two networks have been combined, resulting in one.
     * @param globalID the global id of the network which all merged networks are now connected to
     * @param mergedID the global id of the network which has now been removed
     */
    public void onLocalDataMerged(int globalID, int mergedID, byte cableType){
        if(cableType == CableHelper.DATA_CABLE_TYPE){
            PL3NetworkManager.INSTANCE.mergeNetworks(globalID, mergedID);
        }
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        ListNBT list = nbt.getList("locals", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < list.size(); i++){
            CompoundNBT tag = list.getCompound(i);
            int networkID = tag.getInt("id");
            byte cableType = tag.getByte("type");
            LocalCableData localData = new LocalCableData(this, networkID, cableType);
            localData.read(tag, EnumSyncType.SAVE);
            getOrCreateLocalCableDataList(cableType).add(localData);
        }

        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        ListNBT list = new ListNBT();
        for(Map.Entry<Byte, HashSet<LocalCableData>> networkData : localCableDataMap.entrySet()){
            for(LocalCableData localData : networkData.getValue()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putInt("id", localData.globalNetworkID);
                tag.putByte("type", localData.cableType);
                localData.write(tag, EnumSyncType.SAVE);
                list.add(tag);
            }
        }
        nbt.put("locals", list);
        return nbt;
    }

    public static void clear(WorldCableData data) {
        data.localCableDataMap.forEach((B, L) -> L.forEach(LocalCableData::clear));
        data.localCableDataMap.clear();
    }
}