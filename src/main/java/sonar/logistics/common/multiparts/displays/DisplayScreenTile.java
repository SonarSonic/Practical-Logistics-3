package sonar.logistics.common.multiparts.displays;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.common.multiparts.displays.api.IDisplay;
import sonar.logistics.util.PL3Properties;
import sonar.logistics.util.network.EnumSyncType;

public class DisplayScreenTile extends MultipartTile implements IDisplay {

    public final Quad2D bounds;
    public GSI gsi;

    public DisplayScreenTile(MultipartEntry entry, Quad2D bounds) {
        super(entry);
        this.bounds = bounds;
        this.gsi = new GSI(bounds);
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
    public Quad2D getHostBounds() {
        return bounds;
    }
}
