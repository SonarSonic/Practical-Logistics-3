package sonar.logistics.server.cables;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import sonar.logistics.blocks.PL3Blocks;
import sonar.logistics.blocks.host.MultipartHostHelper;
import sonar.logistics.blocks.host.MultipartHostTile;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;

public class CableHelper {

    public static final byte DATA_CABLE_TYPE = 0;
    public static final byte[] DISPLAY_SCREEN_CABLE_TYPE = new byte[]{ 1,2,3,4,5,6 };

    /***should check the connection in both directions, i.e. check the adjacent block allows the connection to*/
    public static boolean canConnect(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir, byte cableType){
        if(cableType == CableHelper.DATA_CABLE_TYPE){
            return canConnectDataCables(world, cablePos, adjPos, dir);
        }
        return false;
    }

    public static boolean canConnectDataCables(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir){
        return canConnectDataCableOnSide(world, cablePos, dir) && canConnectDataCableOnSide(world, adjPos, dir.getOpposite());
    }

    public static boolean canRenderDataCables(IBlockReader world, BlockPos cablePos, BlockPos adjPos, Direction dir){
        return canConnectDataCableOnSide(world, cablePos, dir) && canConnectDataCableOnSide(world, adjPos, dir.getOpposite()) || canConnectDataCableInternally(world, cablePos, dir);
    }

    public static boolean canConnectDataCableOnSide(IBlockReader world, BlockPos cablePos, Direction dir){
        if(world.getBlockState(cablePos).getBlock() == PL3Blocks.DATA_CABLE){
            ////normal cable blocks don't have any sort of special blocking etc.
            return true;
        }
        MultipartHostTile host = MultipartHostHelper.getMultipartHostTile(world, cablePos);
        if(host != null){
            MultipartEntry entry = host.getMultipart(EnumMultipartSlot.CENTRE);
            if(entry != null && entry.multipart == PL3Blocks.DATA_CABLE){
                MultipartEntry blocking = host.getMultipart(EnumMultipartSlot.fromDirection(dir));
                return blocking == null; //TODO COVERS IMPLEMENTATION ETC.
            }
        }
        return false;
    }

    public static boolean canConnectDataCableInternally(IBlockReader world, BlockPos cablePos, Direction dir){
        MultipartHostTile host = MultipartHostHelper.getMultipartHostTile(world, cablePos);
        if(host != null){
            MultipartEntry entry = host.getMultipart(EnumMultipartSlot.CENTRE);
            if(entry != null && entry.multipart == PL3Blocks.DATA_CABLE){
                MultipartEntry blocking = host.getMultipart(EnumMultipartSlot.fromDirection(dir));
                return blocking != null; //TODO SPECIAL CONNECTIONS ETC
            }
        }
        return false;
    }

}
