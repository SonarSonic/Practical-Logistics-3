package sonar.logistics.client.design.gui;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import sonar.logistics.common.blocks.PL3Blocks;

public class GSIDesignContainer extends Container {

    public GSIDesignContainer(PlayerInventory inv) {
        super(PL3Blocks.GSI_DESIGN_CONTAINER, 0);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
