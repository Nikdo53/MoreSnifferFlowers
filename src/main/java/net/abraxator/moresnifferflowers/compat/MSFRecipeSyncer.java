package net.abraxator.moresnifferflowers.compat;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.*;

public class MSFRecipeSyncer {
    public static final MSFRecipeSyncer INSTANCE = new MSFRecipeSyncer();

    public final Set<RecipeType<?>> knownRecipeTypes = Collections.newSetFromMap(new IdentityHashMap<>());
    public RecipeMap recipeMap = RecipeMap.EMPTY;

    public <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getRecipeMapForType(Level level, RecipeType<T> recipeType) {
        if (level instanceof ClientLevel) {
            if (!knownRecipeTypes.contains(recipeType)) {
                MoreSnifferFlowers.LOGGER.warn("Haven't received recipes of type {} from server yet.", recipeType);
                return List.of();
            }

            return List.copyOf(recipeMap.byType(recipeType));
        }

        ServerLevel serverLevel = (ServerLevel) level;
        return List.copyOf(serverLevel.recipeAccess().recipeMap().byType(recipeType));
    }
}
