package sonar.logistics.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.setup.PL3Registry;

public class SapphireDustItem extends Item {

    public SapphireDustItem() {
        super(new Properties().group(PL3Registry.ITEM_GROUP));
        setRegistryName("sapphire_dust");
    }
}
