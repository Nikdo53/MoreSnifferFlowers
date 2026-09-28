package net.abraxator.moresnifferflowers.compat.jei.corruption;

import com.mojang.datafixers.util.Pair;
import net.abraxator.moresnifferflowers.components.Corruptable;
import net.abraxator.moresnifferflowers.init.MSFDataMaps;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CorruptionRecipe(ItemStack source, ItemStack corrupted, int chance) {
    public static List<CorruptionRecipe> createRecipes() {
        List<CorruptionRecipe> recipes = new ArrayList<>();

        for(Map.Entry<ResourceKey<Block>, Corruptable> entry : BuiltInRegistries.BLOCK.getDataMap(MSFDataMaps.CORRUPTABLE).entrySet()) {
            Item source = BuiltInRegistries.BLOCK.getValue(entry.getKey()).asItem();
            int totalWeight = 0;
            for(Pair<Block, Integer> pair : entry.getValue().list()) {
                totalWeight = totalWeight + pair.getSecond();
            }
            for(Pair<Block, Integer> pair : entry.getValue().list()) {
                int weight = pair.getSecond();
                float percentage = ((float) weight /totalWeight) * 100;
                recipes.add(new CorruptionRecipe(source.getDefaultInstance(), pair.getFirst().asItem().getDefaultInstance(), (int) percentage));
            }
        }
        
        for(Map.Entry<Block, Block> entry : Corruptable.HARDCODED_BLOCK.entrySet()) {
            if(entry.getKey() == MSFBlocks.AMBUSH_BOTTOM.get()) {
                recipes.add(new CorruptionRecipe(MSFItems.AMBUSH_SEEDS.toStack(), MSFItems.GARBUSH_SEEDS.toStack(), 100));
            } else if(entry.getKey() == MSFBlocks.AMBUSH_TOP.get()) {
                continue;
            }
            
            recipes.add(new CorruptionRecipe(entry.getKey().asItem().getDefaultInstance(), entry.getValue().asItem().getDefaultInstance(), 100));
        }
        
        return recipes;
    }
}
