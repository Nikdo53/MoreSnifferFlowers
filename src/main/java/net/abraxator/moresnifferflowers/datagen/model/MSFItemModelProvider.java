package net.abraxator.moresnifferflowers.datagen.model;

import com.google.common.collect.ImmutableMap;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.abraxator.moresnifferflowers.init.MSFItems.*;

public class MSFItemModelProvider extends ItemModelProvider {
    public MSFItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MoreSnifferFlowers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        spawnEggItem(BOBLING_SPAWN_EGG);
        basicItems(BOBLING_CORE, CORRUPTED_BOBLING_CORE);

        MSFBlockFamilies.getAllFamilies().forEach(family -> {
            this.simpleBlockItem(BuiltInRegistries.BLOCK.getKey(family.getBaseBlock()));
            family.getVariants().forEach((variant, block) -> {
                if (variant == BlockFamily.Variant.WALL_SIGN) return;

                BiFunction<BlockFamily, Identifier, ItemModelBuilder> function = FAMILLY_MAP.get(variant);
                if (variant == BlockFamily.Variant.WALL || variant == BlockFamily.Variant.FENCE || variant == BlockFamily.Variant.BUTTON) {
                 block = family.getBaseBlock();
                }
                if (function != null) {
                    function.apply(family, BuiltInRegistries.BLOCK.getKey(block));
                } else {
                    this.simpleBlockItem(BuiltInRegistries.BLOCK.getKey(block));
                }
            });
        });

        basicItems(CORRUPTED_BOAT, CORRUPTED_CHEST_BOAT, VIVICUS_BOAT, VIVICUS_CHEST_BOAT, MSFBlocks.CORRUPTED_HANGING_SIGN, MSFBlocks.VIVICUS_HANGING_SIGN);

        basicItems(CORRUPTED_SLIME_BALL, VIVICUS_ANTIDOTE);
        blockItems(MSFBlocks.DECAYED_LOG, MSFBlocks.CURED_GRASS_BLOCK, MSFBlocks.CORRUPTED_GRASS_BLOCK, MSFBlocks.CORRUPTED_WART);
        flatBlockItem(MSFBlocks.CORRUPTED_GRASS);
        flatBlockItem(MSFBlocks.CORRUPTED_TALL_GRASS, "corrupted_tall_grass_top");
        suffixBlockItem(MSFBlocks.CORRUPTED_SLUDGE, "stage_1");

        withExistingParent(MSFBlocks.CORRUPTED_SLIME_LAYER.getRegisteredName(), modLoc("block/corrupted_slime_height2"));

        basicItems(SALTEMONE_SEEDS, PATTERNFLOWER_SEEDS, SOURLEMONE_SEEDS, BONMEELIA_SEEDS, ACIDRIPIA_SEEDS, AMBUSH_SEEDS, BONDRIPIA_SEEDS, BONWILTIA_SEEDS, CAULORFLOWER_SEEDS, DAWNBERRY_VINE_SEEDS, DYESPRIA_SEEDS, GARBUSH_SEEDS, GLOOMBERRY_VINE_SEEDS);
        basicItems(DAWNBERRY, GLOOMBERRY, JAR_OF_ACID, JAR_OF_BONMEEL, DYESCRAPIA);
        basicItems(AMBER_SHARD, DRAGONFLY, GARNET_SHARD);
        basicItems(MSFBlocks.DRIPSALT);

        legacyBanner(AMBUSH_BANNER_PATTERN);
        legacyBanner(EVIL_BANNER_PATTERN);

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

    public ItemModelBuilder withVanillaParent(Holder<?> name, String parent) {
        return withExistingParent(name.getRegisteredName(), mcLoc(parent));
    }

    public ItemModelBuilder withMSFParent(Holder<?> name, String parent) {
        return withExistingParent(name.getRegisteredName(), loc(parent));
    }


    public Identifier loc(String path) {
        return MoreSnifferFlowers.loc(path);
    }


    //item exclusive
    public ItemModelBuilder simpleBlockItem(Supplier<Block> block) {
        return simpleBlockItem(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())));
    }

    @SafeVarargs
    public final void blockItems(Supplier<? extends Block>... blocks) {
        for (Supplier<? extends Block> block : blocks) {
            simpleBlockItem(block.get());
        }
    }

    public ItemModelBuilder flatBlockItem(ItemLike item) {
        Identifier loc = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem()));
        return getBuilder(loc.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", Identifier.fromNamespaceAndPath(loc.getNamespace(), "block/" + loc.getPath()));

    }

    public ItemModelBuilder flatBlockItem(ItemLike item, String texture) {
        Identifier loc = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem()));
        return getBuilder(loc.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", Identifier.fromNamespaceAndPath(loc.getNamespace(), "block/" + texture));

    }


    public ItemModelBuilder basicItem(ItemLike item) {
        return basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())));
    }

    public void basicItems(ItemLike... items) {
        for (ItemLike item : items) {
            basicItem(item);
        }
    }

    public ItemModelBuilder handheldItem(ItemLike item) {
        return handheldItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())));
    }

    public ItemModelBuilder spawnEggItem(ItemLike item) {
        return spawnEggItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())));
    }

    public ItemModelBuilder legacyBanner(Holder<Item> item) {
       return withExistingParent(item.getRegisteredName(), "item/generated")
                .texture("layer0", Identifier.withDefaultNamespace("item/globe_banner_pattern"));
    }

    public ItemModelBuilder suffixBlockItem(Identifier block, String suffix) {
        return withExistingParent(block.toString(), Identifier.fromNamespaceAndPath(block.getNamespace(), "block/" + block.getPath() + "_" + suffix));
    }

    public ItemModelBuilder suffixBlockItem(Supplier<Block> blockSupplier, String suffix) {
        return suffixBlockItem(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(blockSupplier.get())), suffix);
    }




    final Map<BlockFamily.Variant, BiFunction<BlockFamily, Identifier, ItemModelBuilder>> FAMILLY_MAP = ImmutableMap.<BlockFamily.Variant, BiFunction<BlockFamily, Identifier, ItemModelBuilder>>builder()
            .put(BlockFamily.Variant.BUTTON,(f,r) -> buttonInventory(f.get(BlockFamily.Variant.BUTTON).builtInRegistryHolder().getRegisteredName(), r.withPrefix("block/")))
            .put(BlockFamily.Variant.DOOR, (f,r) -> basicItem(r))
            .put(BlockFamily.Variant.CHISELED, (f,r) -> simpleBlockItem(r))
            .put(BlockFamily.Variant.CRACKED, (f,r) -> simpleBlockItem(r))
            .put(BlockFamily.Variant.FENCE,  (f,r) -> fenceInventory(f.get(BlockFamily.Variant.FENCE).builtInRegistryHolder().getRegisteredName(), r.withPrefix("block/")))
            .put(BlockFamily.Variant.FENCE_GATE, (f,r) -> simpleBlockItem(r))
            .put(BlockFamily.Variant.SIGN, (f,r) -> basicItem(r))
            .put(BlockFamily.Variant.SLAB, (f,r) -> simpleBlockItem(r))
            .put(BlockFamily.Variant.STAIRS, (f,r) -> simpleBlockItem(r))
            .put(BlockFamily.Variant.PRESSURE_PLATE, (f,r) -> simpleBlockItem(r))
            .put(BlockFamily.Variant.TRAPDOOR,  (f,r) -> suffixBlockItem(r, "bottom"))
            .put(BlockFamily.Variant.WALL, (f,r) -> wallInventory(f.get(BlockFamily.Variant.WALL).builtInRegistryHolder().getRegisteredName(), r.withPrefix("block/")))
            .build();


}
