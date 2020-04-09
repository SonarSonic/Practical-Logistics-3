package sonar.logistics.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.setup.PL3Registry;

public class WirelessPlateItem extends Item {

    public WirelessPlateItem() {
        super(new Properties().group(PL3Registry.ITEM_GROUP));
        setRegistryName("wireless_plate");
    }
}
