package sonar.logistics.common.blocks.hammer;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import sonar.logistics.common.blocks.PL3Blocks;
import sonar.logistics.common.items.PL3Items;
import sonar.logistics.util.recipes.SimpleRecipeHandlerV1;
import sonar.logistics.util.recipes.SimpleRecipeV1;

public class ForgingHammerRecipes extends SimpleRecipeHandlerV1 {

    public static ForgingHammerRecipes INSTANCE = new ForgingHammerRecipes();
    public int recipeID = 0;

    public ForgingHammerRecipes() {
        super(1, 1);
    }

    @Override
    public void register() {
        register(new ItemStack(PL3Items.SAPPHIRE_GEM), new ItemStack(PL3Items.SAPPHIRE_DUST));
        register(new ItemStack(PL3Blocks.SAPPHIRE_ORE), new ItemStack(PL3Items.SAPPHIRE_DUST, 2));
        register(new ItemStack(Blocks.STONE), new ItemStack(PL3Items.STONE_PLATE, 4));
        register(new ItemStack(Items.DIAMOND), new ItemStack(PL3Items.ETCHED_PLATE, 4));
        register(new ItemStack(Items.REDSTONE), new ItemStack(PL3Items.SIGNALLING_PLATE, 4));
        register(new ItemStack(Items.ENDER_PEARL), new ItemStack(PL3Items.WIRELESS_PLATE, 4));
    }

    public void register(ItemStack input, ItemStack output){
        register(new SimpleRecipeV1("forginghammer_" + recipeID++, Ingredient.fromStacks(input), output));
    }
}
