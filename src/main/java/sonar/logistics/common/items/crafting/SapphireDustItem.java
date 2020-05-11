package sonar.logistics.common.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.networking.Registry;

public class SapphireDustItem extends Item {

    public SapphireDustItem() {
        super(new Properties().group(Registry.ITEM_GROUP));
        setRegistryName("sapphire_dust");
    }
}
