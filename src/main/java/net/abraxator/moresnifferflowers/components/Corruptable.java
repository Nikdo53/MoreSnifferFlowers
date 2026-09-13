package net.abraxator.moresnifferflowers.components;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFDataMaps;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Corruptable(List<Pair<Block, Integer>> list) {
    public static final Codec<Pair<Block, Integer>> PAIR_CODEC = Codec.pair(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").codec(),
            Codec.INT.fieldOf("weight").codec()
    );
    public static final Codec<Corruptable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PAIR_CODEC.listOf().fieldOf("value").forGetter(Corruptable::list)
    ).apply(instance, Corruptable::new));

    public static final Map<Block, Block> HARDCODED_BLOCK = Util.make(Maps.newHashMap(), map -> {
        map.put(MSFBlocks.DYESPRIA_PLANT.get(), MSFBlocks.DYESCRAPIA_PLANT.get());
        map.put(MSFBlocks.DAWNBERRY_VINE.get(), MSFBlocks.GLOOMBERRY_VINE.get());
        map.put(MSFBlocks.BONMEELIA.get(), MSFBlocks.BONWILTIA.get());
        map.put(MSFBlocks.BONDRIPIA.get(), MSFBlocks.ACIDRIPIA.get());
        map.put(MSFBlocks.AMBUSH_BOTTOM.get(), MSFBlocks.GARBUSH_BOTTOM.get());
        map.put(MSFBlocks.AMBUSH_TOP.get(), MSFBlocks.GARBUSH_TOP.get());
        map.put(MSFBlocks.SALTEMONE.get(), MSFBlocks.SOURLEMONE.get());
        map.put(MSFBlocks.CAULORFLOWER.get(), MSFBlocks.PATTERNFLOWER.get());
        map.put(MSFBlocks.TORCHFLOWER_AFLAME.get(), MSFBlocks.TORCHEWFLOWER.get());
        map.put(Blocks.TORCHFLOWER, MSFBlocks.TORCHEWFLOWER.get());
    });
    
    public Corruptable(Block block) {
        this(List.of(Pair.of(block, 100)));
    }

    public static Optional<Block> getCorruptedBlock(Block block, Level level) {
        return getCorruptedBlock(block, level.getRandom());
    }


    @SuppressWarnings("deprecation")
    public static Optional<Block> getCorruptedBlock(Block block, RandomSource random) {
        Holder<Block> holder =  block.builtInRegistryHolder();
        Corruptable corruptable = holder.getData(MSFDataMaps.CORRUPTABLE);
        Block hardcodedBlock = HARDCODED_BLOCK.getOrDefault(block, Blocks.AIR);
        
        if(hardcodedBlock != Blocks.AIR) {
            return Optional.of(hardcodedBlock);
        }
        
        if (corruptable != null) {
            List<Pair<Block, Integer>> corruptableList = corruptable.list();
            int totalWeight = corruptableList.stream().mapToInt(Pair::getSecond).sum();
            int randomValue = random.nextInt(totalWeight);
            int cumulativeWeight = 0;
            
            for (Pair<Block, Integer> pair : corruptableList) {
                cumulativeWeight += pair.getSecond();
                if(randomValue < cumulativeWeight) {
                    return Optional.of(pair.getFirst());
                }
            }
        }

        return Optional.empty();
    }

    public static boolean canBeCorrupted(Block block, Level level) {
        return canBeCorrupted(block, level.getRandom());
    }


    public static boolean canBeCorrupted(Block block, RandomSource random) {
        if(block == null) {
            return false;
        }
        
        return getCorruptedBlock(block, random).isPresent();
    }
}
