package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface MSFCreativeTabs {
    DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), MoreSnifferFlowers.MOD_ID);

    DeferredHolder<CreativeModeTab, CreativeModeTab> MORESNIFFERFLOWERS_TAB = TABS.register("moresnifferflowers_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("moresnifferflowers.creative_tab"))
            .hideTitle()
            .icon(() -> new ItemStack(MSFItems.DYESPRIA.get()))
            .displayItems((parameters, output) -> {
                output.accept(MSFItems.DAWNBERRY_VINE_SEEDS.get());
                output.accept(MSFItems.DAWNBERRY.get());
                output.accept(MSFItems.GLOOMBERRY_VINE_SEEDS);
                output.accept(MSFItems.GLOOMBERRY);

                output.accept(MSFItems.AMBUSH_SEEDS.get());
                output.accept(MSFBlocks.AMBER_BLOCK.get());
                output.accept(MSFBlocks.AMBER_MOSAIC.get());
                output.accept(MSFBlocks.AMBER_MOSAIC_STAIRS.get());
                output.accept(MSFBlocks.AMBER_MOSAIC_SLAB.get());
                output.accept(MSFBlocks.AMBER_MOSAIC_WALL.get());
                output.accept(MSFBlocks.CHISELED_AMBER.get());
                output.accept(MSFBlocks.CHISELED_AMBER_SLAB.get());
                output.accept(MSFBlocks.CRACKED_AMBER.get());

                output.accept(MSFItems.AMBER_SHARD.get());
                output.accept(MSFItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(MSFItems.DRAGONFLY.get());
                output.accept(MSFItems.AMBUSH_BANNER_PATTERN.get());

                output.accept(MSFItems.GARBUSH_SEEDS);
                output.accept(MSFBlocks.GARNET_BLOCK.get());
                output.accept(MSFBlocks.GARNET_MOSAIC.get());
                output.accept(MSFBlocks.GARNET_MOSAIC_STAIRS.get());
                output.accept(MSFBlocks.GARNET_MOSAIC_SLAB.get());
                output.accept(MSFBlocks.GARNET_MOSAIC_WALL.get());
                output.accept(MSFBlocks.CHISELED_GARNET.get());
                output.accept(MSFBlocks.CHISELED_GARNET_SLAB.get());
                output.accept(MSFBlocks.CRACKED_GARNET.get());
                output.accept(MSFItems.GARNET_SHARD);

                output.accept(MSFItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(MSFItems.EVIL_BANNER_PATTERN.get());

                output.accept(MSFItems.DYESPRIA_SEEDS.get());
                output.accept(MSFItems.DYESPRIA.get());
                output.accept(MSFItems.DYESCRAPIA);

                output.accept(MSFBlocks.CAULORFLOWER.get());
                output.accept(MSFBlocks.PATTERNFLOWER.get());
                output.accept(MSFItems.PATTERNSPRIA.get());

                output.accept(MSFItems.BONMEELIA_SEEDS.get());
                output.accept(MSFItems.JAR_OF_BONMEEL.get());
                output.accept(MSFItems.BONDRIPIA_SEEDS);
                output.accept(MSFItems.BONWILTIA_SEEDS.get());
                output.accept(MSFItems.JAR_OF_ACID.get());
                output.accept(MSFItems.ACIDRIPIA_SEEDS);

                output.accept(MSFItems.SALTEMONE_SEEDS.get());
                output.accept(MSFItems.SALTY_SPICE.get());
                output.accept(MSFBlocks.DRIPSALT.get());
                output.accept(MSFItems.SOURLEMONE_SEEDS.get());
                output.accept(MSFBlocks.SOUR_PUDDLE.get());

                output.accept(MSFBlocks.TORCHFLOWER_AFLAME.get());
                output.accept(MSFBlocks.TORCHFLAME.get());
                output.accept(MSFItems.FIERY_SPICE.get());

                output.accept(MSFBlocks.TORCHEWFLOWER.get());
                output.accept(MSFItems.SWEET_SPICE.get());

                output.accept(MSFItems.BELT_PIECE.get());
                output.accept(MSFItems.ENGINE_PIECE.get());
                output.accept(MSFItems.TUBE_PIECE.get());
                output.accept(MSFItems.SCRAP_PIECE.get());
                output.accept(MSFItems.PRESS_PIECE.get());
                output.accept(MSFItems.CROPRESSOR.get());
                
                output.accept(MSFItems.CROPRESSED_CARROT.get());
                output.accept(MSFItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(MSFBlocks.GIANT_CARROT);
                
                output.accept(MSFItems.CROPRESSED_POTATO.get());
                output.accept(MSFItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(MSFBlocks.GIANT_POTATO);
                
                output.accept(MSFItems.CROPRESSED_WHEAT.get());
                output.accept(MSFItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(MSFBlocks.GIANT_WHEAT);
                
                output.accept(MSFItems.CROPRESSED_BEETROOT.get());
                output.accept(MSFItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(MSFBlocks.GIANT_BEETROOT.get());
                output.accept(MSFItems.FLAVORFUL_ROOTS.get());
                output.accept(MSFBlocks.BEROOT_CAULDRON.get());
                output.accept(MSFItems.BEROOT_COOK_BOOK.get());

                output.accept(MSFItems.CROPRESSED_NETHERWART.get());
                output.accept(MSFItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(MSFBlocks.GIANT_NETHERWART);

                output.accept(MSFItems.EXTRACTION_BOTTLE.get());
                output.accept(MSFItems.BROKEN_REBREWING_STAND.get());
                output.accept(MSFItems.REBREWING_STAND.get());

                if (MoreSnifferFlowers.hasFarmersDelight()){
                    output.accept(MSFBlocks.GIANT_ONION.get());
                    output.accept(MSFBlocks.GIANT_CABBAGE.get());
                    output.accept(MSFBlocks.GIANT_TOMATO.get());
                    output.accept(MSFBlocks.GIANT_RICE.get());

                }
                
                output.accept(MSFItems.BOBLING_SPAWN_EGG.get());
                output.accept(MSFItems.CORRUPTED_BOBLING_CORE.get());
                output.accept(MSFItems.CORRUPTED_SLIME_BALL.get());
                output.accept(MSFItems.BOBLING_CORE.get());
                output.accept(MSFItems.DISC_FRAGMENT_BOBLING.get());
                output.accept(MSFItems.MUSIC_DISC_BOBLING.get());
                output.accept(MSFItems.VIVICUS_ANTIDOTE.get());

                output.accept(MSFBlocks.DECAYED_LOG.get());
                output.accept(MSFBlocks.CORRUPTED_GRASS_BLOCK.get());
                output.accept(MSFBlocks.CURED_GRASS_BLOCK.get());
                output.accept(MSFBlocks.CORRUPTED_GRASS.get());
                output.accept(MSFBlocks.CORRUPTED_TALL_GRASS.get());
                output.accept(MSFBlocks.CORRUPTED_WART.get());

                output.accept(MSFBlocks.VIVICUS_LOG.get());
                output.accept(MSFBlocks.VIVICUS_WOOD.get());
                output.accept(MSFBlocks.STRIPPED_VIVICUS_LOG.get());
                output.accept(MSFBlocks.STRIPPED_VIVICUS_WOOD.get());
                output.accept(MSFBlocks.VIVICUS_PLANKS.get());
                output.accept(MSFBlocks.VIVICUS_STAIRS.get());
                output.accept(MSFBlocks.VIVICUS_SLAB.get());
                output.accept(MSFBlocks.VIVICUS_FENCE.get());
                output.accept(MSFBlocks.VIVICUS_FENCE_GATE.get());
                output.accept(MSFBlocks.VIVICUS_DOOR.get());
                output.accept(MSFBlocks.VIVICUS_TRAPDOOR.get());
                output.accept(MSFBlocks.VIVICUS_PRESSURE_PLATE.get());
                output.accept(MSFBlocks.VIVICUS_BUTTON.get());
                output.accept(MSFBlocks.VIVICUS_LEAVES.get());
                output.accept(MSFBlocks.VIVICUS_SAPLING.get());
                output.accept(MSFBlocks.VIVICUS_LEAVES_SPROUT.get());
                output.accept(MSFItems.VIVICUS_SIGN.get());
                output.accept(MSFItems.VIVICUS_HANGING_SIGN.get());
                output.accept(MSFItems.VIVICUS_BOAT);
                output.accept(MSFItems.VIVICUS_CHEST_BOAT);

                output.accept(MSFBlocks.CORRUPTED_LOG.get());
                output.accept(MSFBlocks.CORRUPTED_WOOD.get());
                output.accept(MSFBlocks.STRIPPED_CORRUPTED_LOG.get());
                output.accept(MSFBlocks.STRIPPED_CORRUPTED_WOOD.get());
                output.accept(MSFBlocks.CORRUPTED_PLANKS.get());
                output.accept(MSFBlocks.CORRUPTED_STAIRS.get());
                output.accept(MSFBlocks.CORRUPTED_SLAB.get());
                output.accept(MSFBlocks.CORRUPTED_FENCE.get());
                output.accept(MSFBlocks.CORRUPTED_FENCE_GATE.get());
                output.accept(MSFBlocks.CORRUPTED_DOOR.get());
                output.accept(MSFBlocks.CORRUPTED_TRAPDOOR.get());
                output.accept(MSFBlocks.CORRUPTED_PRESSURE_PLATE.get());
                output.accept(MSFBlocks.CORRUPTED_BUTTON.get());
                output.accept(MSFBlocks.CORRUPTED_LEAVES.get());
                output.accept(MSFBlocks.CORRUPTED_LEAVES_BUSH.get());
                output.accept(MSFBlocks.CORRUPTED_SAPLING.get());
                output.accept(MSFBlocks.CORRUPTED_SLUDGE);
                output.accept(MSFBlocks.CORRUPTED_SLIME_LAYER);
                output.accept(MSFItems.CORRUPTED_SIGN);
                output.accept(MSFItems.CORRUPTED_HANGING_SIGN);
                output.accept(MSFItems.CORRUPTED_BOAT);
                output.accept(MSFItems.CORRUPTED_CHEST_BOAT);

                output.accept(MSFItems.BLOCK_PATTERN_CLOUDS.get());
                output.accept(MSFItems.BLOCK_PATTERN_EYE.get());
                output.accept(MSFItems.BLOCK_PATTERN_COVER.get());
                output.accept(MSFItems.BLOCK_PATTERN_DEEPSLATE.get());
                output.accept(MSFItems.BLOCK_PATTERN_PAWS.get());
                output.accept(MSFItems.BLOCK_PATTERN_HEARTS.get());
                output.accept(MSFItems.BLOCK_PATTERN_HONEYCOMB.get());
                output.accept(MSFItems.BLOCK_PATTERN_STARS.get());
                output.accept(MSFItems.BLOCK_PATTERN_PIPES.get());
                output.accept(MSFItems.BLOCK_PATTERN_SPROUTS.get());
                output.accept(MSFItems.BLOCK_PATTERN_DIAMOND.get());
                output.accept(MSFItems.BLOCK_PATTERN_BUBBLES.get());
                output.accept(MSFItems.BLOCK_PATTERN_PRISMARINE.get());
                output.accept(MSFItems.BLOCK_PATTERN_FOCUS.get());
                output.accept(MSFItems.BLOCK_PATTERN_BRICKS.get());
                output.accept(MSFItems.BLOCK_PATTERN_FLOWERS.get());



            })
            .backgroundTexture(MoreSnifferFlowers.loc("textures/gui/container/tab_items.png"))
            .build()
    );

    Identifier[] UNSELECTED_TOP_TABS = new Identifier[]{
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_unselected_1"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_unselected_2"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_unselected_3"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_unselected_4"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_unselected_5"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_unselected_6"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_unselected_7")
    };
    Identifier[] SELECTED_TOP_TABS = new Identifier[]{
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_selected_1"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_selected_2"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_selected_3"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_selected_4"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_selected_5"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_selected_6"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_top_selected_7")
    };
    Identifier[] UNSELECTED_BOTTOM_TABS = new Identifier[]{
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_unselected_1"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_unselected_2"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_unselected_3"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_unselected_4"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_unselected_5"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_unselected_6"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_unselected_7")
    };
    Identifier[] SELECTED_BOTTOM_TABS = new Identifier[]{
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_selected_1"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_selected_2"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_selected_3"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_selected_4"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_selected_5"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_selected_6"),
            MoreSnifferFlowers.loc("container/creative_inventory/tab_bottom_selected_7")
    };

}