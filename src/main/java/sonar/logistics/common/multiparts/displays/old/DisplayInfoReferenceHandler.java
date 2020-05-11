/*
package sonar.logistics.common.multiparts.displays.old;

import net.minecraft.entity.player.EntityPlayerMP;
import sonar.core.api.utils.BlockCoords;
import sonar.core.helpers.FunctionHelper;
import sonar.core.helpers.ListHelper;
import sonar.logistics.api.core.tiles.displays.info.InfoUUID;
import sonar.logistics.api.core.tiles.readers.IInfoProvider;
import sonar.logistics.base.ServerInfoHandler;
import sonar.logistics.base.guidance.errors.ErrorDestroyed;
import sonar.logistics.base.guidance.errors.ErrorDisconnected;
import sonar.logistics.base.guidance.errors.ErrorHelper;
import sonar.logistics.base.listeners.ILogicListenable;
import sonar.logistics.base.listeners.ListenerType;
import sonar.logistics.base.utils.PL2AdditionType;
import sonar.logistics.base.utils.PL2RemovalType;
import sonar.logistics.core.tiles.displays.gsi.DisplayGSI;
import sonar.logistics.core.tiles.displays.gsi.storage.DisplayGSISaveHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class DisplayInfoReferenceHandler {

	public static Map<ILogicListenable, UpdateCause> updates = new HashMap<>();

	public static void queueUpdate(ILogicListenable logicTile, UpdateCause cause) {
		UpdateCause current = updates.get(logicTile);
		if (current == null || current.ordinal() > cause.ordinal()) {
			updates.put(logicTile, cause);
		}
	}

	public static Map<DisplayGSI, List<InfoUUID>> getConnectionsForTile(Map<DisplayGSI, List<InfoUUID>> connected, ILogicListenable logicTile) {
		if (logicTile instanceof IInfoProvider) {
			IInfoProvider provider = (IInfoProvider) logicTile;
			for (int i = 0; i < provider.getMaxInfo(); i++) {
				InfoUUID uuid = new InfoUUID(provider.getIdentity(), i);
				forEachGSIDisplayingUUID(uuid, gsi -> {
					connected.computeIfAbsent(gsi, FunctionHelper.ARRAY);
					ListHelper.addWithCheck(connected.get(gsi), uuid);
				});
			}
		}
		return connected;
	}

	public static void doInfoReferenceConnect(DisplayGSI gsi, List<InfoUUID> uuids) {
		if (uuids.isEmpty()) {
			return;
		}
		BlockCoords coords = gsi.getDisplay().getActualDisplay().getCoords();
		List<ILogicListenable> logicTiles = DisplayHelper.getLocalProviders(gsi.getDisplay(), gsi.getWorld(), coords.getBlockPos());

		UUIDS: for (InfoUUID uuid : uuids) {
			for (ILogicListenable tile : logicTiles) {
				if (tile.getIdentity() == uuid.getIdentity()) {
					ErrorHelper.removeError(gsi, new ErrorDisconnected(uuid, tile));
					doLocalProviderConnect(gsi, tile, uuid);
					continue UUIDS;
				}
			}
			ILogicListenable listen = ServerInfoHandler.instance().getNetworkTileMap().get(uuid.identity);
			if (listen != null) {
				doLocalProviderDisconnect(gsi, listen, uuid);
				ErrorHelper.addError(gsi, new ErrorDisconnected(uuid, listen));
			}
		}
	}

	public static void doInfoReferenceDisconnect(DisplayGSI gsi, List<InfoUUID> uuids) {
		if (uuids.isEmpty()) {
			return;
		}
		for (InfoUUID uuid : uuids) {
			ILogicListenable listen = ServerInfoHandler.instance().getNetworkTileMap().get(uuid.identity);
			if (listen != null) {
				doLocalProviderDisconnect(gsi, listen, uuid);
				ErrorHelper.removeError(gsi, new ErrorDisconnected(uuid, listen));
			}
		}
	}

	private static void doLocalProviderConnect(DisplayGSI display, ILogicListenable logicTile, InfoUUID uuid) {
		logicTile.getListenerList().getDisplayListeners().addListener(display, 0);
		// triggers the info packet to be sent to listeners
		ServerInfoHandler.instance().markChanged(logicTile, uuid);
		display.sendInfoContainerPacket(DisplayGSISaveHandler.DisplayGSISavedData.INFO_REFERENCES);
		display.forEachWatcher(watcher -> logicTile.getListenerList().addListener(watcher, ListenerType.TEMPORARY_LISTENER));
	}

	private static void doLocalProviderDisconnect(DisplayGSI display, ILogicListenable logicTile, InfoUUID uuid) {
		logicTile.getListenerList().getDisplayListeners().removeListener(display, true, 0);
		// triggers the info packet to be sent to listeners
		ServerInfoHandler.instance().markChanged(logicTile, uuid);
		display.sendInfoContainerPacket(DisplayGSISaveHandler.DisplayGSISavedData.INFO_REFERENCES);
	}

	public static void updateLocalProviderConnections() {
		if (!updates.isEmpty()) {
			for (Entry<ILogicListenable, UpdateCause> update : updates.entrySet()) {
				Map<DisplayGSI, List<InfoUUID>> affected = getConnectionsForTile(new HashMap(), update.getKey());
				switch (update.getValue()) {
				case NETWORK_CHANGE:
					affected.forEach(DisplayInfoReferenceHandler::doInfoReferenceConnect);
					break;
				case TILE_DESTROYED:
					affected.forEach((GSI, UUIDS) -> {
						ErrorHelper.removeError(GSI, new ErrorDisconnected(UUIDS, update.getKey()));
						ErrorHelper.addError(GSI, new ErrorDestroyed(UUIDS, update.getKey()));
					});
					break;
				case TILE_UNLOADED:
					affected.forEach((GSI, UUIDS) -> {
						ErrorDisconnected error = new ErrorDisconnected(UUIDS, update.getKey());
						error.chunkUnload = true;
						ErrorHelper.addError(GSI, error);
					});
					break;
				default:
					break;
				}
			}
			updates.clear();
		}
	}


	public static List<EntityPlayerMP> getPlayersWatchingUUID(InfoUUID uuid) {
		List<EntityPlayerMP> players = new ArrayList<>();
		ServerInfoHandler.instance().getGSIMap().values().forEach(gsi -> {
			if (gsi.isDisplayingUUID(uuid)) {
				ListHelper.addWithCheck(players, DisplayViewerHandler.instance().getWatchingPlayers(gsi));
			}
		});
		return players;
	}

	public static void forEachGSIDisplayingUUID(InfoUUID uuid, Consumer<DisplayGSI> action) {
		ServerInfoHandler.instance().getGSIMap().values().forEach(gsi -> {
			if (gsi.isDisplayingUUID(uuid)) {
				action.accept(gsi);
			}
		});
	}

	public enum UpdateCause {
		TILE_DESTROYED, TILE_UNLOADED, NETWORK_CHANGE;

		public static UpdateCause getCause(PL2AdditionType type) {
			return NETWORK_CHANGE;
		}

		public static UpdateCause getCause(PL2RemovalType type) {
			switch (type) {
			case CHUNK_UNLOADED:
				return TILE_UNLOADED;
			case PLAYER_REMOVED:
				return TILE_DESTROYED;
			default:
				return NETWORK_CHANGE;
			}
		}

	}
}
*/
