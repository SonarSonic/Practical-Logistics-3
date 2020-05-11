package sonar.logistics.multiparts.displays;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.base.MultipartTile;
import sonar.logistics.multiparts.displays.api.IDisplay;
import sonar.logistics.utils.PL3Properties;
import sonar.logistics.utils.network.EnumSyncType;

public class DisplayScreenTile extends MultipartTile implements IDisplay {

    public final Quad2D bounds;
    public GSI gsi = new GSI(this);

    public DisplayScreenTile(MultipartEntry entry, Quad2D bounds) {
        super(entry);
        this.bounds = bounds;
    }

    public void tick() {
        super.tick();
    }

    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType) {
        return tag;
    }

    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType) {
        return tag;
    }

    @Override
    public GSI getGSI() {
        return gsi;
    }

    @Override
    public Direction getFacing() {
        return entry.getBlockState().get(PL3Properties.ORIENTATION);
    }

    @Override
    public BlockPos getPos() {
        return getHostPos();
    }

    @Override
    public Quad2D getGSIBounds() {
        return bounds;
    }
}
