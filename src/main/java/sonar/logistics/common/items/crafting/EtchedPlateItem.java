package sonar.logistics.common.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.networking.Registry;

public class EtchedPlateItem extends Item {

    public EtchedPlateItem() {
        super(new Properties().group(Registry.ITEM_GROUP));
        setRegistryName("etched_plate");
    }
}
