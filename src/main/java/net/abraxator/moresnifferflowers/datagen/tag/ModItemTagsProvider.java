package net.abraxator.moresnifferflowers.datagen.tag;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends BlockTagCopyingItemTagProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> tagLookupCompletableFuture) {
        super(output, lookupProvider, tagLookupCompletableFuture, MoreSnifferFlowers.MOD_ID);
    }


    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tags.Items.SEEDS).add(MSFItems.DAWNBERRY_VINE_SEEDS.get(), MSFItems.AMBUSH_SEEDS.get(), MSFItems.BONMEELIA_SEEDS.get(), MSFItems.DYESPRIA_SEEDS.get());

        this.tag(net.minecraft.tags.ItemTags.TRIM_MATERIALS).add(MSFItems.AMBER_SHARD.get(), MSFItems.GARNET_SHARD.get(), MSFItems.CROPRESSED_BEETROOT.get(), MSFItems.CROPRESSED_POTATO.get(), MSFItems.CROPRESSED_NETHERWART.get(), MSFItems.CROPRESSED_CARROT.get(), MSFItems.CROPRESSED_WHEAT.get());
       // this.tag(net.minecraft.tags.ItemTags.TRIM_TEMPLATES).add(MSFItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        this.tag(MSFTags.MSFItemTags.AROMA_TRIM_TEMPLATE_INGREDIENT).add(MSFItems.AMBER_SHARD.get(), MSFBlocks.AMBER_BLOCK.get().asItem());
        this.tag(MSFTags.MSFItemTags.CROPRESSABLE).add(net.minecraft.world.item.Items.POTATO, net.minecraft.world.item.Items.CARROT, net.minecraft.world.item.Items.BEETROOT, net.minecraft.world.item.Items.NETHER_WART, net.minecraft.world.item.Items.WHEAT);
        this.tag(MSFTags.MSFItemTags.CROPRESSED_CROPS).add(MSFItems.CROPRESSED_CARROT.get(), MSFItems.CROPRESSED_POTATO.get(), MSFItems.CROPRESSED_WHEAT.get(), MSFItems.CROPRESSED_BEETROOT.get(), MSFItems.CROPRESSED_NETHERWART.get());
        this.tag(MSFTags.MSFItemTags.CROP_SMITHING_TEMPLATES).add(MSFItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MSFItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        this.tag(MSFTags.MSFItemTags.BLOCK_PATTERNS).add(MSFItems.BLOCK_PATTERN_PIPES.get(), MSFItems.BLOCK_PATTERN_BRICKS.get(), MSFItems.BLOCK_PATTERN_FOCUS.get(), MSFItems.BLOCK_PATTERN_BUBBLES.get(), MSFItems.BLOCK_PATTERN_CLOUDS.get(), MSFItems.BLOCK_PATTERN_DEEPSLATE.get(),
                MSFItems.BLOCK_PATTERN_DIAMOND.get(), MSFItems.BLOCK_PATTERN_EYE.get(), MSFItems.BLOCK_PATTERN_HEARTS.get(), MSFItems.BLOCK_PATTERN_HONEYCOMB.get(), MSFItems.BLOCK_PATTERN_PAWS.get(), MSFItems.BLOCK_PATTERN_PRISMARINE.get(),
                MSFItems.BLOCK_PATTERN_SPROUTS.get(), MSFItems.BLOCK_PATTERN_STARS.get(), MSFItems.BLOCK_PATTERN_COVER.get(), MSFItems.BLOCK_PATTERN_FLOWERS.get());

        this.tag(MSFTags.MSFItemTags.CROPRESSOR_PIECES).add(MSFItems.SCRAP_PIECE.get(), MSFItems.BELT_PIECE.get(), MSFItems.ENGINE_PIECE.get(), MSFItems.TUBE_PIECE.get(), MSFItems.PRESS_PIECE.get());

        this.tag(MSFTags.MSFItemTags.REBREWED_POTIONS).add(MSFItems.REBREWED_POTION.get(), MSFItems.REBREWED_SPLASH_POTION.get(), MSFItems.REBREWED_LINGERING_POTION.get());

        this.tag(MSFTags.MSFItemTags.VIVICUS_LOGS).add(MSFBlocks.VIVICUS_LOG.get().asItem(), MSFBlocks.VIVICUS_WOOD.get().asItem(), MSFBlocks.STRIPPED_VIVICUS_LOG.get().asItem(), MSFBlocks.STRIPPED_VIVICUS_WOOD.get().asItem() );
        this.tag(MSFTags.MSFItemTags.CORRUPTED_LOGS).add(MSFBlocks.CORRUPTED_LOG.get().asItem(), MSFBlocks.CORRUPTED_WOOD.get().asItem(), MSFBlocks.STRIPPED_CORRUPTED_LOG.get().asItem(), MSFBlocks.STRIPPED_CORRUPTED_WOOD.get().asItem() );
        this.tag(net.minecraft.tags.ItemTags.LOGS_THAT_BURN).addTags(MSFTags.MSFItemTags.CORRUPTED_LOGS, MSFTags.MSFItemTags.VIVICUS_LOGS);

        this.tag(net.minecraft.tags.ItemTags.WOODEN_BUTTONS).add(MSFBlocks.CORRUPTED_BUTTON.get().asItem(), MSFBlocks.VIVICUS_BUTTON.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_PRESSURE_PLATES).add(MSFBlocks.CORRUPTED_PRESSURE_PLATE.get().asItem(), MSFBlocks.VIVICUS_PRESSURE_PLATE.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_DOORS).add(MSFBlocks.CORRUPTED_DOOR.get().asItem(), MSFBlocks.VIVICUS_DOOR.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_SLABS).add(MSFBlocks.CORRUPTED_SLAB.get().asItem(), MSFBlocks.VIVICUS_SLAB.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_STAIRS).add(MSFBlocks.CORRUPTED_STAIRS.get().asItem(), MSFBlocks.VIVICUS_STAIRS.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_FENCES).add(MSFBlocks.CORRUPTED_FENCE.get().asItem(), MSFBlocks.VIVICUS_FENCE.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.FENCE_GATES).add(MSFBlocks.CORRUPTED_FENCE_GATE.get().asItem(), MSFBlocks.VIVICUS_FENCE_GATE.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_TRAPDOORS).add(MSFBlocks.CORRUPTED_TRAPDOOR.get().asItem(), MSFBlocks.VIVICUS_TRAPDOOR.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.PLANKS).add(MSFBlocks.CORRUPTED_PLANKS.get().asItem(), MSFBlocks.VIVICUS_PLANKS.get().asItem());
        this.tag(net.minecraft.tags.ItemTags.SAPLINGS).add(MSFBlocks.CORRUPTED_SAPLING.get().asItem(), MSFBlocks.VIVICUS_SAPLING.get().asItem());

        this.tag(MSFTags.MSFItemTags.COLORABLE)
                .add(MSFBlocks.STRIPPED_VIVICUS_WOOD.asItem(), MSFBlocks.STRIPPED_VIVICUS_LOG.asItem(), MSFBlocks.VIVICUS_BUTTON.asItem(),
                MSFBlocks.VIVICUS_DOOR.asItem(), MSFBlocks.VIVICUS_FENCE.asItem(), MSFBlocks.VIVICUS_FENCE_GATE.asItem(),
                MSFBlocks.VIVICUS_LEAVES.asItem(), MSFBlocks.VIVICUS_LOG.asItem(), MSFBlocks.VIVICUS_PLANKS.asItem(),
                MSFBlocks.VIVICUS_PRESSURE_PLATE.asItem(), MSFBlocks.VIVICUS_SAPLING.asItem(), MSFBlocks.VIVICUS_STAIRS.asItem(),
                MSFBlocks.VIVICUS_SLAB.asItem(), MSFBlocks.VIVICUS_TRAPDOOR.asItem(), MSFBlocks.VIVICUS_WOOD.asItem(),
                MSFBlocks.VIVICUS_LEAVES_SPROUT.asItem(), MSFBlocks.VIVICUS_SIGN.asItem(), MSFBlocks.VIVICUS_HANGING_SIGN.asItem());

        this.tag(net.minecraft.tags.ItemTags.SNIFFER_FOOD).add(MSFItems.DAWNBERRY.get().asItem());

        this.tag(Tags.Items.MUSIC_DISCS).add(MSFItems.MUSIC_DISC_BOBLING.get());

        this.tag(MSFTags.MSFItemTags.MSF_SNIFFER_LOOT).add(MSFItems.DAWNBERRY_VINE_SEEDS.get(), MSFItems.DYESPRIA_SEEDS.get(), MSFItems.AMBUSH_SEEDS.get(), MSFItems.CAULORFLOWER_SEEDS.get(),
                MSFItems.BONMEELIA_SEEDS.get(), MSFItems.BONDRIPIA_SEEDS.get(), MSFBlocks.VIVICUS_SAPLING.get().asItem(), MSFItems.SALTEMONE_SEEDS.get());
    }

    private Item item(DeferredBlock<Block> object) {
        return object.get().asItem();
    }
}