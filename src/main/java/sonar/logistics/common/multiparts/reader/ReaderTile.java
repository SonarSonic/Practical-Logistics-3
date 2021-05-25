package sonar.logistics.common.multiparts.reader;

import net.minecraft.util.Direction;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.NetworkedTile;
import sonar.logistics.common.multiparts.networking.IAddressProvider;
import sonar.logistics.server.address.Address;
import sonar.logistics.util.ListHelper;
import sonar.logistics.util.PL3Properties;
import java.util.List;

public class ReaderTile extends NetworkedTile implements IAddressProvider {

    public ReaderTile(MultipartEntry entry) {
        super(entry);
    }

    public Direction getFacing() {
        return entry.getBlockState().get(PL3Properties.ORIENTATION);
    }

    @Override
    public void addToExternalAddressList(List<Address> addressList) {
        ListHelper.addWithCheck(addressList, Address.createBlockAddress(getHostPos().offset(getFacing()), getHostWorld().getDimension().getType(), getFacing()));
    }

    @Override
    public void addToGlobalAddressList(List<Address> addressList) {
        ///NOP
    }
}
