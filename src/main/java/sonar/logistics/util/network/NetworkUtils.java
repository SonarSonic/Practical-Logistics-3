package sonar.logistics.util.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.logistics.PL3;

import javax.annotation.Nullable;

public class NetworkUtils {

    public static World getWorld(NetworkEvent.Context context){
        if(context.getDirection() == NetworkDirection.PLAY_TO_SERVER){
            return context.getSender().getEntityWorld();
        }
        return PL3.proxy.getClientWorld();
    }

    @Nullable
    public static TileEntity getSafeTileEntity(BlockPos pos, NetworkEvent.Context context){
        World world = getWorld(context);
        if(world != null && world.isBlockLoaded(pos)){
            return world.getTileEntity(pos);
        }
        return null;
    }


}
