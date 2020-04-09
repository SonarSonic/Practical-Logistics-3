package sonar.logistics.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.setup.PL3Registry;

public class StonePlateItem extends Item {

    public StonePlateItem() {
        super(new Properties().group(PL3Registry.ITEM_GROUP));
        setRegistryName("stone_plate");
    }
}
