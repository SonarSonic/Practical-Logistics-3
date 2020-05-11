package sonar.logistics.common.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.networking.Registry;

public class SapphireGemItem extends Item {
    public SapphireGemItem() {
        super(new Item.Properties().group(Registry.ITEM_GROUP));
        setRegistryName("sapphire_gem");
    }
}
