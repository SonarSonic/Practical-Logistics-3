package sonar.logistics.common.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.networking.Registry;

public class WirelessPlateItem extends Item {

    public WirelessPlateItem() {
        super(new Properties().group(Registry.ITEM_GROUP));
        setRegistryName("wireless_plate");
    }
}
