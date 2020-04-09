package sonar.logistics.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.setup.PL3Registry;

public class SapphireGemItem extends Item {
    public SapphireGemItem() {
        super(new Item.Properties().group(PL3Registry.ITEM_GROUP));
        setRegistryName("sapphire_gem");
    }
}
