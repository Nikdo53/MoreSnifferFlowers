package net.abraxator.moresnifferflowers.datagen.model;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static net.abraxator.moresnifferflowers.init.MSFBlocks.*;

public class MSFBlockFamilies {
    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();
    private static final String RECIPE_GROUP_PREFIX_WOODEN = "wooden";
    private static final String RECIPE_UNLOCKED_BY_HAS_PLANKS = "has_planks";

    public static final BlockFamily CORRUPTED = woodBuilder(CORRUPTED_PLANKS.get())
            .button(CORRUPTED_BUTTON.get())
            .fence(CORRUPTED_FENCE.get())
            .fenceGate(CORRUPTED_FENCE_GATE.get())
            .pressurePlate(CORRUPTED_PRESSURE_PLATE.get())
            .sign(CORRUPTED_SIGN.get(), CORRUPTED_WALL_SIGN.get())
            .slab(CORRUPTED_SLAB.get())
            .stairs(CORRUPTED_STAIRS.get())
            .door(CORRUPTED_DOOR.get())
            .trapdoor(CORRUPTED_TRAPDOOR.get())
            .getFamily();

    public static final BlockFamily VIVICUS = woodBuilder(VIVICUS_PLANKS.get())
            .button(VIVICUS_BUTTON.get())
            .fence(VIVICUS_FENCE.get())
            .fenceGate(VIVICUS_FENCE_GATE.get())
            .pressurePlate(VIVICUS_PRESSURE_PLATE.get())
            .sign(VIVICUS_SIGN.get(), VIVICUS_WALL_SIGN.get())
            .slab(VIVICUS_SLAB.get())
            .stairs(VIVICUS_STAIRS.get())
            .door(VIVICUS_DOOR.get())
            .trapdoor(VIVICUS_TRAPDOOR.get())
            .getFamily();

    public static final BlockFamily AMBER = familyBuilder(AMBER_BLOCK.get())
            .cracked(CRACKED_AMBER.get())
            .getFamily();

    public static final BlockFamily AMBER_MOSAICS = familyBuilder(AMBER_MOSAIC.get())
            .chiseled(CHISELED_AMBER.get())
            .slab(AMBER_MOSAIC_SLAB.get())
            .stairs(AMBER_MOSAIC_STAIRS.get())
            .wall(AMBER_MOSAIC_WALL.get())
            .getFamily();

    public static final BlockFamily AMBER_CHISELED = familyBuilder(CHISELED_AMBER.get())
            .slab(CHISELED_AMBER_SLAB.get())
            .getFamily();

    public static final BlockFamily GARNET = familyBuilder(GARNET_BLOCK.get())
            .cracked(CRACKED_GARNET.get())
            .getFamily();

    public static final BlockFamily GARNET_MOSAICS = familyBuilder(GARNET_MOSAIC.get())
            .chiseled(CHISELED_GARNET.get())
            .slab(GARNET_MOSAIC_SLAB.get())
            .stairs(GARNET_MOSAIC_STAIRS.get())
            .wall(GARNET_MOSAIC_WALL.get())
            .getFamily();

    public static final BlockFamily GARNET_CHISELED = familyBuilder(CHISELED_GARNET.get())
            .slab(CHISELED_GARNET_SLAB.get())
            .getFamily();


    private static BlockFamily.Builder woodBuilder(Block baseBlock) {
        return familyBuilder(baseBlock)
                .recipeUnlockedBy(RECIPE_UNLOCKED_BY_HAS_PLANKS)
                .recipeGroupPrefix(RECIPE_GROUP_PREFIX_WOODEN);
    }

    private static BlockFamily.Builder familyBuilder(Block baseBlock) {
        BlockFamily.Builder blockfamily$builder = new BlockFamily.Builder(baseBlock);
        BlockFamily blockfamily = MAP.put(baseBlock, blockfamily$builder.getFamily());
        if (blockfamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(baseBlock));
        } else {
            return blockfamily$builder;
        }
    }

    public static Stream<BlockFamily> getAllFamilies() {
        return MAP.values().stream();
    }

    public static Stream<BlockFamily> getAllNoVivicus() {
        return MAP.values().stream().filter(family -> family != VIVICUS);
    }

    public static Set<Block> getModelDatagenBlacklist(){
        Set<Block> set = new HashSet<>();
        set.add(AMBER_BLOCK.get());
        set.add(GARNET_BLOCK.get());
        return set;
    }

    public static Set<Block> getAllTranslucent(){
        Set<Block> set = new HashSet<>();
        set.add(AMBER.getBaseBlock());
        set.addAll(AMBER.getVariants().values());
        set.addAll(AMBER_MOSAICS.getVariants().values());
        set.addAll(AMBER_CHISELED.getVariants().values());

        set.add(GARNET.getBaseBlock());
        set.addAll(GARNET.getVariants().values());
        set.addAll(GARNET_MOSAICS.getVariants().values());
        set.addAll(GARNET_CHISELED.getVariants().values());
        return set;
    }

    public static Set<Block> getCutout(){
        Set<Block> set = new HashSet<>();

        MAP.values().forEach(family -> {
            set.add(family.get(BlockFamily.Variant.DOOR));
            set.add(family.get(BlockFamily.Variant.TRAPDOOR));
        });
        return set;
    }

}
