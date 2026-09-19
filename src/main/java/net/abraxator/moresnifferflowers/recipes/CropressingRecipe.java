package net.abraxator.moresnifferflowers.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.MSFRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record CropressingRecipe(Ingredient ingredient, int count, ItemStackTemplate result) implements Recipe<SingleRecipeInput> {
    public static final MapCodec<CropressingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(CropressingRecipe::ingredient),
                    Codec.INT.fieldOf("count").forGetter(CropressingRecipe::count),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(CropressingRecipe::result)
            ).apply(builder, CropressingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CropressingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, CropressingRecipe::ingredient,
            ByteBufCodecs.INT, CropressingRecipe::count,
            ItemStackTemplate.STREAM_CODEC, CropressingRecipe::result,
            CropressingRecipe::new
    );

    public static final RecipeSerializer<CropressingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(SingleRecipeInput pInput, Level level) {
        ItemStack itemStack = pInput.getItem(0);
        return itemStack.getCount() >= count && ingredient.test(itemStack.copyWithCount(1));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput pInput) {
        return this.result.create();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<CropressingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return MSFRecipes.Types.CROPRESSING.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

}
