package net.abraxator.moresnifferflowers.datagen.recipe.builder;

import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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
        this.ingredient = Ingredient.of(crop);
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
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(new ItemStackTemplate(result));
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> location) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location))
                .rewards(AdvancementRewards.Builder.recipe(location))
                .requirements(AdvancementRequirements.Strategy.OR);
        CropressingRecipe cropressingRecipe = new CropressingRecipe(this.ingredient, this.count, new ItemStackTemplate(result));

        this.criteria.forEach(advancement::addCriterion);
        recipeOutput.accept(location, cropressingRecipe, advancement.build(location.identifier()));

    }

}
