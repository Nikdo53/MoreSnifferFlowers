package net.abraxator.moresnifferflowers.datagen.datamaps;

import com.mojang.datafixers.util.Pair;
import net.abraxator.moresnifferflowers.components.Corruptable;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFDataMaps;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.components.nutrition.Nutrition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Strippable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MSFDataMapsProvider extends DataMapProvider {
    private static final String FARMERS_DELIGHT = "farmersdelight";
    private static final ICondition FARMERS_DELIGHT_LOADED = new ModLoadedCondition(FARMERS_DELIGHT);

    public MSFDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void gather(HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(MSFItems.DAWNBERRY_VINE_SEEDS, new Compostable(0.3F), false)
                .add(MSFItems.DAWNBERRY, new Compostable(0.3F), false)
                .add(MSFItems.AMBUSH_SEEDS, new Compostable(0.3F), false)
                .add(MSFItems.CAULORFLOWER_SEEDS, new Compostable(0.4F), false)
                .add(MSFItems.DYESPRIA_SEEDS, new Compostable(0.4F), false)
                .add(MSFItems.BONMEELIA_SEEDS, new Compostable(0.5F), false)
                .add(MSFItems.CROPRESSED_BEETROOT, new Compostable(1.0F), false)
                .add(MSFItems.CROPRESSED_NETHERWART, new Compostable(1.0F), false)
                .add(MSFItems.CROPRESSED_WHEAT, new Compostable(1.0F), false)
                .add(MSFItems.CROPRESSED_POTATO, new Compostable(1.0F), false)
                .add(MSFItems.CROPRESSED_CARROT, new Compostable(1.0F), false)
                .add(MSFBlocks.CORRUPTED_SAPLING.getId(), new Compostable(1.0F), false)
                .add(MSFBlocks.VIVICUS_SAPLING.getId(), new Compostable(1.0F), false)
                .add(MSFBlocks.CORRUPTED_LEAVES.getId(), new Compostable(1.0F), false)
                .add(MSFBlocks.VIVICUS_LEAVES.getId(), new Compostable(1.0F), false);

        this.builder(NeoForgeDataMaps.STRIPPABLES)
                .add(MSFBlocks.CORRUPTED_LOG, new Strippable(MSFBlocks.STRIPPED_CORRUPTED_LOG.get()), false)
                .add(MSFBlocks.CORRUPTED_WOOD, new Strippable(MSFBlocks.STRIPPED_CORRUPTED_WOOD.get()), false)
                .add(MSFBlocks.VIVICUS_LOG, new Strippable(MSFBlocks.STRIPPED_VIVICUS_LOG.get()), false)
                .add(MSFBlocks.VIVICUS_WOOD, new Strippable(MSFBlocks.STRIPPED_VIVICUS_WOOD.get()), false);
        
        this.builder(MSFDataMaps.CORRUPTABLE)
                .add(holder(Blocks.GRASS_BLOCK), new Corruptable( List.of(
                        Pair.of(MSFBlocks.CORRUPTED_GRASS_BLOCK.get(), 15),
                        Pair.of(Blocks.COARSE_DIRT, 85)
                        )), false)
                .add(holder(Blocks.DIRT), new Corruptable(Blocks.COARSE_DIRT), false)
                .add(holder(Blocks.STONE), new Corruptable(Blocks.NETHERRACK), false)
                .add(holder(Blocks.DEEPSLATE), new Corruptable(Blocks.BLACKSTONE), false)
                .add(BlockTags.LOGS, new Corruptable(MSFBlocks.DECAYED_LOG.get()), false)
                .remove(MSFBlocks.CORRUPTED_LOG)
                .remove(MSFBlocks.DECAYED_LOG)
                .remove(MSFBlocks.CORRUPTED_WOOD)
                .remove(MSFBlocks.STRIPPED_CORRUPTED_LOG)
                .remove(MSFBlocks.STRIPPED_CORRUPTED_WOOD)
                .add(BlockTags.LEAVES, new Corruptable(Blocks.AIR), false)
                .remove(MSFBlocks.CORRUPTED_LEAVES)
                .remove(MSFBlocks.CORRUPTED_LEAVES_BUSH);

        this.builder(MSFDataMaps.NUTRITION)
                .add(Items.MUSHROOM_STEW.builtInRegistryHolder(), Nutrition.of(36, 60, 0, 0, 0), false)
                .add(Items.PORKCHOP.builtInRegistryHolder(), Nutrition.of(72, 30, 0, 0, 0), false)
                .add(Items.COOKED_PORKCHOP.builtInRegistryHolder(), Nutrition.of(48, 70, 0, 0, 0), false)
                .add(Items.ENCHANTED_GOLDEN_APPLE.builtInRegistryHolder(), Nutrition.of(100, 100, 100, 100, 100), false)
                .add(Items.COD.builtInRegistryHolder(), Nutrition.of(60, 30, 0, 0, 0), false)
                .add(Items.SALMON.builtInRegistryHolder(), Nutrition.of(60, 50, 0, 0, 0), false)
                .add(Items.TROPICAL_FISH.builtInRegistryHolder(), Nutrition.of(60, 80, 0, 0, 0), false)
                .add(Items.PUFFERFISH.builtInRegistryHolder(), Nutrition.of(50, 80, 0, 0, 80), false)
                .add(Items.COOKED_COD.builtInRegistryHolder(), Nutrition.of(50, 50, 0, 0, 0), false)
                .add(Items.COOKED_SALMON.builtInRegistryHolder(), Nutrition.of(60, 70, 0, 0, 0), false)
                .add(Items.COOKIE.builtInRegistryHolder(), Nutrition.of(15, 10, 80, 0, 0), false)
                .add(Items.DRIED_KELP.builtInRegistryHolder(), Nutrition.of(70, 40, 0, 0, 0), false)
                .add(Items.COOKED_BEEF.builtInRegistryHolder(), Nutrition.of(70, 60, 0, 0, 0), false)
                .add(Items.CHICKEN.builtInRegistryHolder(), Nutrition.of(50, 40, 0, 0, 0), false)
                .add(Items.COOKED_CHICKEN.builtInRegistryHolder(), Nutrition.of(35, 30, 0, 0, 0), false)
                .add(Items.BAKED_POTATO.builtInRegistryHolder(), Nutrition.of(60, 60, 0, 0, 0), false)
                .add(Items.RABBIT.builtInRegistryHolder(), Nutrition.of(60, 50, 0, 0, 0), false)
                .add(Items.COOKED_RABBIT.builtInRegistryHolder(), Nutrition.of(50, 70, 0, 0, 0), false)
                .add(Items.RABBIT_STEW.builtInRegistryHolder(), Nutrition.of(75, 70, 0, 0, 30), false)
                .add(Items.MUTTON.builtInRegistryHolder(), Nutrition.of(60, 50, 0, 0, 0), false)
                .add(Items.COOKED_MUTTON.builtInRegistryHolder(), Nutrition.of(45, 70, 0, 0, 0), false)
                .add(Items.BEETROOT_SOUP.builtInRegistryHolder(), Nutrition.of(80, 50, 0, 0, 0), false)
                .add(Items.SUSPICIOUS_STEW.builtInRegistryHolder(), Nutrition.of(1, 1, 1, 1, 1), false)
                .add(Items.BEEF.builtInRegistryHolder(), Nutrition.of(60, 40, 0, 0, 0), false)
                .add(Items.ROTTEN_FLESH.builtInRegistryHolder(), Nutrition.of(25, 0, 0, 0, 80), false)
                .add(Items.SPIDER_EYE.builtInRegistryHolder(), Nutrition.of(0, 0, 0, 0, 80), false)
                .add(Items.POISONOUS_POTATO.builtInRegistryHolder(), Nutrition.of(70, 0, 0, 0, 90), false)
                .add(Items.APPLE.builtInRegistryHolder(), Nutrition.of(0, 0, 60, 30, 0), false)
                .add(Items.GOLDEN_APPLE.builtInRegistryHolder(), Nutrition.of(0, 0, 70, 50, 0), false)
                .add(Items.MELON_SLICE.builtInRegistryHolder(), Nutrition.of(25, 0, 60, 40, 0), false)
                .add(Items.CHORUS_FRUIT.builtInRegistryHolder(), Nutrition.of(0, 0, 20, 80, 0), false)
                .add(Items.SWEET_BERRIES.builtInRegistryHolder(), Nutrition.of(0, 0, 70, 40, 0), false)
                .add(Items.GLOW_BERRIES.builtInRegistryHolder(), Nutrition.of(0, 0, 40, 70, 0), false)
                .add(Items.PUMPKIN_PIE.builtInRegistryHolder(), Nutrition.of(35, 0, 70, 0, 0), false)
                .add(Items.BEETROOT.builtInRegistryHolder(), Nutrition.of(80, 0, 5, 0, 0), false)
                .add(Items.SUGAR.builtInRegistryHolder(), Nutrition.of(0, 0, 100, 0, 0), false)
                .add(Items.HONEY_BOTTLE.builtInRegistryHolder(), Nutrition.of(0, 0, 80, 0, 0), false)
                .add(Items.BREAD.builtInRegistryHolder(), Nutrition.of(100, 0, 0, 0, 0), false)
                .add(Items.CARROT.builtInRegistryHolder(), Nutrition.of(80, 0, 0, 0, 0), false)
                .add(Items.POTATO.builtInRegistryHolder(), Nutrition.of(80, 0, 0, 0, 0), false)
                .add(Items.GOLDEN_CARROT.builtInRegistryHolder(), Nutrition.of(95, 0, 0, 0, 0), false)

                .add(MSFItems.DAWNBERRY, Nutrition.of(20, 0, 60, 60, 0), false)
                .add(MSFItems.SWEET_SPICE, Nutrition.of(0, 0, 100, 0, 0), false)
                .add(MSFItems.SALTY_SPICE, Nutrition.of(0, 100, 0, 0, 0), false)
                .add(MSFItems.GLOOMBERRY, Nutrition.of(20, 0, 0, 60, 70), false)
                .add(MSFItems.FIERY_SPICE, Nutrition.of(0, 0, 0, 0, 100), false)
                .add(MSFItems.SOUR_SPICE, Nutrition.of(0, 0, 0, 100, 0), false)

                .add(farmersDelight("cabbage"), Nutrition.of(50, 10, 0, 10, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("tomato"), Nutrition.of(30, 0, 40, 20, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("onion"), Nutrition.of(10, 0, 0, 40, 40), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("fried_egg"), Nutrition.of(50, 30, 0, 0, 20), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("apple_cider"), Nutrition.of(10, 0, 20, 40, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("tomato_sauce"), Nutrition.of(20, 15, 25, 20, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("wheat_dough"), Nutrition.of(70, 0, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("raw_pasta"), Nutrition.of(60, 0, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("pumpkin_slice"), Nutrition.of(40, 0, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cabbage_leaf"), Nutrition.of(30, 0, 0, 5, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("minced_beef"), Nutrition.of(60, 60, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("beef_patty"), Nutrition.of(64, 30, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("chicken_cuts"), Nutrition.of(80, 20, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cooked_chicken_cuts"), Nutrition.of(85, 30, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("bacon"), Nutrition.of(30, 50, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cooked_bacon"), Nutrition.of(25, 60, 15, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cod_slice"), Nutrition.of(40, 20, 0, 5, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cooked_cod_slice"), Nutrition.of(50, 25, 0, 10, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("salmon_slice"), Nutrition.of(40, 20, 0, 0, 20), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cooked_salmon_slice"), Nutrition.of(50, 25, 0, 0, 40), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("mutton_chops"), Nutrition.of(50, 40, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cooked_mutton_chops"), Nutrition.of(60, 50, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("ham"), Nutrition.of(50, 55, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("smoked_ham"), Nutrition.of(45, 65, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("pie_crust"), Nutrition.of(30, 5, 20, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cake_slice"), Nutrition.of(20, 0, 30, 10, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("apple_pie_slice"), Nutrition.of(25, 0, 20, 25, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("sweet_berry_cheesecake_slice"), Nutrition.of(10, 0, 70, 5, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("chocolate_pie_slice"), Nutrition.of(5, 0, 90, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("sweet_berry_cookie"), Nutrition.of(5, 0, 80, 10, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("honey_cookie"), Nutrition.of(0, 0, 100, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("melon_popsicle"), Nutrition.of(0, 0, 50, 20, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("glow_berry_custard"), Nutrition.of(25, 0, 20, 50, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("barbecue_stick"), Nutrition.of(30, 0, 0, 0, 50), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("egg_sandwich"), Nutrition.of(70, 20, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("chicken_sandwich"), Nutrition.of(80, 15, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("hamburger"), Nutrition.of(90, 40, 10, 15, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("bacon_sandwich"), Nutrition.of(60, 45, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("mutton_wrap"), Nutrition.of(70, 30, 0, 20, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("dumplings"), Nutrition.of(30, 20, 10, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("stuffed_potato"), Nutrition.of(65, 25, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cabbage_rolls"), Nutrition.of(30, 10, 10, 25, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("salmon_roll"), Nutrition.of(25, 10, 0, 0, 10), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("cod_roll"), Nutrition.of(25, 15, 0, 10, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("kelp_roll"), Nutrition.of(45, 20, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("kelp_roll_slice"), Nutrition.of(20, 10, 0, 0, 0), false, FARMERS_DELIGHT_LOADED)
                .add(farmersDelight("dog_food"), Nutrition.of(1, 3, 7, 9, 6), false, FARMERS_DELIGHT_LOADED);
    }

    public static @NotNull Identifier holder(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private static @NotNull Identifier farmersDelight(String path) {
        return Identifier.fromNamespaceAndPath(FARMERS_DELIGHT, path);
    }
}
