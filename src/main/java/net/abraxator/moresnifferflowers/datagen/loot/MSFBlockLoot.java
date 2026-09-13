package net.abraxator.moresnifferflowers.datagen.loot;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.BonmeeliaBlock;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.nikdo53.tinymultiblocklib.block.BaseMultiblock;

import java.util.Set;
import java.util.stream.Collectors;

public class MSFBlockLoot extends BlockLootSubProvider {
    public MSFBlockLoot(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(MSFBlocks.DAWNBERRY_VINE.get(), noDrop());
        dropSelf(MSFBlocks.GLOOMBERRY_VINE.get());

        add(MSFBlocks.AMBER_BLOCK.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(hasSilkTouch())
                        .add(LootItem.lootTableItem(MSFBlocks.AMBER_BLOCK.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(doesNotHaveSilkTouch())
                        .add(LootItem.lootTableItem(MSFItems.AMBER_SHARD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.5F, 2))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(doesNotHaveSilkTouch())
                        //COMMON
                        .add(LootItem.lootTableItem(Items.COAL).setWeight(100))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(100))
                        .add(LootItem.lootTableItem(Items.STICK).setWeight(100))
                        .add(LootItem.lootTableItem(MSFItems.AMBER_SHARD.get()).setWeight(100))
                        .add(LootItem.lootTableItem(MSFItems.DRAGONFLY.get()).setWeight(100))
                        //UNCOMMON
                        .add(LootItem.lootTableItem(Items.CARROT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.POTATO).setWeight(50))
                        .add(LootItem.lootTableItem(Items.BEETROOT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(50))
                        .add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(50))
                        .add(LootItem.lootTableItem(Items.WHEAT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(50))
                        //RARE
                        .add(LootItem.lootTableItem(MSFItems.DISC_FRAGMENT_BOBLING.get()).setWeight(25))
                        .add(LootItem.lootTableItem(Items.SNORT_POTTERY_SHERD).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.BELT_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.ENGINE_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.SCRAP_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.TUBE_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.PRESS_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.AMBUSH_BANNER_PATTERN.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.EXTRACTION_BOTTLE.get()).setWeight(25))
                        //VERY RARE
                        .add(LootItem.lootTableItem(Items.TORCHFLOWER_SEEDS).setWeight(12))
                        .add(LootItem.lootTableItem(Items.PITCHER_POD).setWeight(12))
                        .add(LootItem.lootTableItem(Items.SNIFFER_EGG).setWeight(12))
                        .add(LootItem.lootTableItem(MSFItems.DAWNBERRY_VINE_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(MSFItems.AMBUSH_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(MSFItems.DYESPRIA_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(MSFItems.BONDRIPIA_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(MSFBlocks.VIVICUS_SAPLING.get()).setWeight(12))
                        .add(LootItem.lootTableItem(MSFItems.BONMEELIA_SEEDS.get()).setWeight(12))));

        add(MSFBlocks.GARNET_BLOCK.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(hasSilkTouch())
                        .add(LootItem.lootTableItem(MSFBlocks.GARNET_BLOCK.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(doesNotHaveSilkTouch())
                        .add(LootItem.lootTableItem(MSFItems.GARNET_SHARD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.5F, 2))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(doesNotHaveSilkTouch())

                        //COMMON
                        .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(100))
                        .add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(100))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(100))
                        .add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(100))
                        .add(LootItem.lootTableItem(MSFItems.GARNET_SHARD.get()).setWeight(100))
                        .add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(100))
                        //UNCOMMON

                        .add(LootItem.lootTableItem(MSFItems.DISC_FRAGMENT_BOBLING.get()).setWeight(50))
                        .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(50))
                        .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(50))

                        //RARE
                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(25))
                        .add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING).setWeight(25))
                        .add(LootItem.lootTableItem(MSFBlocks.CORRUPTED_SAPLING.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(MSFItems.EVIL_BANNER_PATTERN.get()).setWeight(25))
                        .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP).setWeight(25))

                        //VERY RARE
                        .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(12))
                        .add(LootItem.lootTableItem(Items.IRON_BLOCK).setWeight(12))
                        .add(LootItem.lootTableItem(Items.SNIFFER_EGG).setWeight(12))
                        .add(LootItem.lootTableItem(Items.WITHER_SKELETON_SKULL).setWeight(12))
                        .add(LootItem.lootTableItem(Items.NETHERITE_INGOT).setWeight(12))
                        .add(LootItem.lootTableItem(Items.DIAMOND_BLOCK).setWeight(12))
                        .add(LootItem.lootTableItem(Items.HEAVY_CORE).setWeight(12))));


        dropWhenSilkTouch(MSFBlocks.CRACKED_AMBER.get());
        dropWhenSilkTouch(MSFBlocks.CHISELED_AMBER.get());
        dropWhenSilkTouch(MSFBlocks.CHISELED_AMBER_SLAB.get());
        dropWhenSilkTouch(MSFBlocks.AMBER_MOSAIC.get());
        dropWhenSilkTouch(MSFBlocks.AMBER_MOSAIC_SLAB.get());
        dropWhenSilkTouch(MSFBlocks.AMBER_MOSAIC_STAIRS.get());
        dropWhenSilkTouch(MSFBlocks.AMBER_MOSAIC_WALL.get());
        dropWhenSilkTouch(MSFBlocks.CRACKED_GARNET.get());
        dropWhenSilkTouch(MSFBlocks.CHISELED_GARNET.get());
        dropWhenSilkTouch(MSFBlocks.CHISELED_GARNET_SLAB.get());
        dropWhenSilkTouch(MSFBlocks.GARNET_MOSAIC.get());
        dropWhenSilkTouch(MSFBlocks.GARNET_MOSAIC_SLAB.get());
        dropWhenSilkTouch(MSFBlocks.GARNET_MOSAIC_STAIRS.get());
        dropWhenSilkTouch(MSFBlocks.GARNET_MOSAIC_WALL.get());

        add(MSFBlocks.AMBUSH_TOP.get(), noDrop());
        dropSelf(MSFBlocks.AMBUSH_BOTTOM.get());
        add(MSFBlocks.GARBUSH_TOP.get(), noDrop());
        dropSelf(MSFBlocks.GARBUSH_BOTTOM.get());

        add(MSFBlocks.CAULORFLOWER.get(), noDrop());
        add(MSFBlocks.PATTERNFLOWER.get(), noDrop());

        add(MSFBlocks.GIANT_CARROT.get(), giantCropLoot(Items.CARROT, MSFItems.CROPRESSED_CARROT.get(), Items.AIR, MSFItems.BELT_PIECE.get(), MSFItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(MSFBlocks.GIANT_POTATO.get(), giantCropLoot(Items.POTATO, MSFItems.CROPRESSED_POTATO.get(), Items.AIR, MSFItems.TUBE_PIECE.get(), MSFItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(MSFBlocks.GIANT_NETHERWART.get(), giantCropLoot(Items.NETHER_WART, MSFItems.CROPRESSED_NETHERWART.get(), MSFItems.BROKEN_REBREWING_STAND.get(), MSFItems.PRESS_PIECE.get(), MSFItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(MSFBlocks.GIANT_BEETROOT.get(), giantCropLoot(Items.BEETROOT, MSFItems.CROPRESSED_BEETROOT.get(), MSFItems.FLAVORFUL_ROOTS.get(), MSFItems.ENGINE_PIECE.get(), MSFItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(MSFBlocks.GIANT_WHEAT.get(), giantCropLoot(Items.WHEAT, MSFItems.CROPRESSED_WHEAT.get(), Items.AIR, MSFItems.SCRAP_PIECE.get(), MSFItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get()));

        add(MSFBlocks.BONMEELIA.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(MSFItems.BONMEELIA_SEEDS.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(MSFItems.JAR_OF_BONMEEL.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MSFBlocks.BONMEELIA.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BonmeeliaBlock.AGE, BonmeeliaBlock.MAX_AGE)
                                        .hasProperty(BonmeeliaBlock.HAS_BOTTLE, true))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MSFBlocks.BONMEELIA.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BonmeeliaBlock.AGE, BonmeeliaBlock.MAX_AGE))
                                .invert()
                                .and(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MSFBlocks.BONMEELIA.get())
                                        .setProperties((StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(BonmeeliaBlock.HAS_BOTTLE, true)))))));

        add(MSFBlocks.BONWILTIA.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(MSFItems.BONWILTIA_SEEDS.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(MSFItems.JAR_OF_ACID.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MSFBlocks.BONWILTIA.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BonmeeliaBlock.AGE, BonmeeliaBlock.MAX_AGE)
                                        .hasProperty(BonmeeliaBlock.HAS_BOTTLE, true))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MSFBlocks.BONWILTIA.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BonmeeliaBlock.AGE, BonmeeliaBlock.MAX_AGE))
                                .invert()
                                .and(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MSFBlocks.BONWILTIA.get())
                                        .setProperties((StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(BonmeeliaBlock.HAS_BOTTLE, true)))))));

        dropSelf(MSFBlocks.CROPRESSOR_OUT.get());
        dropSelf(MSFBlocks.CROPRESSOR_CENTER.get());
        dropSelf(MSFBlocks.REBREWING_STAND_BOTTOM.get());
        add(MSFBlocks.REBREWING_STAND_TOP.get(), noDrop());
        add(MSFBlocks.DYESPRIA_PLANT.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(MSFItems.DYESPRIA_SEEDS.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MSFBlocks.DYESPRIA_PLANT.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(MSFStateProperties.AGE_3, 3))
                                .invert())));
        dropSelf(MSFBlocks.DYESCRAPIA_PLANT.get());

        dropSelf(MSFBlocks.CORRUPTED_LOG.get());
        dropSelf(MSFBlocks.CORRUPTED_WOOD.get());
        dropSelf(MSFBlocks.STRIPPED_CORRUPTED_LOG.get());
        dropSelf(MSFBlocks.STRIPPED_CORRUPTED_WOOD.get());
        dropSelf(MSFBlocks.CORRUPTED_PLANKS.get());
        dropSelf(MSFBlocks.CORRUPTED_STAIRS.get());
        add(MSFBlocks.CORRUPTED_SLAB.get(), this::createSlabItemTable);
        dropSelf(MSFBlocks.CORRUPTED_FENCE.get());
        dropSelf(MSFBlocks.CORRUPTED_FENCE_GATE.get());
        add(MSFBlocks.CORRUPTED_DOOR.get(), this::createDoorTable);
        dropSelf(MSFBlocks.CORRUPTED_TRAPDOOR.get());
        dropSelf(MSFBlocks.CORRUPTED_PRESSURE_PLATE.get());
        dropSelf(MSFBlocks.CORRUPTED_BUTTON.get());
        add(MSFBlocks.CORRUPTED_LEAVES.get(), block -> createLeavesDrops(block, Blocks.DEAD_BUSH, 0.05F, 0.0625F, 0.083333336F, 0.1F));
        dropWhenSilkTouch(MSFBlocks.CORRUPTED_LEAVES_BUSH.get());

        dropSelf(MSFBlocks.CORRUPTED_SAPLING.get());
        add(MSFBlocks.CORRUPTED_SLUDGE.get(), block -> createSilkTouchDispatchTable(
                block, this.applyExplosionCondition(
                        block, LootItem.lootTableItem(MSFItems.CORRUPTED_SLIME_BALL)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 12.0F)))
                )
        ));

        add(MSFBlocks.DECAYED_LOG.get(), block -> createSilkTouchDispatchTable(
                block, this.applyExplosionCondition(
                        block, LootItem.lootTableItem(Items.STICK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                )
        ));
        add(MSFBlocks.CORRUPTED_GRASS_BLOCK.get(), block -> this.createSingleItemTableWithSilkTouch(block, Blocks.COARSE_DIRT));
        add(MSFBlocks.CORRUPTED_TALL_GRASS.get(), block -> this.createDoublePlantWithSeedDrops(block, MSFBlocks.CORRUPTED_GRASS.get()));
        add(MSFBlocks.CORRUPTED_GRASS.get(), block -> createShearsDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(MSFItems.CORRUPTED_SLIME_BALL.get()).when(LootItemRandomChanceCondition.randomChance(0.125F)).apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 2)))));
        add(MSFBlocks.CURED_GRASS_BLOCK.get(), block -> this.createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
        add(MSFBlocks.CORRUPTED_WART.get(), block -> this.createSingleItemTableWithSilkTouch(block, MSFItems.CORRUPTED_SLIME_BALL.get(), UniformGenerator.between(0F, 1F)));

        dropSelf(MSFBlocks.VIVICUS_LOG.get());
        dropSelf(MSFBlocks.VIVICUS_WOOD.get());
        dropSelf(MSFBlocks.STRIPPED_VIVICUS_LOG.get());
        dropSelf(MSFBlocks.STRIPPED_VIVICUS_WOOD.get());
        dropSelf(MSFBlocks.VIVICUS_PLANKS.get());
        dropSelf(MSFBlocks.VIVICUS_STAIRS.get());
        add(MSFBlocks.VIVICUS_SLAB.get(), this::createSlabItemTable);
        dropSelf(MSFBlocks.VIVICUS_FENCE.get());
        dropSelf(MSFBlocks.VIVICUS_FENCE_GATE.get());
        add(MSFBlocks.VIVICUS_DOOR.get(), this::createDoorTable);
        dropSelf(MSFBlocks.VIVICUS_TRAPDOOR.get());
        dropSelf(MSFBlocks.VIVICUS_PRESSURE_PLATE.get());
        dropSelf(MSFBlocks.VIVICUS_BUTTON.get());
        add(MSFBlocks.VIVICUS_LEAVES.get(), block -> createLeavesDrops(block, Blocks.DEAD_BUSH, 0.05F, 0.0625F, 0.083333336F, 0.1F));
        dropSelf(MSFBlocks.VIVICUS_SAPLING.get());
        add(MSFBlocks.VIVICUS_LEAVES_SPROUT.get(), this::createShearsOnlyDrop);

        add(MSFBlocks.CORRUPTED_SLIME_LAYER.get(),
                block -> LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                        .when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS))
                                        .add(AlternativesEntry.alternatives(
                                                SnowLayerBlock.LAYERS.getPossibleValues(),
                                                p_252097_ -> LootItem.lootTableItem(MSFItems.CORRUPTED_SLIME_BALL.get()).when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                .setProperties(
                                                                        StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, p_252097_)
                                                                )).apply(SetItemCountFunction.setCount(UniformGenerator.between(0F,(float) p_252097_)))
                                                )

                                        )
                        )
        );

        dropPottedContents(MSFBlocks.POTTED_DYESPRIA.get());
        dropPottedContents(MSFBlocks.POTTED_CORRUPTED_SAPLING.get());
        dropPottedContents(MSFBlocks.POTTED_VIVICUS_SAPLING.get());
        
        dropOther(MSFBlocks.CORRUPTED_SIGN.get(), MSFItems.CORRUPTED_SIGN);
        dropOther(MSFBlocks.CORRUPTED_WALL_SIGN.get(), MSFItems.CORRUPTED_SIGN);
        dropOther(MSFBlocks.CORRUPTED_HANGING_SIGN.get(), MSFItems.CORRUPTED_HANGING_SIGN);
        dropOther(MSFBlocks.CORRUPTED_WALL_HANGING_SIGN.get(), MSFItems.CORRUPTED_HANGING_SIGN);
        
        dropOther(MSFBlocks.VIVICUS_SIGN.get(), MSFItems.VIVICUS_SIGN);
        dropOther(MSFBlocks.VIVICUS_WALL_SIGN.get(), MSFItems.VIVICUS_SIGN);
        dropOther(MSFBlocks.VIVICUS_HANGING_SIGN.get(), MSFItems.VIVICUS_HANGING_SIGN);
        dropOther(MSFBlocks.VIVICUS_WALL_HANGING_SIGN.get(), MSFItems.VIVICUS_HANGING_SIGN);

        add(MSFBlocks.BONMEEL_FILLED_CAULDRON.get(), noDrop());
        add(MSFBlocks.ACID_FILLED_CAULDRON.get(), noDrop());

        add(MSFBlocks.BONDRIPIA.get(), simpleConditional(BaseMultiblock.CENTER, MSFBlocks.BONDRIPIA.get(), MSFItems.BONDRIPIA_SEEDS.get()));
        add(MSFBlocks.ACIDRIPIA.get(), simpleConditional(BaseMultiblock.CENTER, MSFBlocks.ACIDRIPIA.get(), MSFItems.ACIDRIPIA_SEEDS.get()));
        add(MSFBlocks.BEROOT_CAULDRON.get(), simpleConditional(BaseMultiblock.CENTER, MSFBlocks.BEROOT_CAULDRON.get(), MSFBlocks.BEROOT_CAULDRON.get().asItem()));
        add(MSFBlocks.SALTEMONE.get(), simpleConditional(BaseMultiblock.CENTER, MSFBlocks.SALTEMONE.get(), MSFItems.SALTEMONE_SEEDS.get()));
        add(MSFBlocks.SOURLEMONE.get(), simpleConditional(BaseMultiblock.CENTER, MSFBlocks.SOURLEMONE.get(), MSFItems.SOURLEMONE_SEEDS.get()));

        add(MSFBlocks.SALTY_CLUMP.get(), simpleIncreasingConditional(MSFStateProperties.AMOUNT_4, MSFBlocks.SALTY_CLUMP.get(), MSFItems.SALTY_SPICE.get()));
        add(MSFBlocks.SOUR_PUDDLE.get(), createSingleItemTable(MSFItems.SOUR_SPICE.get()));


        add(MSFBlocks.DRIPSALT.get(), createSingleItemTable(MSFBlocks.DRIPSALT.get().asItem()));

        dropSelf(MSFBlocks.TORCHFLOWER_AFLAME.get());
        dropSelf(MSFBlocks.TORCHFLAME.get());
        dropSelf(MSFBlocks.TORCHEWFLOWER.get());
    }

    private LootTable.Builder simpleIncreasingConditional(Property<Integer> property, Block block, Item item){
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS))
                        .add(AlternativesEntry.alternatives(
                                property.getPossibleValues(),
                                        integer -> LootItem.lootTableItem(item)
                                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, integer)))
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(integer))
                                )

                        ))
                );
    }


    private LootTable.Builder simpleConditional(Property<Boolean> property, Block block, Item item){
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(item)
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(property, true)))));
    }

    private LootTable.Builder giantCropLoot(Item crop, Item cropressed, Item special, Item piece, Item trim) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 4))
                        .add(LootItem.lootTableItem(crop)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.05F))
                        .add(LootItem.lootTableItem(piece).setWeight(50))
                        .add(LootItem.lootTableItem(trim).setWeight(22))
                        .add(LootItem.lootTableItem(cropressed).setWeight(100))
                        .add(LootItem.lootTableItem(special).setWeight(50)));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(MoreSnifferFlowers.MOD_ID))
                .filter(block -> {
                    boolean isCompat = block.equals(MSFBlocks.GIANT_CABBAGE.get())
                            || block.equals(MSFBlocks.GIANT_ONION.get())
                            || block.equals(MSFBlocks.GIANT_TOMATO.get())
                            || block.equals(MSFBlocks.GIANT_RICE.get())

                            ;
                    return !isCompat;
                })
                .collect(Collectors.toSet());
    }
}
