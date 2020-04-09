package sonar.logistics.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.setup.PL3Registry;

public class EtchedPlateItem extends Item {

    public EtchedPlateItem() {
        super(new Properties().group(PL3Registry.ITEM_GROUP));
        setRegistryName("etched_plate");
    }
}
