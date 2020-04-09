package sonar.logistics.blocks.ores;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import sonar.logistics.blocks.PL3Blocks;

public class SapphireOreGen {

      public static ConfiguredFeature<?, ?> SAPPHIRE_FEATURE;

      public static int SAPPHIRE_MAX_VEIN_SIZE = 8;
      public static int SAPPHIRE_COUNT_PER_CHUNK = 20;
      public static int SAPPHIRE_BOTTOM_OFFSET = 0;
      public static int SAPPHIRE_TOP_OFFSET = 0;
      public static int SAPPHIRE_MAXIMUM = 128;

      public static void register(){
            SAPPHIRE_FEATURE = getSimpleOreFeature(PL3Blocks.SAPPHIRE_ORE.getDefaultState(), SAPPHIRE_MAX_VEIN_SIZE, (new CountRangeConfig(SAPPHIRE_COUNT_PER_CHUNK, SAPPHIRE_BOTTOM_OFFSET, SAPPHIRE_TOP_OFFSET, SAPPHIRE_MAXIMUM)));

            ForgeRegistries.BIOMES.forEach(biome -> {
                  if(isValidBiome(biome)){
                        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SAPPHIRE_FEATURE);
                  }
            });
      }

      public static boolean isValidBiome(Biome biome) {
            return biome.getCategory() != Category.THEEND && biome.getCategory() != Category.NETHER;
      }

      public static ConfiguredFeature<?, ?> getSimpleOreFeature(BlockState state, int maxVeinSize, CountRangeConfig config){
            return Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, state, maxVeinSize)).withPlacement(Placement.COUNT_RANGE.configure(config));
      }


}
