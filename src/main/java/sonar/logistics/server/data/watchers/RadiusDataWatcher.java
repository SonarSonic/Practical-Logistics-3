package sonar.logistics.server.data.watchers;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import sonar.logistics.common.multiparts.base.NetworkedTile;
import sonar.logistics.common.multiparts.networking.INetworkedTile;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.util.MathUtils;
import sonar.logistics.util.WorldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RadiusDataWatcher extends DataWatcher {

    public NetworkedTile tile;
    public double radius;

    public List<ServerPlayerEntity> trackingPlayers = new ArrayList<>();
    public boolean sendFullPacket = true;

    public static int tickRate = 20;
    public int updatePlayerTick = MathUtils.randInt(0, tickRate);

    public RadiusDataWatcher(NetworkedTile tile, double range, Consumer<DataWatcher> callback) {
        super(callback);
        this.tile = tile;
        this.radius = range;
    }

    @Override
    public Address getAddress() {
        return tile.getAddress();
    }

    @Override
    public boolean isWatcherActive() {
        return !trackingPlayers.isEmpty();
    }

    @Override
    public void tick() {
        super.tick();
        updatePlayerTick++;
        if(updatePlayerTick == tickRate){
            List<ServerPlayerEntity> currentPlayers = WorldUtils.getPlayersInRadius(tile.getHostPos().getX(), tile.getHostPos().getY(), tile.getHostPos().getZ(), radius, tile.getHostWorld().getDimension().getType());
            if(!trackingPlayers.containsAll(currentPlayers)){
                sendFullPacket = true;
            }
            trackingPlayers = currentPlayers;
            updatePlayerTick = 0;
        }
    }

    @Override
    public void postDataUpdate() {
        super.postDataUpdate();
        if(sendFullPacket){
            DataManager.INSTANCE.addDataToSyncPacket(trackingPlayers, watchingData);
            sendFullPacket = false;
        }else{
            DataManager.INSTANCE.addDataToUpdatePacket(trackingPlayers, changedData);
        }
    }
}
