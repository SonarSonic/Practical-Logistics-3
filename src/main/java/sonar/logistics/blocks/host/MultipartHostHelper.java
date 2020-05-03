package sonar.logistics.blocks.host;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import sonar.logistics.PL3;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.base.MultipartTile;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;

import javax.annotation.Nullable;
import java.util.Optional;

public class MultipartHostHelper {

    @Nullable
    public static NetworkedHostTile getNetworkedHostTile(IBlockReader reader, BlockPos pos){
        TileEntity tile = reader.getTileEntity(pos);
        if(tile instanceof NetworkedHostTile){
            return (NetworkedHostTile) tile;
        }
        return null;
    }

    @Nullable
    public static MultipartHostTile getMultipartHostTile(IBlockReader reader, BlockPos pos){
        TileEntity tile = reader.getTileEntity(pos);
        if(tile instanceof MultipartHostTile){
            return (MultipartHostTile) tile;
        }
        return null;
    }

    @Nullable
    public static MultipartEntry getMultipartEntry(IBlockReader reader, BlockPos pos, EnumMultipartSlot slot){
        MultipartHostTile hostTile = getMultipartHostTile(reader, pos);
        if(hostTile != null){
           return hostTile.getMultipart(slot);
        }
        return null;
    }

    @Nullable
    public static MultipartTile getMultipartTile(IBlockReader reader, BlockPos pos, EnumMultipartSlot slot){
        MultipartEntry entry = getMultipartEntry(reader, pos, slot);
        if(entry != null){
            return entry.getMultipartTile();
        }
        return null;
    }

    /**if the entity is a player will return the set reach distance otherwise it will return 20.0F - the default from the ForgeIngameGui */
    public static double getLookDistance(Entity entity){
        if(entity instanceof PlayerEntity){
            return ((PlayerEntity) entity).getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
        }
        return 20.0F;
    }

    /**returns a pair of vectors, the start and end from the entities eye position to the maximum distance*/
    public static Pair<Vec3d, Vec3d> getEntityLookVectors(Entity entity, double distance, float partialTicks){
        Vec3d vec3d = entity.getEyePosition(partialTicks);
        Vec3d vec3d1 = entity.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
        return Pair.of(vec3d, vec3d2);
    }

    /**to be avoided if possible, only for use where the player entity is unavailable.*/
    @Nullable
    public static Entity getEntityDangerously(IBlockReader reader, ISelectionContext context){
        if(context.getEntity() != null){
            return context.getEntity();
        }
        if(reader instanceof World && ((World) reader).isRemote){
            return PL3.proxy.getClientPlayer();
        }
        return null;
    }

    @Nullable
    public static VoxelShape getMultipartHostCollisionVoxel(IBlockReader reader, BlockPos pos){
        MultipartHostTile host = getMultipartHostTile(reader, pos);
        if(host != null && !host.MULTIPARTS.isEmpty()){ //TODO CACHE THIS?
            VoxelShape hostShape = VoxelShapes.empty();
            for(MultipartEntry multipart : host.MULTIPARTS){
                VoxelShape multipartShape = multipart.getBlockState().getCollisionShape(reader, pos);
                hostShape = VoxelShapes.combine(hostShape, multipartShape, IBooleanFunction.OR);
            }
            return hostShape;
        }
        return VoxelShapes.empty();
    }

    @Nullable
    public static MultipartEntry getRayTraceMultipart(IBlockReader reader, BlockPos pos, Entity entity){
        RayTraceResult rayTraceResult = getRayTraceResult(reader, pos, entity);
        if (rayTraceResult != null && rayTraceResult.hitInfo instanceof MultipartEntry) {
            return ((MultipartEntry)rayTraceResult.hitInfo);
        }
        return null;
    }

    public static RayTraceResult getRayTraceResult(IBlockReader reader, BlockPos pos, Entity entity) {
        if(entity != null) {
            Pair<Vec3d, Vec3d> vectors = getEntityLookVectors(entity, getLookDistance(entity), 0.0F);
            return MultipartHostHelper.getRayTraceResult(reader, pos, vectors.getLeft(), vectors.getRight());
        }
        return null;
    }

    @Nullable
    public static RayTraceResult getRayTraceResult(IBlockReader reader, BlockPos pos, Vec3d start, Vec3d end) {
        MultipartHostTile host = getMultipartHostTile(reader, pos);
        if(host != null){
            BlockRayTraceResult result = null;
            for(MultipartEntry multipart : host.MULTIPARTS){
                VoxelShape multipartShape = multipart.getBlockState().getShape(reader, pos);
                BlockRayTraceResult trace = multipartShape.rayTrace(start, end, pos);
                if(trace != null && (result == null || result.getHitVec().squareDistanceTo(start) > trace.getHitVec().squareDistanceTo(start))){
                    trace.hitInfo = multipart;
                    trace.subHit = multipart.slot.ordinal();
                    result = trace;
                }
            }
            return result;
        }
        return null;
    }

}
