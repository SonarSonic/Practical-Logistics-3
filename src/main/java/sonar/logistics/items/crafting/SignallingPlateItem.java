package sonar.logistics.items.crafting;

import net.minecraft.item.Item;
import sonar.logistics.setup.PL3Registry;

public class SignallingPlateItem extends Item {

    public SignallingPlateItem() {
        super(new Properties().group(PL3Registry.ITEM_GROUP));
        setRegistryName("signalling_plate");
    }
}
