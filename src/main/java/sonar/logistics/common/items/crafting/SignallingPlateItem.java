package sonar.logistics.common.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.networking.Registry;

public class SignallingPlateItem extends Item {

    public SignallingPlateItem() {
        super(new Properties().group(Registry.ITEM_GROUP));
        setRegistryName("signalling_plate");
    }
}
