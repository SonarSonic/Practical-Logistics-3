package sonar.logistics.server.cables;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.ISyncable;

import java.util.HashSet;

public class LocalCableData implements ISyncable {

    public final WorldCableData worldData;
    public final int globalNetworkID;
    public final EnumCableTypes cableType;

    public HashSet<BlockPos> cables = new HashSet<>();

    protected LocalCableData(WorldCableData worldData, int globalNetworkID, EnumCableTypes cableType) {
        this.worldData = worldData;
        this.globalNetworkID = globalNetworkID;
        this.cableType = cableType;
    }

    public boolean isConnected(BlockPos pos){
        return cables.contains(pos);
    }

    public void doAddCable(World world, BlockPos pos, EnumCableTypes cableType){
        cables.add(pos);
    }

    public void doRemoveCable(World world, BlockPos pos, EnumCableTypes cableType){
        cables.remove(pos);
    }

    public void merge(LocalCableData merging) {
        cables.addAll(merging.cables);
    }

    public void clear() {
        cables = new HashSet<>();
    }

    @Override
    public int hashCode() {
        return globalNetworkID;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LocalCableData){
            return ((LocalCableData) obj).globalNetworkID == globalNetworkID;
        }
        return false;
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        ListNBT list = nbt.getList("cables", Constants.NBT.TAG_LONG);
        for(int i = 0; i < list.size(); i++){
            long next = ((LongNBT)list.get(i)).getLong();
            cables.add(BlockPos.fromLong(next));
        }
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        ListNBT list = new ListNBT();
        for(BlockPos pos : cables){
            list.add(LongNBT.valueOf(pos.toLong()));
        }
        nbt.put("cables", list);
        return nbt;
    }
}
