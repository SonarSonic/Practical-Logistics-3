package sonar.logistics.server.data.source;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.data.api.IDataSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Environment implements IDataSource {

    ///// ENVIRONMENT \\\\\
    public final Address address;
    public World world;
    public BlockState state;
    public BlockPos pos;
    public Direction face;
    public TileEntity tile;
    public PL3Network network;
    public Entity entity;

    public Environment(Address address) {
        this.address = address;
    }

    public void reset() {
        world = null;
        pos = null;
        state = null;
        face = null;
        tile = null;
        network = null;
        entity = null;
    }

    @Nonnull
    @Override
    public Address address() {
        return address;
    }

    @Nullable
    @Override
    public World world() {
        return world;
    }

    @Nullable
    @Override
    public BlockState state() {
        return state;
    }

    @Nullable
    @Override
    public BlockPos pos() {
        return pos;
    }

    @Nullable
    @Override
    public Direction face() {
        return face;
    }

    @Nullable
    @Override
    public TileEntity tile() {
        return tile;
    }

    @Nullable
    @Override
    public PL3Network network() {
        return network;
    }

    @Nullable
    @Override
    public Entity entity() {
        return entity;
    }
}
