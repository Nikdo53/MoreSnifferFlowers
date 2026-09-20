package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.items.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface MSFItems {
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreSnifferFlowers.MOD_ID);

    DeferredItem<Item> DAWNBERRY_VINE_SEEDS = registerBlockItem("dawnberry_vine_seeds", MSFBlocks.DAWNBERRY_VINE);
    DeferredItem<Item> GLOOMBERRY_VINE_SEEDS = registerBlockItem("gloomberry_vine_seeds", MSFBlocks.GLOOMBERRY_VINE);
    DeferredItem<Item> DAWNBERRY = register("dawnberry", Item::new, (p) -> p.food(Food.DAWNBERRY, Consumables.DAWNBERRY));
    DeferredItem<Item> GLOOMBERRY = register("gloomberry", Item::new, (p) -> p.food(Food.GLOOMBERRY, Consumables.GLOOMBERRY));

    DeferredItem<Item> AMBUSH_SEEDS = registerBlockItem("ambush_seeds", MSFBlocks.AMBUSH_BOTTOM);
    DeferredItem<Item> GARBUSH_SEEDS = registerBlockItem("garbush_seeds", MSFBlocks.GARBUSH_BOTTOM);

    DeferredItem<Item> AMBUSH_BANNER_PATTERN = register("ambush_banner_pattern", properties -> new BannerPatternItem(MSFTags.BannerTags.AMBUSH_BANNER_PATTERN, properties), (p) -> p.stacksTo(1));
    DeferredItem<Item> EVIL_BANNER_PATTERN = register("evil_banner_pattern", properties -> new BannerPatternItem(MSFTags.BannerTags.EVIL_BANNER_PATTERN, properties), (p) -> p.stacksTo(1));

    DeferredItem<Item> AMBER_SHARD = register("amber_shard", TrimMaterialItem::new);
    DeferredItem<Item> GARNET_SHARD = register("garnet_shard", TrimMaterialItem::new);

    DeferredItem<Item> AROMA_ARMOR_TRIM_SMITHING_TEMPLATE = register("aroma_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate);
    DeferredItem<Item> CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE = register("carnage_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate);
    DeferredItem<Item> DRAGONFLY = register("dragonfly", DragonflyItem::new);
    DeferredItem<Item> DYESPRIA = register("dyespria", DyespriaItem::new, (p) -> p.stacksTo(1));
    DeferredItem<Item> DYESCRAPIA = register("dyescrapia", DyescrapiaItem::new, (p) -> p.stacksTo(1));
    DeferredItem<Item> DYESPRIA_SEEDS = registerBlockItem("dyespria_seeds", MSFBlocks.DYESPRIA_PLANT, Component.translatableWithFallback("tooltip.dyespria_seeds", "Shear to hide dye").withStyle(ChatFormatting.GOLD));

    DeferredItem<Item> BONMEELIA_SEEDS = registerBlockItem("bonmeelia_seeds", MSFBlocks.BONMEELIA);
    DeferredItem<Item> JAR_OF_BONMEEL = register("jar_of_bonmeel", JarOfBonmeelItem::new);
    DeferredItem<Item> BONDRIPIA_SEEDS = registerBlockItem("bondripia_seeds", MSFBlocks.BONDRIPIA, Component.translatable("tooltip.bondripia_seeds").withStyle(ChatFormatting.GOLD));

    DeferredItem<Item> BONWILTIA_SEEDS = registerBlockItem("bonwiltia_seeds", MSFBlocks.BONWILTIA);
    DeferredItem<Item> JAR_OF_ACID = register("jar_of_acid", JarOfAcidItem::new);
    DeferredItem<Item> ACIDRIPIA_SEEDS = registerBlockItem("acidripia_seeds", MSFBlocks.ACIDRIPIA);

    DeferredItem<Item> CROPRESSOR = registerBlockItem("cropressor", MSFBlocks.CROPRESSOR_OUT);
    DeferredItem<Item> TUBE_PIECE = register("tube_piece", Item::new);
    DeferredItem<Item> BELT_PIECE = register("belt_piece", Item::new);
    DeferredItem<Item> SCRAP_PIECE = register("scrap_piece", Item::new);
    DeferredItem<Item> ENGINE_PIECE = register("engine_piece", Item::new);
    DeferredItem<Item> PRESS_PIECE = register("press_piece", Item::new);

    DeferredItem<Item> REBREWING_STAND = registerBlockItem("rebrewing_stand", MSFBlocks.REBREWING_STAND_BOTTOM);
    DeferredItem<Item> BROKEN_REBREWING_STAND = register("broken_rebrewing_stand", Item::new);
    DeferredItem<Item> EXTRACTION_BOTTLE = register("extraction_bottle", BottleOfExtractionItem::new, (p) -> p.stacksTo(1));
    DeferredItem<Item> EXTRACTED_BOTTLE = register("extracted_bottle", ExtractedBottleItem::new, (p) -> p.stacksTo(1));
    DeferredItem<Item> REBREWED_POTION = register("rebrewed_potion", PotionItem::new, (p) -> p.stacksTo(1));
    DeferredItem<Item> REBREWED_SPLASH_POTION = register("rebrewed_splash_potion", SplashPotionItem::new, (p) -> p.stacksTo(1));
    DeferredItem<Item> REBREWED_LINGERING_POTION = register("rebrewed_lingering_potion", LingeringPotionItem::new, (p) -> p.stacksTo(1));

    DeferredItem<Item> CROPRESSED_POTATO = register("cropressed_potato", TrimMaterialItem::new);
    DeferredItem<Item> CROPRESSED_CARROT = register("cropressed_carrot", TrimMaterialItem::new);
    DeferredItem<Item> CROPRESSED_BEETROOT = register("cropressed_beetroot", TrimMaterialItem::new);
    DeferredItem<Item> CROPRESSED_NETHERWART = register("cropressed_nether_wart", TrimMaterialItem::new);
    DeferredItem<Item> CROPRESSED_WHEAT = register("cropressed_wheat", TrimMaterialItem::new);

    DeferredItem<Item> TATER_ARMOR_TRIM_SMITHING_TEMPLATE = register("tater_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate);
    DeferredItem<Item> CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE = register("carotene_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate);
    DeferredItem<Item> BEAT_ARMOR_TRIM_SMITHING_TEMPLATE = register("beat_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate);
    DeferredItem<Item> NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE = register("nether_wart_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate);
    DeferredItem<Item> GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE = register("grain_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate);

    DeferredItem<Item> VIVICUS_ANTIDOTE = register("vivicus_antidote", VivicusAntidoteItem::new);
    DeferredItem<Item> CORRUPTED_BOBLING_CORE = register("corrupted_bobling_core", Item::new);
    DeferredItem<Item> BOBLING_CORE = register("bobling_core", Item::new);
    DeferredItem<Item> CORRUPTED_SLIME_BALL = register("corrupted_slime_ball", CorruptedSlimeBallItem::new);

    DeferredItem<Item> CORRUPTED_SIGN = register("corrupted_sign", properties -> new SignItem(MSFBlocks.CORRUPTED_SIGN.get(), MSFBlocks.CORRUPTED_WALL_SIGN.get(), properties), Item.Properties::useBlockDescriptionPrefix);
    DeferredItem<Item> CORRUPTED_HANGING_SIGN = register("corrupted_hanging_sign", properties -> new HangingSignItem(MSFBlocks.CORRUPTED_HANGING_SIGN.get(), MSFBlocks.CORRUPTED_WALL_HANGING_SIGN.get(), properties), Item.Properties::useBlockDescriptionPrefix);
    DeferredItem<Item> CORRUPTED_BOAT = register("corrupted_boat", properties -> new BoatItem(MSFEntityTypes.CORRUPTED_BOAT.get(), properties));
    DeferredItem<Item> CORRUPTED_CHEST_BOAT = register("corrupted_chest_boat", properties -> new BoatItem(MSFEntityTypes.CORRUPTED_BOAT.get(), properties));

    DeferredItem<Item> VIVICUS_SIGN = register("vivicus_sign", properties -> new SignItem(MSFBlocks.VIVICUS_SIGN.get(), MSFBlocks.VIVICUS_WALL_SIGN.get(), properties),Item.Properties::useBlockDescriptionPrefix);
    DeferredItem<Item> VIVICUS_HANGING_SIGN = register("vivicus_hanging_sign", properties -> new HangingSignItem(MSFBlocks.VIVICUS_HANGING_SIGN.get(), MSFBlocks.VIVICUS_WALL_HANGING_SIGN.get(), properties),Item.Properties::useBlockDescriptionPrefix);
    DeferredItem<Item> VIVICUS_BOAT = register("vivicus_boat", properties -> new BoatItem(MSFEntityTypes.VIVICUS_BOAT.get(), properties));
    DeferredItem<Item> VIVICUS_CHEST_BOAT = register("vivicus_chest_boat", properties -> new BoatItem(MSFEntityTypes.VIVICUS_CHEST_BOAT.get(), properties));

    DeferredItem<Item> BOBLING_SPAWN_EGG = register("bobling_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(MSFEntityTypes.BOBLING.get()));

    DeferredItem<Item> CAULORFLOWER_SEEDS = registerBlockItem("caulorflower_seeds", MSFBlocks.CAULORFLOWER);
    DeferredItem<Item> PATTERNFLOWER_SEEDS = registerBlockItem("patternflower_seeds", MSFBlocks.PATTERNFLOWER);
    DeferredItem<Item> PATTERNSPRIA = register("patternspria", PatternspriaItem::new, (p) -> p.stacksTo(1));

    DeferredItem<Item> BLOCK_PATTERN_PIPES = register("block_pattern_pipes", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_BRICKS = register("block_pattern_bricks", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_FOCUS = register("block_pattern_focus", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_BUBBLES = register("block_pattern_bubbles", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_CLOUDS = register("block_pattern_clouds", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_DEEPSLATE = register("block_pattern_deepslate", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_DIAMOND = register("block_pattern_diamond", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_EYE = register("block_pattern_eye", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_HEARTS = register("block_pattern_hearts", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_HONEYCOMB = register("block_pattern_honeycomb", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_PAWS = register("block_pattern_paws", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_PRISMARINE = register("block_pattern_prismarine", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_SPROUTS = register("block_pattern_sprouts", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_STARS = register("block_pattern_stars", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_COVER = register("block_pattern_cover", Item::new);
    DeferredItem<Item> BLOCK_PATTERN_FLOWERS = register("block_pattern_flowers", Item::new);

    DeferredItem<Item> ROOTED_SOUP = register("rooted_soup", RootedSoupItem::new, (p) -> p.stacksTo(1));
    DeferredItem<Item> BEROOT_COOK_BOOK = register("beroot_cook_book", BerootCookbookItem::new, (p) -> p.stacksTo(1));
    DeferredItem<Item> FLAVORFUL_ROOTS = register("flavorful_roots", Item::new);

    DeferredItem<Item> SALTEMONE_SEEDS = register("saltemone_seeds", properties -> new SaltemoneSeedsItem(MSFBlocks.SALTEMONE.get(), properties));
    DeferredItem<Item> SOURLEMONE_SEEDS = register("sourlemone_seeds", properties -> new SaltemoneSeedsItem(MSFBlocks.SOURLEMONE.get(), properties));
    DeferredItem<Item> SALTY_SPICE = register("salty_spice", properties -> new SaltySpiceItem(MSFBlocks.SALTY_CLUMP.get(), properties));
    DeferredItem<Item> SOUR_SPICE = register("sour_spice", properties -> new SourSpiceItem(MSFBlocks.SOUR_PUDDLE.get(), properties));
    DeferredItem<Item> FIERY_SPICE = register("fiery_spice", Item::new);
    DeferredItem<Item> SWEET_SPICE = register("sweet_spice", Item::new);

    DeferredItem<Item> BURNED_SLOT = register("burned_slot", BurnedSlotItem::new, (p) -> p.stacksTo(1));

    DeferredItem<Item> MUSIC_DISC_BOBLING = register("music_disc_bobling", Item::new, (p) -> p.stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(MSFSounds.MusicDiscs.BOBLING_BATTLE.getKey()));
    DeferredItem<Item> DISC_FRAGMENT_BOBLING = register("disc_fragment_bobling", Item::new);

    DeferredItem<Item> WAND_OF_CUBING = register("wand_of_cubing", WandOfCubingItem::new);
    DeferredItem<Item> DEBUG_FLOWER = register("debug_flower", DebugFlowerItem::new);

    static DeferredItem<Item> register(String name, Function<Item.Properties, Item> itemFactory, UnaryOperator<Item.Properties> properties) {
        return ITEMS.registerItem(name, itemFactory, properties);
    }

    static DeferredItem<Item> register(String name, Function<Item.Properties, Item> itemFactory) {
        return ITEMS.registerItem(name, itemFactory);
    }

    static DeferredItem<Item> registerBlockItem(String name, Supplier<Block> blockSupplier, Component... description) {
        return register(name, properties -> new DescriptionBlockItem(blockSupplier.get(), properties, description));
    }

    interface Food {
        FoodProperties DAWNBERRY = builder(4, 0.6).build();
        FoodProperties GLOOMBERRY = builder(4, 0.6).build();

        static FoodProperties.Builder builder(int nutrition, double saturationModifier) {
            return new FoodProperties.Builder().nutrition(nutrition).saturationModifier((float) saturationModifier);
        }
    }

    interface Consumables{
        Consumable DAWNBERRY = fastFood().build();
        Consumable GLOOMBERRY = fastFood()
                .onConsume(new ApplyStatusEffectsConsumeEffect(
                        List.of(new MobEffectInstance(MobEffects.NAUSEA, 100), new MobEffectInstance(MobEffects.POISON, 100))
                ))
                .build();

        static Consumable.Builder fastFood() {
            return defaultFood().consumeSeconds(0.8f);
        }

        static Consumable.Builder defaultFood() {
            return net.minecraft.world.item.component.Consumables.defaultFood();
        }

        static Consumable.Builder defaultDrink() {
            return net.minecraft.world.item.component.Consumables.defaultDrink();
        }
    }
}