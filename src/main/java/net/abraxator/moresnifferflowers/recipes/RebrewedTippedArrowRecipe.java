package net.abraxator.moresnifferflowers.recipes;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFRecipes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RebrewedTippedArrowRecipe extends CustomRecipe {
    public static final MapCodec<RebrewedTippedArrowRecipe> CODEC = MapCodec.unit(RebrewedTippedArrowRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, RebrewedTippedArrowRecipe> STREAM_CODEC = StreamCodec.of(
            (_, _) -> {}, b -> new RebrewedTippedArrowRecipe()
    );

    public static final RecipeSerializer<RebrewedTippedArrowRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput pInput, Level level) {
        if (pInput.width() == 3 && pInput.height() == 3) {
            for (int i = 0; i < pInput.width(); i++) {
                for (int j = 0; j < pInput.height(); j++) {
                    ItemStack itemstack = pInput.getItem(i + j * pInput.width());
                    if (itemstack.isEmpty()) {
                        return false;
                    }

                    if (i == 1 && j == 1) {
                        if (!itemstack.is(MSFItems.REBREWED_LINGERING_POTION)) {
                            return false;
                        }
                    } else if (!itemstack.is(Items.ARROW)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingInput pInput) {
        ItemStack itemstack = pInput.getItem(1 + pInput.width());
        if (!itemstack.is(MSFItems.REBREWED_LINGERING_POTION)) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = new ItemStack(Items.TIPPED_ARROW, 8);
            itemstack1.set(DataComponents.POTION_CONTENTS, itemstack.get(DataComponents.POTION_CONTENTS));
            return itemstack1;
        }
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return MSFRecipes.Serializer.REBREWED_TIPPED_ARROW.get();
    }
}
