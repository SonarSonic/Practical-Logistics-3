package sonar.logistics.common.multiparts.reader;

import net.minecraft.util.Direction;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.common.multiparts.base.NetworkedTile;
import sonar.logistics.common.multiparts.networking.ISourceAddressProvider;
import sonar.logistics.server.data.source.Address;
import sonar.logistics.util.PL3Properties;
import java.util.List;

public class ReaderTile extends NetworkedTile implements ISourceAddressProvider {

    public ReaderTile(MultipartEntry entry) {
        super(entry);
    }

    public Direction getFacing() {
        return entry.getBlockState().get(PL3Properties.ORIENTATION);
    }

    @Override
    public void addSourceAddresses(List<Address> addressList) {
        addressList.add(Address.createBlockAddress(getHostPos().offset(getFacing()), getHostWorld().getDimension().getType(), getFacing()));
    }
}
