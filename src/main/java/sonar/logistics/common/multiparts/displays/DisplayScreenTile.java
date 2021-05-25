package sonar.logistics.common.multiparts.displays;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.common.multiparts.base.NetworkedTile;
import sonar.logistics.server.data.watchers.DataWatcher;
import sonar.logistics.server.data.watchers.RadiusDataWatcher;
import sonar.logistics.util.vectors.Quad2D;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.displays.api.IDisplay;
import sonar.logistics.util.PL3Properties;
import sonar.logistics.util.network.EnumSyncType;

import javax.annotation.Nullable;

//for small displays which don't connect to other displays
public class DisplayScreenTile extends NetworkedTile implements IDisplay {

    public final Quad2D bounds;
    public final GSI gsi;
    public final DataWatcher dataWatcher;

    public DisplayScreenTile(MultipartEntry entry, Quad2D bounds) {
        super(entry);
        this.bounds = bounds;
        this.gsi = new GSI(bounds);
        this.gsi.display = this;
        this.dataWatcher = new RadiusDataWatcher(this, 25, (watcher) -> queueTileUpdate());
    }

    public void tick() {
        super.tick();
        if(getHostWorld().isRemote) {
            gsi.tick();
        }
    }

    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType) {
        super.write(tag, syncType);
        dataWatcher.write(tag, syncType);
        return tag;
    }

    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType) {
        super.read(tag, syncType);
        dataWatcher.read(tag, syncType);
        return tag;
    }

    @Override
    public GSI getGSI() {
        return gsi;
    }

    @Nullable
    @Override
    public DataWatcher getDataWatcher() {
        return dataWatcher;
    }

    @Override
    public Direction getFacing() {
        return entry.getBlockState().get(PL3Properties.ORIENTATION);
    }

    @Override
    public BlockPos getPos() {
        return getHostPos();
    }
}