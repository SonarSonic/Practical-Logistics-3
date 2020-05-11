package sonar.logistics.common.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.networking.Registry;

public class StonePlateItem extends Item {

    public StonePlateItem() {
        super(new Properties().group(Registry.ITEM_GROUP));
        setRegistryName("stone_plate");
    }
}
