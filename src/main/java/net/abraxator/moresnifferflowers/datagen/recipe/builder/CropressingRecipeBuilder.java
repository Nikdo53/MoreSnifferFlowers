package net.abraxator.moresnifferflowers.datagen.recipe.builder;

import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class CropressingRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private Ingredient ingredient;
    private int count;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public CropressingRecipeBuilder(ItemLike result) {
        this.result = result.asItem();
    }

    public RecipeBuilder requiresCrop(Item crop) {
        this.ingredient = Ingredient.of(new ItemStack(crop));
        this.count = 16;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> pCriterion) {
        this.criteria.put(name, pCriterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, Identifier id) {
        this.ensureValid(id);
        Advancement.Builder advancement = recipeOutput.advancement()
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                        .rewards(AdvancementRewards.Builder.recipe(id))
                        .requirements(AdvancementRequirements.Strategy.OR);
        CropressingRecipe cropressingRecipe = new CropressingRecipe(this.ingredient, this.count, this.result.getDefaultInstance());
        
        this.criteria.forEach(advancement::addCriterion);
        recipeOutput.accept(id, cropressingRecipe, advancement.build(id));
    }

    private void ensureValid(Identifier id) {
        if(this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }
}
