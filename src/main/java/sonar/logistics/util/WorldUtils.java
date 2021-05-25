package sonar.logistics.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorldUtils {

    public static ServerWorld getWorld(DimensionType type) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return DimensionManager.getWorld(server, type, false, false);
    }

    public static ServerWorld getWorld(World world, DimensionType type) {
        MinecraftServer server = world.getServer();
        return DimensionManager.getWorld(server, type, false, false);
    }

    public static List<ServerPlayerEntity> getPlayersInRadius(double x, double y, double z, double radius, DimensionType dimension){
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        List<ServerPlayerEntity> inRadius = new ArrayList<>();
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();

        for(ServerPlayerEntity player : players){
            if (player.dimension == dimension) {
                double d0 = x - player.getPosX();
                double d1 = y - player.getPosY();
                double d2 = z - player.getPosZ();
                if (d0 * d0 + d1 * d1 + d2 * d2 < radius * radius) {
                    inRadius.add(player);
                }
            }
        }
        return inRadius;
    }

}