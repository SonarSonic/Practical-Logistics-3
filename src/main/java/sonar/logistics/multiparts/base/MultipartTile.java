package sonar.logistics.multiparts.base;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sonar.logistics.utils.network.EnumSyncType;
import sonar.logistics.utils.network.ISyncable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultipartTile implements ICapabilityProvider, ISyncable {

    protected final MultipartEntry entry;

    public MultipartTile(MultipartEntry entry) {
        super();
        this.entry = entry;
    }

    //// KEY METHODS \\\\

    public void tick(){}

    public final MultipartEntry getEntry() {
        return entry;
    }

    public final World getHostWorld() {
        return entry.getHost().getWorld();
    }

    public final BlockPos getHostPos() {
        return entry.getHost().getPos();
    }

    public final void queueMarkDirty() {
        entry.getHost().queueMarkDirty();
    }

    public final void queueSyncPacket() {
        entry.getHost().queueSyncPacket();
    }




    //// NBT METHODS \\\\

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
        return tag;
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
        return tag;
    }



    //// LOADING METHODS \\\\

    protected boolean validated, hadLoadTick;

    public void onLoadTick(){}

    public final void onLoad(){
        if(!hadLoadTick){
            onLoadTick();
        }
    }

    public final void onUnload(){
        if(hadLoadTick){
            hadLoadTick = false;
        }
    }

    public boolean isValid(){
        return validated;
    }

    public void validate() {
        this.validated = true;
    }

    public void invalidate() {
        this.validated = false;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }
}
