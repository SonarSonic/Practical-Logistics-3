package sonar.logistics.blocks.ores;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class SapphireOreBlock extends Block {
    public SapphireOreBlock() {
        super(Properties.create(Material.ROCK)
                .sound(SoundType.STONE)
                .hardnessAndResistance(4.0f));
        setRegistryName("sapphire_ore");
    }
}
