package sonar.logistics.server.address;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.data.api.IData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Environment {

    ///// ENVIRONMENT \\\\\
    public final Address address;
    public World world;
    public BlockState state;
    public BlockPos pos;
    public Direction face;
    public TileEntity tile;
    public Entity entity;
    public PL3Network network;
    public INetworkedTile networkedTile;
    public IData data;

    public Environment(Address address) {
        this.address = address;
    }

    public void reset() {
        world = null;
        pos = null;
        state = null;
        face = null;
        tile = null;
        entity = null;
        network = null;
        networkedTile = null;
        data = null;
    }

    @Nonnull
    public Address address() {
        return address;
    }

    @Nullable
    public World world() {
        return world;
    }

    @Nullable
    public BlockState state() {
        return state;
    }

    @Nullable
    public BlockPos pos() {
        return pos;
    }

    @Nullable
    public Direction face() {
        return face;
    }

    @Nullable
    public TileEntity tile() {
        return tile;
    }

    @Nullable
    public PL3Network network() {
        return network;
    }

    @Nullable
    public Entity entity() {
        return entity;
    }

    @Nullable
    public INetworkedTile networkedTile() {
        return networkedTile;
    }

    @Nullable
    public IData data() {
        return data;
    }
}
