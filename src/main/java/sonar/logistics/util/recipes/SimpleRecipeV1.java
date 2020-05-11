package sonar.logistics.util.recipes;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import sonar.logistics.PL3;

import java.util.List;

public class SimpleRecipeV1 {

    public ResourceLocation loc;
    public List<Ingredient> inputs;
    public List<ItemStack> outputs;

    public SimpleRecipeV1(String recipeName, Ingredient input, ItemStack output){
        loc = new ResourceLocation(PL3.MODID, recipeName);
        inputs = Lists.newArrayList(input);
        outputs = Lists.newArrayList(output);
    }

    public ItemStack getOutput(int i){
        return outputs.get(i).copy();
    }

    public boolean matches(ItemStack...stacks){
        if(stacks.length == inputs.size()) {
            for(int i = 0; i < stacks.length; i++){
                ItemStack test = stacks[i];
                if(!inputs.get(i).test(test)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}