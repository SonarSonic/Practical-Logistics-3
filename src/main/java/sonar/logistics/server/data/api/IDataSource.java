package sonar.logistics.server.data.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.logistics.server.caches.network.PL3Network;
import sonar.logistics.server.data.source.Address;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IDataSource {

    @Nonnull
    Address address();

    @Nullable
    World world();

    @Nullable
    BlockState state();

    @Nullable
    BlockPos pos();

    @Nullable
    Direction face();

    @Nullable
    TileEntity tile();

    @Nullable
    PL3Network network();

    @Nullable
    default Entity entity(){
        return null; //FIXME ENTITY SYSTEM
    }

}