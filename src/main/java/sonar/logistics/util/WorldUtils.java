package sonar.logistics.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class WorldUtils {

    public static ServerWorld getWorld(DimensionType type) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return DimensionManager.getWorld(server, type, false, false);
    }

    public static ServerWorld getWorld(World world, DimensionType type) {
        MinecraftServer server = world.getServer();
        return DimensionManager.getWorld(server, type, false, false);
    }

}