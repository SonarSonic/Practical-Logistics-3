package sonar.logistics.server.cables;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.util.ListHelper;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.ISyncable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class WorldCableData implements ISyncable {

    public final int dimension;
    protected final HashMap<EnumCableTypes, HashSet<LocalCableData>> localCableDataMap = new HashMap<>();
    protected final GlobalCableData globalData;

    public WorldCableData(GlobalCableData globalData, int dimension) {
        this.globalData = globalData;
        this.dimension = dimension;
    }

    public boolean hasCableType(EnumCableTypes cableType){
        return localCableDataMap.containsKey(cableType);
    }

    public HashSet<LocalCableData> getOrCreateLocalCableDataList(EnumCableTypes cableType){
        localCableDataMap.putIfAbsent(cableType, new HashSet<>());
        return localCableDataMap.get(cableType);
    }

    @Nullable
    public LocalCableData getCableData(int networkID, EnumCableTypes cableType){
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
    public LocalCableData getCableData(BlockPos pos, EnumCableTypes cableType){
        for(LocalCableData localData : getOrCreateLocalCableDataList(cableType)){
            if(localData.isConnected(pos)){
                return localData;
            }
        }
        return null;
    }


    public void addCable(World world, BlockPos pos, EnumCableTypes cableType){
        LocalCableData existing = getCableData(pos, cableType);
        if(existing != null){
            return;
        }

        List<LocalCableData> adjacent = new ArrayList<>();

        ///// FIND ADJACENT LOCALS \\\\\

        for(Direction dir : Direction.values()){
            BlockPos adjPos = pos.offset(dir);
            if(cableType.getCableHelper().canConnectCables(world, pos, adjPos, dir, cableType)){
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

            cableType.getCableListener().onCableAdded(world, pos, cableType);
            cableType.getCableListener().onCableDataCreated(localData.globalNetworkID, cableType);

        }else if(adjacent.size() == 1){
            ////one adjacent local: connect to it
            LocalCableData localData  = adjacent.get(0);
            localData.doAddCable(world, pos, cableType);

            cableType.getCableListener().onCableAdded(world, pos, cableType);
            cableType.getCableListener().onCableDataGrown(localData.globalNetworkID, cableType);

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
            cableType.getCableListener().onCableAdded(world, pos, cableType);
            cableType.getCableListener().onCableDataGrown(localData.globalNetworkID, cableType);

            for(Integer mergedId : merged){
                cableType.getCableListener().onCableDataMerged(localData.globalNetworkID, mergedId, cableType);
            }

        }
        globalData.markDirty();
    }

    public void removeCable(World world, BlockPos pos, EnumCableTypes cableType){
        LocalCableData localData = getCableData(pos, cableType);
        if(localData == null){
            return;
        }

        localData.doRemoveCable(world, pos, cableType);
        cableType.getCableListener().onCableRemoved(world, pos, cableType);

        if(localData.cables.isEmpty()){
            getOrCreateLocalCableDataList(cableType).remove(localData);
            cableType.getCableListener().onCableDataDeleted(localData.globalNetworkID, cableType);
            return;
        }
        redistributeNetwork(world, cableType, localData);
        globalData.markDirty();
    }

    /**removes all of the cables on the network and adds them back*/
    public void redistributeNetwork(World world, EnumCableTypes cableType, LocalCableData localData){
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
        cableType.getCableListener().onCableDataShrunk(localData.globalNetworkID, cableType);
    }

    public void addRemainingLocalPos(World world, BlockPos origin, EnumCableTypes cableType, HashSet<BlockPos> oldPos, Consumer<BlockPos> add){
        add.accept(origin);
        oldPos.remove(origin);
        for(Direction dir : Direction.values()){
            BlockPos adjPos = origin.offset(dir);
            if(oldPos.contains(adjPos) && cableType.getCableHelper().canConnectCables(world, origin, adjPos, dir, cableType)){
                addRemainingLocalPos(world, adjPos, cableType, oldPos, add);
            }
        }
    }

    /**only to be called when the connections of a specific cable have been changed.*/
    public void checkCableConnection(World world, BlockPos pos, Direction changedDir, EnumCableTypes cableType){
        LocalCableData existing = getCableData(pos, cableType);
        BlockPos adjPos = pos.offset(changedDir);
        boolean canConnect = cableType.getCableHelper().canConnectCables(world, pos, adjPos, changedDir, cableType);
        LocalCableData cableData = getCableData(adjPos, cableType);

        if(cableData != null && canConnect && cableData != existing){
            removeCable(world, pos, cableType);
            addCable(world, pos, cableType);
        }
        if(!canConnect && cableData == existing){
            redistributeNetwork(world, cableType, existing);
        }
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        ListNBT list = nbt.getList("locals", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < list.size(); i++){
            CompoundNBT tag = list.getCompound(i);
            int networkID = tag.getInt("id");
            EnumCableTypes cableType = EnumCableTypes.values()[tag.getByte("type")];
            LocalCableData localData = new LocalCableData(this, networkID, cableType);
            localData.read(tag, EnumSyncType.SAVE);
            getOrCreateLocalCableDataList(cableType).add(localData);
        }

        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        ListNBT list = new ListNBT();
        for(Map.Entry<EnumCableTypes, HashSet<LocalCableData>> networkData : localCableDataMap.entrySet()){
            for(LocalCableData localData : networkData.getValue()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putInt("id", localData.globalNetworkID);
                tag.putByte("type", (byte)localData.cableType.ordinal());
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