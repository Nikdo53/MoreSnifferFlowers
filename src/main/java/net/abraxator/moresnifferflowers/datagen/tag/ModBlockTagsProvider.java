package net.abraxator.moresnifferflowers.datagen.tag;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.abraxator.moresnifferflowers.init.MSFTags.BlockTags.*;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(net.minecraft.tags.BlockTags.FLOWERS).add(MSFBlocks.DAWNBERRY_VINE.get(), MSFBlocks.AMBUSH_BOTTOM.get(), MSFBlocks.AMBUSH_TOP.get(), MSFBlocks.CAULORFLOWER.get(), MSFBlocks.DYESPRIA_PLANT.get(), MSFBlocks.BONMEELIA.get());
        this.tag(net.minecraft.tags.BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(MSFBlocks.DAWNBERRY_VINE.get(), MSFBlocks.AMBUSH_BOTTOM.get(), MSFBlocks.AMBUSH_TOP.get(), MSFBlocks.CAULORFLOWER.get());

        this.tag(net.minecraft.tags.BlockTags.SWORD_EFFICIENT).add(MSFBlocks.DAWNBERRY_VINE.get(), MSFBlocks.AMBUSH_BOTTOM.get(), MSFBlocks.AMBUSH_TOP.get(), MSFBlocks.CAULORFLOWER.get(), MSFBlocks.BONMEELIA.get(),
                MSFBlocks.GIANT_CARROT.get(), MSFBlocks.GIANT_POTATO.get(), MSFBlocks.GIANT_NETHERWART.get(), MSFBlocks.GIANT_BEETROOT.get(), MSFBlocks.GIANT_WHEAT.get());
        this.tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_HOE).add(MSFBlocks.DAWNBERRY_VINE.get(), MSFBlocks.AMBUSH_BOTTOM.get(), MSFBlocks.AMBUSH_TOP.get(), MSFBlocks.BONMEELIA.get(), MSFBlocks.CAULORFLOWER.get(),
                MSFBlocks.CORRUPTED_LEAVES.get(), MSFBlocks.CORRUPTED_LEAVES_BUSH.get(), MSFBlocks.VIVICUS_LEAVES.get(), MSFBlocks.VIVICUS_LEAVES_SPROUT.get()).addTag(GIANT_CROPS);
        this.tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE).add(MSFBlocks.CAULORFLOWER.get()).addTag(GIANT_CROPS);
        this.tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL).add(MSFBlocks.CORRUPTED_SLUDGE.get(), MSFBlocks.CORRUPTED_SLIME_LAYER.get(), MSFBlocks.CORRUPTED_GRASS_BLOCK.get(), MSFBlocks.CURED_GRASS_BLOCK.get(), MSFBlocks.SALTY_CLUMP.get(), MSFBlocks.SOUR_PUDDLE.get());

        this.tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE).add(
                MSFBlocks.AMBER_BLOCK.get(), MSFBlocks.GARNET_BLOCK.get(), MSFBlocks.CROPRESSOR_OUT.get(), MSFBlocks.CROPRESSOR_CENTER.get(), MSFBlocks.REBREWING_STAND_BOTTOM.get(),
                MSFBlocks.CHISELED_AMBER.get(), MSFBlocks.CHISELED_AMBER_SLAB.get(), MSFBlocks.CRACKED_AMBER.get(), MSFBlocks.AMBER_MOSAIC.get(), MSFBlocks.AMBER_MOSAIC_STAIRS.get(), MSFBlocks.AMBER_MOSAIC_WALL.get(), MSFBlocks.AMBER_MOSAIC_SLAB.get(),
                MSFBlocks.CHISELED_GARNET.get(), MSFBlocks.CHISELED_GARNET_SLAB.get(), MSFBlocks.CRACKED_GARNET.get(), MSFBlocks.GARNET_MOSAIC.get(), MSFBlocks.GARNET_MOSAIC_STAIRS.get(), MSFBlocks.GARNET_MOSAIC_WALL.get(), MSFBlocks.GARNET_MOSAIC_SLAB.get(),
                MSFBlocks.REBREWING_STAND_TOP.get(), MSFBlocks.BEROOT_CAULDRON.get(), MSFBlocks.DRIPSALT.get() );
        this.tag(net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL).add(MSFBlocks.AMBER_BLOCK.get(), MSFBlocks.GARNET_BLOCK.get(), MSFBlocks.CROPRESSOR_OUT.get(), MSFBlocks.CROPRESSOR_CENTER.get(), MSFBlocks.REBREWING_STAND_BOTTOM.get(), MSFBlocks.REBREWING_STAND_TOP.get());

        this.tag(MSFTags.BlockTags.BONMEELABLE).add(net.minecraft.world.level.block.Blocks.WHEAT, net.minecraft.world.level.block.Blocks.CARROTS, net.minecraft.world.level.block.Blocks.POTATOES, net.minecraft.world.level.block.Blocks.BEETROOTS, net.minecraft.world.level.block.Blocks.NETHER_WART)
                .addOptional(ModBlocks.TOMATO_CROP.get()).addOptional(ModBlocks.ONION_CROP.get()).addOptional(ModBlocks.CABBAGE_CROP.get()).addOptional(ModBlocks.RICE_CROP_PANICLES.get());

        this.tag(MSFTags.BlockTags.GIANT_CROP_REPLACEABLE).addOptional(ModBlocks.RICE_CROP.get());
        this.tag(MSFTags.BlockTags.GIANT_CROPS).add(MSFBlocks.GIANT_CARROT.get(), MSFBlocks.GIANT_POTATO.get(), MSFBlocks.GIANT_NETHERWART.get(), MSFBlocks.GIANT_BEETROOT.get(), MSFBlocks.GIANT_WHEAT.get(), MSFBlocks.GIANT_ONION.get(), MSFBlocks.GIANT_TOMATO.get(), MSFBlocks.GIANT_CABBAGE.get(), MSFBlocks.GIANT_RICE.get());
        this.tag(MSFTags.BlockTags.NO_SHADING).add(MSFBlocks.GIANT_RICE.get());
        this.tag(MSFTags.BlockTags.WATERLOGGABLE).add(MSFBlocks.GIANT_RICE.get());

        this.tag(net.minecraft.tags.BlockTags.LOGS_THAT_BURN).add(MSFBlocks.DECAYED_LOG.get(), MSFBlocks.CORRUPTED_LOG.get(), MSFBlocks.VIVICUS_LOG.get(), MSFBlocks.STRIPPED_CORRUPTED_LOG.get(), MSFBlocks.STRIPPED_VIVICUS_LOG.get(), MSFBlocks.CORRUPTED_WOOD.get(), MSFBlocks.VIVICUS_WOOD.get(), MSFBlocks.STRIPPED_CORRUPTED_WOOD.get(), MSFBlocks.STRIPPED_VIVICUS_WOOD.get());
        this.tag(net.minecraft.tags.BlockTags.LEAVES).add(MSFBlocks.CORRUPTED_LEAVES.get(), MSFBlocks.CORRUPTED_LEAVES_BUSH.get(), MSFBlocks.VIVICUS_LEAVES.get());

        this.tag(net.minecraft.tags.BlockTags.WOODEN_BUTTONS).add(MSFBlocks.CORRUPTED_BUTTON.get(), MSFBlocks.VIVICUS_BUTTON.get());
        this.tag(net.minecraft.tags.BlockTags.WOODEN_PRESSURE_PLATES).add(MSFBlocks.CORRUPTED_PRESSURE_PLATE.get(), MSFBlocks.VIVICUS_PRESSURE_PLATE.get());
        this.tag(net.minecraft.tags.BlockTags.WOODEN_DOORS).add(MSFBlocks.CORRUPTED_DOOR.get(), MSFBlocks.VIVICUS_DOOR.get());
        this.tag(net.minecraft.tags.BlockTags.WOODEN_SLABS).add(MSFBlocks.CORRUPTED_SLAB.get(), MSFBlocks.VIVICUS_SLAB.get());
        this.tag(net.minecraft.tags.BlockTags.WOODEN_STAIRS).add(MSFBlocks.CORRUPTED_STAIRS.get(), MSFBlocks.VIVICUS_STAIRS.get());
        this.tag(net.minecraft.tags.BlockTags.WOODEN_FENCES).add(MSFBlocks.CORRUPTED_FENCE.get(), MSFBlocks.VIVICUS_FENCE.get());
        this.tag(net.minecraft.tags.BlockTags.FENCE_GATES).add(MSFBlocks.CORRUPTED_FENCE_GATE.get(), MSFBlocks.VIVICUS_FENCE_GATE.get());
        this.tag(net.minecraft.tags.BlockTags.WOODEN_TRAPDOORS).add(MSFBlocks.CORRUPTED_TRAPDOOR.get(), MSFBlocks.VIVICUS_TRAPDOOR.get());
        this.tag(net.minecraft.tags.BlockTags.PLANKS).add(MSFBlocks.CORRUPTED_PLANKS.get(), MSFBlocks.VIVICUS_PLANKS.get());
        this.tag(net.minecraft.tags.BlockTags.SAPLINGS).add(MSFBlocks.CORRUPTED_SAPLING.get(), MSFBlocks.VIVICUS_SAPLING.get());
        this.tag(net.minecraft.tags.BlockTags.WALLS).add(MSFBlocks.AMBER_MOSAIC_WALL.get(), MSFBlocks.GARNET_MOSAIC_WALL.get());

        this.tag(VIVICUS_BLOCKS).add(
            MSFBlocks.STRIPPED_VIVICUS_WOOD.get(), MSFBlocks.STRIPPED_VIVICUS_LOG.get(), MSFBlocks.VIVICUS_BUTTON.get(),
            MSFBlocks.VIVICUS_DOOR.get(), MSFBlocks.VIVICUS_FENCE.get(), MSFBlocks.VIVICUS_FENCE_GATE.get(),
            MSFBlocks.VIVICUS_LEAVES.get(), MSFBlocks.VIVICUS_LOG.get(), MSFBlocks.VIVICUS_PLANKS.get(),
            MSFBlocks.VIVICUS_PRESSURE_PLATE.get(), MSFBlocks.VIVICUS_SAPLING.get(), MSFBlocks.VIVICUS_STAIRS.get(),
            MSFBlocks.VIVICUS_SLAB.get(), MSFBlocks.VIVICUS_TRAPDOOR.get(), MSFBlocks.VIVICUS_WOOD.get(),
            MSFBlocks.VIVICUS_LEAVES_SPROUT.get());
        this.tag(CORRUPTED_BLOCKS).add(
                MSFBlocks.STRIPPED_CORRUPTED_WOOD.get(), MSFBlocks.STRIPPED_CORRUPTED_LOG.get(), MSFBlocks.CORRUPTED_BUTTON.get(),
                MSFBlocks.CORRUPTED_DOOR.get(), MSFBlocks.CORRUPTED_FENCE.get(), MSFBlocks.CORRUPTED_FENCE_GATE.get(),
                MSFBlocks.CORRUPTED_LEAVES.get(), MSFBlocks.CORRUPTED_LEAVES_BUSH.get(), MSFBlocks.CORRUPTED_LOG.get(), MSFBlocks.CORRUPTED_PLANKS.get(),
                MSFBlocks.CORRUPTED_PRESSURE_PLATE.get(), MSFBlocks.CORRUPTED_SAPLING.get(), MSFBlocks.CORRUPTED_STAIRS.get(),
                MSFBlocks.CORRUPTED_SLAB.get(), MSFBlocks.CORRUPTED_TRAPDOOR.get(), MSFBlocks.CORRUPTED_WOOD.get(),
                MSFBlocks.CORRUPTED_SLUDGE.get());
        this.tag(CORRUPTED_SLUDGE).add(MSFBlocks.CORRUPTED_LOG.get(), MSFBlocks.CORRUPTED_LEAVES.get(), MSFBlocks.CORRUPTED_LEAVES_BUSH.get());
        this.tag(VIVICUS_TREE_REPLACABLE).addTag(net.minecraft.tags.BlockTags.REPLACEABLE_BY_TREES).add(MSFBlocks.VIVICUS_SAPLING.get());
        this.tag(CORRUPTION_TRANSFORMABLES).add(MSFBlocks.DYESPRIA_PLANT.get(), MSFBlocks.DAWNBERRY_VINE.get());
        this.tag(NO_CORRUPTED_SLIME_COLLISION).add(MSFBlocks.SALTEMONE.get(), MSFBlocks.SOURLEMONE.get());

        this.tag(MSFTags.BlockTags.STICKABLE)
                .add(net.minecraft.world.level.block.Blocks.SHORT_GRASS, net.minecraft.world.level.block.Blocks.STONE, net.minecraft.world.level.block.Blocks.SAND, net.minecraft.world.level.block.Blocks.GRAVEL, net.minecraft.world.level.block.Blocks.TALL_GRASS)
                .addTag(net.minecraft.tags.BlockTags.LEAVES).addTag(net.minecraft.tags.BlockTags.DIRT).addTag(net.minecraft.tags.BlockTags.REPLACEABLE);

        this.tag(CORRUPTION_SHIELDING).add(net.minecraft.world.level.block.Blocks.OXEYE_DAISY, net.minecraft.world.level.block.Blocks.SUNFLOWER, net.minecraft.world.level.block.Blocks.PITCHER_PLANT);
        this.tag(CARRYON_BLACKLIST).add(MSFBlocks.AMBUSH_BOTTOM.get(), MSFBlocks.AMBUSH_TOP.get(), MSFBlocks.GARBUSH_BOTTOM.get(), MSFBlocks.GARBUSH_TOP.get());

        this.tag(net.minecraft.tags.BlockTags.CAULDRONS).add(MSFBlocks.ACID_FILLED_CAULDRON.get(), MSFBlocks.BONMEEL_FILLED_CAULDRON.get());

        this.supTag(net.minecraft.tags.BlockTags.DIRT, MSFBlocks.CORRUPTED_GRASS_BLOCK, MSFBlocks.CURED_GRASS_BLOCK);
        this.tag(net.minecraft.tags.BlockTags.FLOWER_POTS).add(MSFBlocks.POTTED_DYESPRIA.get(), MSFBlocks.POTTED_CORRUPTED_SAPLING.get(), MSFBlocks.POTTED_VIVICUS_SAPLING.get());

        this.tag(MSFTags.BlockTags.DYED).add(net.minecraft.world.level.block.Blocks.GLASS, net.minecraft.world.level.block.Blocks.GLASS_PANE, net.minecraft.world.level.block.Blocks.TERRACOTTA, net.minecraft.world.level.block.Blocks.SHULKER_BOX, net.minecraft.world.level.block.Blocks.CANDLE)
                .addTag(Tags.Blocks.DYED).remove(net.minecraft.tags.BlockTags.BEDS);
    }

    @SafeVarargs
    public final void supTag(TagKey<Block> tagKey, Supplier<Block>... blocks) {
        var tag = this.tag(tagKey);

        for (Supplier<Block> block : blocks) {
            tag.add(block.get());
        }
    }
}
