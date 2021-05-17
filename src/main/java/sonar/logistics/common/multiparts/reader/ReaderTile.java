package sonar.logistics.common.multiparts.reader;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.common.multiparts.networking.IDataSourceNode;
import sonar.logistics.server.data.api.IEnvironment;
import sonar.logistics.server.data.sources.IDataSource;
import sonar.logistics.util.PL3Properties;

import java.util.List;

public class ReaderTile extends MultipartTile implements IDataSourceNode, IDataSource, IEnvironment {

    public ReaderTile(MultipartEntry entry) {
        super(entry);
    }

    public Direction getFacing() {
        return entry.getBlockState().get(PL3Properties.ORIENTATION);
    }

    ///

    @Override
    public void addDataSources(List<IDataSource> sources) {
        sources.add(this);
    }

    ///

    @Override
    public IEnvironment getEnvironment() {
        return this;
    }

    ///

    @Override
    public World world() {
        return getHostWorld();
    }

    @Override
    public BlockState state() {
        return world().getBlockState(pos());
    }

    @Override
    public BlockPos pos() {
        return getHostPos().offset(getFacing());
    }

    @Override
    public Direction face() {
        return getFacing();
    }

    @Override
    public TileEntity tile() {
        return world().getTileEntity(pos());
    }
}
