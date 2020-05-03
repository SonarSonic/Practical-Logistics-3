package sonar.logistics.multiparts.displays;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.base.MultipartTile;
import sonar.logistics.multiparts.displays.api.IDisplay;
import sonar.logistics.utils.PL3Properties;
import sonar.logistics.utils.network.EnumSyncType;

public class DisplayScreenTile extends MultipartTile implements IDisplay {

    public final Vec3d scaling;
    public GSI gsi = new GSI(this);

    public DisplayScreenTile(MultipartEntry entry, Vec3d scaling) {
        super(entry);
        this.scaling = scaling;
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
    public Vec3d getScreenSizing() {
        return scaling;
    }
}
