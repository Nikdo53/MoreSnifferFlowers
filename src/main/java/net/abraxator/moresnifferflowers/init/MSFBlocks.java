package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.*;
import net.abraxator.moresnifferflowers.blocks.corrupted.*;
import net.abraxator.moresnifferflowers.blocks.xbush.AmbushBlockLower;
import net.abraxator.moresnifferflowers.blocks.xbush.AmbushBlockUpper;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockOut;
import net.abraxator.moresnifferflowers.blocks.GiantCropBlock;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockBase;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockTop;
import net.abraxator.moresnifferflowers.blocks.signs.ModHangingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModStandingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModWallHangingSign;
import net.abraxator.moresnifferflowers.blocks.signs.ModWallSignBlock;
import net.abraxator.moresnifferflowers.blocks.vivicus.*;
import net.abraxator.moresnifferflowers.blocks.xbush.GarbushBlockLower;
import net.abraxator.moresnifferflowers.blocks.xbush.GarbushBlockUpper;
import net.abraxator.moresnifferflowers.items.GiantCropItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public interface MSFBlocks {
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MoreSnifferFlowers.MOD_ID);

    DeferredBlock<Block> TORCHFLOWER_AFLAME = registerWithItem("torchflower_aflame", TorchflowerAflameBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TORCHFLOWER).noCollision().randomTicks().lightLevel(value -> value.getValue(MSFStateProperties.AGE_2) == 1 ? 12 : 0));
    DeferredBlock<Block> TORCHFLAME = registerWithItem("torchflame", TorchflameBlock::new, () -> BlockBehaviour.Properties.of().sound(SoundType.EMPTY).lightLevel(value -> 12).instabreak().noOcclusion().pushReaction(PushReaction.DESTROY).noCollision());
    DeferredBlock<Block> TORCHEWFLOWER = registerWithItem("torchewflower", TorchewflowerBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TORCHFLOWER).noCollision().randomTicks());

    DeferredBlock<Block> DAWNBERRY_VINE = register("dawnberry_vine", properties -> new DawnberryVineBlock(properties, false), () -> BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).noCollision().strength(0.2F).sound(SoundType.GLOW_LICHEN).lightLevel(value -> value.getValue(DawnberryVineBlock.AGE) >= 3 ? 3 : 0).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion());
    DeferredBlock<Block> GLOOMBERRY_VINE = register("gloomberry_vine", GloomberryVineBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).noCollision().strength(0.2F).sound(SoundType.GLOW_LICHEN).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion());

    DeferredBlock<Block> AMBUSH_BOTTOM = register("ambush_bottom", AmbushBlockLower::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F));
    DeferredBlock<Block> AMBUSH_TOP = register("ambush_top", AmbushBlockUpper::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F));
    DeferredBlock<Block> GARBUSH_BOTTOM = register("garbush_bottom", GarbushBlockLower::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F));
    DeferredBlock<Block> GARBUSH_TOP = register("garbush_top", GarbushBlockUpper::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F));

    DeferredBlock<Block> AMBER_BLOCK = registerWithItem("amber_block", HalfTransparentBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion());
    DeferredBlock<Block> CHISELED_AMBER = registerWithItem("chiseled_amber", HalfTransparentBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion());
    DeferredBlock<Block> CHISELED_AMBER_SLAB = registerWithItem("chiseled_amber_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.CHISELED_AMBER.get()));
    DeferredBlock<Block> CRACKED_AMBER = registerWithItem("cracked_amber", HalfTransparentBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion());
    DeferredBlock<Block> AMBER_MOSAIC = registerWithItem("amber_mosaic", HalfTransparentBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion());
    DeferredBlock<Block> AMBER_MOSAIC_SLAB = registerWithItem("amber_mosaic_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.AMBER_MOSAIC.get()));
    DeferredBlock<Block> AMBER_MOSAIC_STAIRS = registerWithItem("amber_mosaic_stairs", properties -> new StairBlock(AMBER_MOSAIC.get().defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.AMBER_MOSAIC.get()));
    DeferredBlock<Block> AMBER_MOSAIC_WALL = registerWithItem("amber_mosaic_wall", WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.AMBER_MOSAIC.get()));
    DeferredBlock<Block> GARNET_BLOCK = registerWithItem("garnet_block", HalfTransparentBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(5.0F).noOcclusion());
    DeferredBlock<Block> CHISELED_GARNET = registerWithItem("chiseled_garnet", HalfTransparentBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion());
    DeferredBlock<Block> CHISELED_GARNET_SLAB = registerWithItem("chiseled_garnet_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.CHISELED_GARNET.get()));
    DeferredBlock<Block> CRACKED_GARNET = registerWithItem("cracked_garnet", HalfTransparentBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion());
    DeferredBlock<Block> GARNET_MOSAIC = registerWithItem("garnet_mosaic", HalfTransparentBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion());
    DeferredBlock<Block> GARNET_MOSAIC_SLAB = registerWithItem("garnet_mosaic_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GARNET_MOSAIC.get()));
    DeferredBlock<Block> GARNET_MOSAIC_STAIRS = registerWithItem("garnet_mosaic_stairs", properties -> new StairBlock(GARNET_MOSAIC.get().defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GARNET_MOSAIC.get()));
    DeferredBlock<Block> GARNET_MOSAIC_WALL = registerWithItem("garnet_mosaic_wall", WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GARNET_MOSAIC.get()));

    DeferredBlock<Block> CAULORFLOWER = register("caulorflower", CaulorflowerBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollision().noOcclusion().randomTicks());
    DeferredBlock<Block> PATTERNFLOWER = register("patternflower", PatternflowerBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollision().noOcclusion().randomTicks());

    DeferredBlock<Block> GIANT_CARROT = registerGiantCrop("giant_carrot", GiantCropBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BANJO).strength(3.0F).sound(SoundType.MOSS_CARPET).noOcclusion().pushReaction(PushReaction.BLOCK).isSuffocating(GiantCropBlock.STATE_PREDICATE));
    DeferredBlock<Block> GIANT_POTATO = registerGiantCrop("giant_potato", GiantCropBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GIANT_CARROT.get()));
    DeferredBlock<Block> GIANT_NETHERWART = registerGiantCrop("giant_netherwart", GiantCropBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.STATE_PREDICATE).dynamicShape());
    DeferredBlock<Block> GIANT_BEETROOT = registerGiantCrop("giant_beetroot", GiantCropBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.STATE_PREDICATE));
    DeferredBlock<Block> GIANT_WHEAT = registerGiantCrop("giant_wheat", GiantCropBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.STATE_PREDICATE));
    DeferredBlock<Block> GIANT_CABBAGE = registerGiantCrop("giant_cabbage", GiantCropBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.STATE_PREDICATE));
    DeferredBlock<Block> GIANT_ONION = registerGiantCrop("giant_onion", GiantCropBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.STATE_PREDICATE));
    DeferredBlock<Block> GIANT_TOMATO = registerGiantCrop("giant_tomato", GiantCropBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.STATE_PREDICATE));
    DeferredBlock<Block> GIANT_RICE = registerGiantCrop("giant_rice", GiantCropBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.STATE_PREDICATE));

    DeferredBlock<Block> BONMEELIA = register("bonmeelia", properties -> new BonmeeliaBlock(properties, false), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion());
    DeferredBlock<Block> BONWILTIA = register("bonwiltia", properties -> new BonmeeliaBlock(properties, true), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion());
    DeferredBlock<Block> BONDRIPIA = register("bondripia", BondripiaBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks().pushReaction(PushReaction.BLOCK));
    DeferredBlock<Block> ACIDRIPIA = register("acidripia", AciddripiaBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks().pushReaction(PushReaction.BLOCK));
    DeferredBlock<Block> BONMEEL_FILLED_CAULDRON = register("bonmeel_filled_cauldron", properties -> new ModLayeredCauldronBlock(Biome.Precipitation.NONE, MSFCauldronInteractions.BONMEEL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON));
    DeferredBlock<Block> ACID_FILLED_CAULDRON = register("acid_filled_cauldron", properties -> new ModLayeredCauldronBlock(Biome.Precipitation.NONE, MSFCauldronInteractions.ACID, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON));

    DeferredBlock<Block> CROPRESSOR_CENTER = register("cropressor_center", properties -> new CropressorBlockBase(properties, CropressorBlockBase.Part.CENTER), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL));
    DeferredBlock<Block> CROPRESSOR_OUT = register("cropressor_out", properties -> new CropressorBlockOut(properties, CropressorBlockBase.Part.OUT), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL));

    DeferredBlock<Block> REBREWING_STAND_BOTTOM = register("rebrewing_stand_bottom", RebrewingStandBlockBase::new, () -> BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion());
    DeferredBlock<Block> REBREWING_STAND_TOP = register("rebrewing_stand_top", RebrewingStandBlockTop::new, () -> BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion());

    DeferredBlock<Block> DYESPRIA_PLANT = register("dyespria_plant", DyespriaPlantBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    DeferredBlock<Block> DYESCRAPIA_PLANT = register("dyescrapia_plant", DyescrapiaPlantBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(DYESPRIA_PLANT.get()));

    DeferredBlock<RotatedPillarBlock> CORRUPTED_LOG = registerWithItem("corrupted_log", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_STEM));
    DeferredBlock<RotatedPillarBlock> CORRUPTED_WOOD = registerWithItem("corrupted_wood", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HYPHAE));
    DeferredBlock<RotatedPillarBlock> STRIPPED_CORRUPTED_LOG = registerWithItem("stripped_corrupted_log", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_STEM));
    DeferredBlock<RotatedPillarBlock> STRIPPED_CORRUPTED_WOOD = registerWithItem("stripped_corrupted_wood", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE));
    DeferredBlock<Block> CORRUPTED_PLANKS = registerWithItem("corrupted_planks", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS));
    DeferredBlock<Block> CORRUPTED_STAIRS = registerWithItem("corrupted_stairs", properties -> new StairBlock(CORRUPTED_PLANKS.get().defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(CORRUPTED_PLANKS.get()));
    DeferredBlock<Block> CORRUPTED_SLAB = registerWithItem("corrupted_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SLAB));
    DeferredBlock<Block> CORRUPTED_FENCE = registerWithItem("corrupted_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FENCE));
    DeferredBlock<Block> CORRUPTED_FENCE_GATE = registerWithItem("corrupted_fence_gate", properties -> new FenceGateBlock(MSFWood.WoodTypes.CORRUPTED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FENCE_GATE));
    DeferredBlock<Block> CORRUPTED_DOOR = registerWithItem("corrupted_door", properties -> new DoorBlock(BlockSetType.WARPED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_DOOR));
    DeferredBlock<Block> CORRUPTED_TRAPDOOR = registerWithItem("corrupted_trapdoor", properties -> new TrapDoorBlock(BlockSetType.WARPED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_TRAPDOOR));
    DeferredBlock<Block> CORRUPTED_PRESSURE_PLATE = registerWithItem("corrupted_pressure_plate", properties -> new PressurePlateBlock(BlockSetType.WARPED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PRESSURE_PLATE));
    DeferredBlock<Block> CORRUPTED_BUTTON = registerWithItem("corrupted_button", properties -> new ButtonBlock(BlockSetType.WARPED, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_BUTTON));
    DeferredBlock<Block> CORRUPTED_LEAVES = registerWithItem("corrupted_leaves", CorruptedLeavesBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES).noOcclusion());
    DeferredBlock<Block> CORRUPTED_SAPLING = registerWithItem("corrupted_sapling", properties -> new CorruptedSaplingBlock(MSFWood.TreeGrowers.CORRUPTED_TREE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SAPLING));
    DeferredBlock<Block> CORRUPTED_SLUDGE = registerWithItem("corrupted_sludge", CorruptedSludgeBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(2.0F).friction(0.8F).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4));
    DeferredBlock<Block> CORRUPTED_SLIME_LAYER = registerWithItem("corrupted_slime_layer", CorruptedSlimeLayerBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(0.5F).friction(0.8F).noOcclusion().randomTicks().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4));
    DeferredBlock<Block> CORRUPTED_SIGN = register("corrupted_sign", properties -> new ModStandingSignBlock(MSFWood.WoodTypes.CORRUPTED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SIGN));
    DeferredBlock<Block> CORRUPTED_WALL_SIGN = register("corrupted_wall_sign", properties -> new ModWallSignBlock(MSFWood.WoodTypes.CORRUPTED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WALL_SIGN));
    DeferredBlock<Block> CORRUPTED_HANGING_SIGN = register("corrupted_hanging_sign", properties -> new ModHangingSignBlock(MSFWood.WoodTypes.CORRUPTED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HANGING_SIGN));
    DeferredBlock<Block> CORRUPTED_WALL_HANGING_SIGN = register("corrupted_wall_hanging_sign", properties -> new ModWallHangingSign(MSFWood.WoodTypes.CORRUPTED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WALL_HANGING_SIGN));
    DeferredBlock<Block> CORRUPTED_LEAVES_BUSH = registerWithItem("corrupted_leaves_bush", CorruptedLeavesBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.CORRUPTED_LEAVES.get()).noOcclusion());

    DeferredBlock<Block> CORRUPTED_GRASS = registerWithItem("corrupted_grass", TallGrassBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollision().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).ignitedByLava().pushReaction(PushReaction.DESTROY));
    DeferredBlock<Block> CORRUPTED_TALL_GRASS = registerWithItem("corrupted_tall_grass", DoublePlantBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollision().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY));
    DeferredBlock<RotatedPillarBlock> DECAYED_LOG = registerWithItem("decayed_log", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG));
    DeferredBlock<Block> CORRUPTED_GRASS_BLOCK = registerWithItem("corrupted_grass_block", CorruptedGrassBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS));
    DeferredBlock<Block> CURED_GRASS_BLOCK = registerWithItem("cured_grass_block", CuredGrassBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS));
    DeferredBlock<Block> CORRUPTED_WART = registerWithItem("corrupted_wart", CorruptedWartBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().instabreak().dynamicShape().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).pushReaction(PushReaction.DESTROY));

    DeferredBlock<RotatedPillarBlock> VIVICUS_LOG = registerWithItem("vivicus_log", VivicusRotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LOG));
    DeferredBlock<RotatedPillarBlock> VIVICUS_WOOD = registerWithItem("vivicus_wood", VivicusRotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WOOD));
    DeferredBlock<RotatedPillarBlock> STRIPPED_VIVICUS_LOG = registerWithItem("stripped_vivicus_log", VivicusRotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_LOG));
    DeferredBlock<RotatedPillarBlock> STRIPPED_VIVICUS_WOOD = registerWithItem("stripped_vivicus_wood", VivicusRotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD));
    DeferredBlock<Block> VIVICUS_PLANKS = registerWithItem("vivicus_planks", VivicusBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS));
    DeferredBlock<Block> VIVICUS_STAIRS = registerWithItem("vivicus_stairs", properties -> new VivicusStairBlock(VIVICUS_PLANKS.get().defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(VIVICUS_PLANKS.get()));
    DeferredBlock<Block> VIVICUS_SLAB = registerWithItem("vivicus_slab", VivicusSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SLAB));
    DeferredBlock<Block> VIVICUS_FENCE = registerWithItem("vivicus_fence", VivicusFenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE));
    DeferredBlock<Block> VIVICUS_FENCE_GATE = registerWithItem("vivicus_fence_gate", properties -> new VivicusFenceGateBlock(MSFWood.WoodTypes.VIVICUS, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE_GATE));
    DeferredBlock<Block> VIVICUS_DOOR = registerWithItem("vivicus_door", properties -> new VivicusDoorBlock(BlockSetType.CHERRY, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_DOOR));
    DeferredBlock<Block> VIVICUS_TRAPDOOR = registerWithItem("vivicus_trapdoor", properties -> new VivicusTrapDoorBlock(BlockSetType.CHERRY, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_TRAPDOOR));
    DeferredBlock<Block> VIVICUS_PRESSURE_PLATE = registerWithItem("vivicus_pressure_plate", properties -> new VivicusPressurePlateBlock(BlockSetType.CHERRY, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PRESSURE_PLATE));
    DeferredBlock<Block> VIVICUS_BUTTON = registerWithItem("vivicus_button", properties -> new VivicusButtonBlock(BlockSetType.CHERRY, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_BUTTON));
    DeferredBlock<Block> VIVICUS_LEAVES = registerWithItem("vivicus_leaves", VivicusLeavesBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES));
    DeferredBlock<Block> VIVICUS_SAPLING = registerWithItem("vivicus_sapling", VivicusSaplingBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SAPLING));
    DeferredBlock<Block> VIVICUS_LEAVES_SPROUT = registerWithItem("vivicus_leaves_sprout", VivicusSproutingBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PROPAGULE));
    DeferredBlock<Block> VIVICUS_SIGN = register("vivicus_sign", properties -> new VivicusStandingSignBlock(MSFWood.WoodTypes.VIVICUS, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SIGN));
    DeferredBlock<Block> VIVICUS_WALL_SIGN = register("vivicus_wall_sign", properties -> new VivicusWallSignBlock(MSFWood.WoodTypes.VIVICUS, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WALL_SIGN));
    DeferredBlock<Block> VIVICUS_HANGING_SIGN = register("vivicus_hanging_sign", properties -> new VivicusHangingSignBlock(MSFWood.WoodTypes.VIVICUS, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_HANGING_SIGN));
    DeferredBlock<Block> VIVICUS_WALL_HANGING_SIGN = register("vivicus_wall_hanging_sign", properties -> new VivicusHangingWallSignBlock(MSFWood.WoodTypes.VIVICUS, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WALL_HANGING_SIGN));

    DeferredBlock<Block> POTTED_DYESPRIA = register("potted_dyespria", properties -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DYESPRIA_PLANT, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
    DeferredBlock<Block> POTTED_CORRUPTED_SAPLING = register("potted_corrupted_sapling", properties -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CORRUPTED_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
    DeferredBlock<Block> POTTED_VIVICUS_SAPLING = register("potted_vivicus_sapling", properties -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VIVICUS_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
    DeferredBlock<Block> BEROOT_CAULDRON = registerWithItem("beroot_cauldron", BerootCauldronBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL).noOcclusion().pushReaction(PushReaction.BLOCK).isSuffocating(MSFBlocks::never));

    DeferredBlock<Block> SALTEMONE = register("saltemone", SaltemoneBlock::new, () -> BlockBehaviour.Properties.of().noOcclusion().pushReaction(PushReaction.BLOCK).isSuffocating(MSFBlocks::never).strength(0.3F).sound(SoundType.WET_GRASS));
    DeferredBlock<Block> SOURLEMONE = register("sourlemone", SourlemonBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.SALTEMONE.get()).noOcclusion().pushReaction(PushReaction.BLOCK).isSuffocating(MSFBlocks::never));
    DeferredBlock<Block> SALTY_CLUMP = register("salty_clump", SaltyClumpBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD).noOcclusion().sound(SoundType.SAND).pushReaction(PushReaction.DESTROY).isSuffocating(MSFBlocks::never).noCollision());
    DeferredBlock<Block> DRIPSALT = registerWithItem("dripsalt", DripsaltBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.POINTED_DRIPSTONE));
    DeferredBlock<Block> SOUR_PUDDLE = register("sour_puddle", SourPuddleBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MSFBlocks.SALTY_CLUMP.get()).sound(SoundType.MUD).requiresCorrectToolForDrops().friction(0.98F).noOcclusion().pushReaction(PushReaction.DESTROY).isSuffocating(MSFBlocks::never));

    private static <T extends Block> DeferredBlock<T> register(String name, Function<BlockBehaviour.Properties, ? extends T> block, Supplier<BlockBehaviour.Properties> properties) {
        return BLOCKS.register(name, () -> block.apply(properties.get()));
    }

    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Function<BlockBehaviour.Properties, ? extends T> block, Supplier<BlockBehaviour.Properties> properties) {
        DeferredBlock<T> toReturn = register(name, block, properties);
        MSFItems.ITEMS.registerItem(name, itemProps -> new BlockItem(toReturn.get(), itemProps));
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerGiantCrop(String name, Function<BlockBehaviour.Properties, ? extends T> block, Supplier<BlockBehaviour.Properties> properties) {
        DeferredBlock<T> toReturn = register(name, block, properties);
        MSFItems.ITEMS.registerItem(name, itemProps -> new GiantCropItem(toReturn.get(), itemProps));
        return toReturn;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }
}
