package sonar.logistics.server.caches;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.logistics.server.cables.*;

import javax.annotation.Nullable;
import java.util.HashMap;

public abstract class AbstractCableCacheListener<C extends ICableCache<C>> implements ICableListener {

    public HashMap<Integer, C> cached = new HashMap<>();

    public void clear(){
        cached.clear();
    }

    public abstract C createNewCache(World world, int globalCacheID, EnumCableTypes cableType);

    public C getCachedData(int globalCacheID){
        return cached.get(globalCacheID);
    }

    @Nullable
    public LocalCableData getCachedData(World world, BlockPos pos, EnumCableTypes cableType){
        WorldCableData worldData = GlobalCableData.getWorldData(world);
        return worldData.getCableData(pos, cableType);
    }

    public int getGlobalNetworkID(World world, BlockPos pos, EnumCableTypes cableType){
        LocalCableData cableData = getCachedData(world, pos, cableType);
        return cableData == null ? -1 : cableData.globalNetworkID;
    }

    public C getCableCache(World world, BlockPos pos, EnumCableTypes cableType){
        int globalNetworkID = getGlobalNetworkID(world, pos, cableType);
        if (globalNetworkID != -1) {
            return cached.get(globalNetworkID);
        }
        return null;
    }

    public C getOrCreateCableCache(World world, BlockPos pos, EnumCableTypes cableType){
        int globalNetworkID = getGlobalNetworkID(world, pos, cableType);
        if (globalNetworkID != -1) {
            return getOrCreateCableCache(world, globalNetworkID, cableType);
        }
        return null;
    }

    public C getOrCreateCableCache(World world, int globalNetworkID, EnumCableTypes cableType){
        C network = cached.get(globalNetworkID);
        if(network == null){
            network = createNewCache(world, globalNetworkID, cableType);
            cached.put(globalNetworkID, network);
        }
        return network;
    }


    ///// ICableListener \\\\\

    @Override
    public void onCableDataCreated(int globalID, EnumCableTypes cableType) {}

    @Override
    public void onCableDataDeleted(int globalID, EnumCableTypes cableType) {
        C network = cached.get(globalID);
        if(network != null){
            network.deleteCache();
        }
    }

    @Override
    public void onCableDataGrown(int globalID, EnumCableTypes cableType) {}

    @Override
    public void onCableDataShrunk(int globalID, EnumCableTypes cableType) {
        C network = cached.get(globalID);
        if(network != null){
            network.shrinkCache();
        }
    }

    @Override
    public void onCableDataMerged(int globalID, int mergedID, EnumCableTypes cableType) {
        C network = cached.get(globalID);
        C merging = cached.get(mergedID);
        if(network == null && merging == null){
            return; //no networks currently exist for these cables
        }
        if(network != null && merging == null){
            return; //if the merging network is just cables, there is nothing to merge.
        }
        if(network != null){
            network.mergeCache(merging);
        }else{
            merging.changeCacheID(globalID);
        }

    }
}
