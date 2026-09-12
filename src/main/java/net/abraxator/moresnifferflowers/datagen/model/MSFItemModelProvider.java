package net.abraxator.moresnifferflowers.datagen.model;

import com.google.common.collect.ImmutableMap;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static net.abraxator.moresnifferflowers.init.MSFItems.*;

public class MSFItemModelProvider extends ModelProvider {
    BlockModelGenerators blockModels;
    ItemModelGenerators itemModels;
    public MSFItemModelProvider(PackOutput packOutput) {
        super(packOutput, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.blockModels = blockModels;
        this.itemModels = itemModels;

        basicItems(BOBLING_CORE, CORRUPTED_BOBLING_CORE, BOBLING_SPAWN_EGG);

        basicItems(CORRUPTED_BOAT, CORRUPTED_CHEST_BOAT, VIVICUS_BOAT, VIVICUS_CHEST_BOAT, MSFBlocks.CORRUPTED_HANGING_SIGN, MSFBlocks.VIVICUS_HANGING_SIGN);

        basicItems(CORRUPTED_SLIME_BALL, VIVICUS_ANTIDOTE);
        blockItems(MSFBlocks.DECAYED_LOG, MSFBlocks.CURED_GRASS_BLOCK, MSFBlocks.CORRUPTED_GRASS_BLOCK, MSFBlocks.CORRUPTED_WART);
        flatBlockItem(MSFBlocks.CORRUPTED_GRASS);
        flatBlockItem(MSFBlocks.CORRUPTED_TALL_GRASS, "corrupted_tall_grass_top");
        suffixBlockItem(MSFBlocks.CORRUPTED_SLUDGE, "stage_1");

        blockModels.registerSimpleItemModel(MSFBlocks.CORRUPTED_SLIME_LAYER.get(), MoreSnifferFlowers.loc("block/corrupted_slime_height2"));

        basicItems(SALTEMONE_SEEDS, PATTERNFLOWER_SEEDS, SOURLEMONE_SEEDS, BONMEELIA_SEEDS, ACIDRIPIA_SEEDS, AMBUSH_SEEDS, BONDRIPIA_SEEDS, BONWILTIA_SEEDS, CAULORFLOWER_SEEDS, DAWNBERRY_VINE_SEEDS, DYESPRIA_SEEDS, GARBUSH_SEEDS, GLOOMBERRY_VINE_SEEDS);
        basicItems(DAWNBERRY, GLOOMBERRY, JAR_OF_ACID, JAR_OF_BONMEEL, DYESCRAPIA);
        basicItems(AMBER_SHARD, DRAGONFLY, GARNET_SHARD);
        basicItems(MSFBlocks.DRIPSALT);

        basicItems(AMBUSH_BANNER_PATTERN, EVIL_BANNER_PATTERN);

        basicItems(AROMA_ARMOR_TRIM_SMITHING_TEMPLATE, BEAT_ARMOR_TRIM_SMITHING_TEMPLATE, CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE, CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE, GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE, NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE, TATER_ARMOR_TRIM_SMITHING_TEMPLATE);
        basicItems(BELT_PIECE, ENGINE_PIECE, PRESS_PIECE, SCRAP_PIECE, TUBE_PIECE);
        basicItems(BEROOT_COOK_BOOK, MSFBlocks.BEROOT_CAULDRON, FLAVORFUL_ROOTS);
        basicItems(REBREWING_STAND, BROKEN_REBREWING_STAND, EXTRACTION_BOTTLE);

        blockItems(MSFBlocks.CORRUPTED_WOOD, MSFBlocks.CORRUPTED_LOG, MSFBlocks.STRIPPED_CORRUPTED_LOG, MSFBlocks.STRIPPED_CORRUPTED_WOOD,
                MSFBlocks.VIVICUS_WOOD, MSFBlocks.VIVICUS_LOG, MSFBlocks.STRIPPED_VIVICUS_LOG, MSFBlocks.STRIPPED_VIVICUS_WOOD,
                MSFBlocks.CORRUPTED_LEAVES, MSFBlocks.VIVICUS_LEAVES);

        basicItem(MSFBlocks.CORRUPTED_LEAVES_BUSH);

        flatBlockItem(MSFBlocks.CORRUPTED_SAPLING, "corrupted_sapling_1");
        flatBlockItem(MSFBlocks.VIVICUS_SAPLING);

        basicItems(BLOCK_PATTERN_PIPES, BLOCK_PATTERN_BRICKS, BLOCK_PATTERN_FOCUS, BLOCK_PATTERN_BUBBLES, BLOCK_PATTERN_CLOUDS, BLOCK_PATTERN_DEEPSLATE,
                BLOCK_PATTERN_DIAMOND, BLOCK_PATTERN_EYE, BLOCK_PATTERN_HEARTS, BLOCK_PATTERN_HONEYCOMB, BLOCK_PATTERN_PAWS, BLOCK_PATTERN_PRISMARINE,
                BLOCK_PATTERN_SPROUTS, BLOCK_PATTERN_STARS, BLOCK_PATTERN_COVER, BLOCK_PATTERN_FLOWERS);

        basicItems(SALTY_SPICE, SOUR_SPICE, FIERY_SPICE, SWEET_SPICE);

        basicItems(MSFBlocks.GIANT_BEETROOT, MSFBlocks.GIANT_CARROT, MSFBlocks.GIANT_NETHERWART, MSFBlocks.GIANT_POTATO, MSFBlocks.GIANT_WHEAT,
                MSFBlocks.GIANT_ONION, MSFBlocks.GIANT_TOMATO, MSFBlocks.GIANT_CABBAGE, MSFBlocks.GIANT_RICE);

        flatBlockItem(MSFBlocks.TORCHEWFLOWER);
        flatBlockItem(MSFBlocks.TORCHFLOWER_AFLAME);

        basicItems(DEBUG_FLOWER, MSFBlocks.TORCHFLAME, WAND_OF_CUBING);
        basicItems(MUSIC_DISC_BOBLING, DISC_FRAGMENT_BOBLING);
    }


    public Identifier loc(String path) {
        return MoreSnifferFlowers.loc(path);
    }

    //item exclusive
    public void simpleBlockItem(Supplier<? extends Block> block) {
        blockModels.registerSimpleItemModel(block.get(), BuiltInRegistries.BLOCK.getKey(block.get()));
    }

    @SafeVarargs
    public final void blockItems(Supplier<? extends Block>... blocks) {
        for (Supplier<? extends Block> block : blocks) {
            simpleBlockItem(block);
        }
    }

    public void flatBlockItem(DeferredBlock<Block> block) {
        blockModels.createFlatItemModelWithBlockTexture(block.asItem(), block.get());
    }

    public void flatBlockItem(DeferredBlock<Block> block, String suffix) {
        blockModels.createFlatItemModelWithBlockTexture(block.asItem(), block.get(), suffix);
    }

    public void basicItem(ItemLike item) {
        itemModels.createFlatItemModel(item.asItem(), ModelTemplates.FLAT_ITEM);
    }

    public void basicItems(ItemLike... items) {
        for (ItemLike item : items) {
            basicItem(item);
        }
    }

    public void handheldItem(ItemLike item) {
        itemModels.createFlatItemModel(item.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    public void suffixBlockItem(Supplier<Block> blockSupplier, String suffix) {
        blockModels.registerSimpleItemModel(blockSupplier.get(), BuiltInRegistries.BLOCK.getKey(blockSupplier.get()).withSuffix("_" + suffix));
    }

}
