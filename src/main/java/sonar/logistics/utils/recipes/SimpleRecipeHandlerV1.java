package sonar.logistics.utils.recipes;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class SimpleRecipeHandlerV1 {

    public List<SimpleRecipeV1> recipes = Lists.newArrayList();
    public int inputSize, outputSize;

    public SimpleRecipeHandlerV1(int inputSize, int outputSize){
        this.inputSize=inputSize;
        this.outputSize=outputSize;
        this.register();
    }

    public abstract void register();

    public void register(SimpleRecipeV1 recipe){
        if(recipe.inputs.size() == inputSize && recipe.outputs.size() == outputSize) {
            recipes.add(recipe);
        }
    }

    public SimpleRecipeV1 getMatchingRecipe(ItemStack...stacks){
        for(SimpleRecipeV1 r : recipes){
            if(r.matches(stacks)){
                return r;
            }
        }
        return null;
    }

}
