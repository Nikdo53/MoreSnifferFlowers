package net.abraxator.moresnifferflowers.blocks.corrupted;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.phys.AABB;

public class CuredGrassBlock extends GrassBlock {
    public CuredGrassBlock(Properties properties) {
        super(properties);
    }
    public static final MapCodec<CuredGrassBlock> CODEC = simpleCodec(CuredGrassBlock::new);

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if (d0 < 0.1 && !entity.isSteppingCarefully()) {
            double d1 = 0.8;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0, d1));
        }
    }

    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(
                    state, blockstate, Direction.UP, blockstate.getLightEmission(levelReader, blockpos)
            );
            return i < levelReader.getLightEngine().getMaxLightSection(); // todo: check
        }
    }


    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            if (!level.isAreaLoaded(pos, 1)) return;
            level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
        } else {
            if (!level.isAreaLoaded(pos, 3)) return;
            BlockState blockstate = this.defaultBlockState();

            CorruptionCapability.cure(level.getChunkAt(pos));

            for (int i = 0; i < 10; i++) {
                BlockPos blockpos = pos.offset(random.nextIntBetweenInclusive(-1,1), random.nextIntBetweenInclusive(-3,8), random.nextIntBetweenInclusive(-1,1));
                BlockState state1 = level.getBlockState(blockpos);
                if (state1.is(MSFBlocks.CORRUPTED_GRASS_BLOCK.get())) {
                    level.setBlockAndUpdate(
                            blockpos, blockstate.setValue(SNOWY, level.getBlockState(blockpos.above()).is(Blocks.SNOW))
                    );
                }

                if (state1.getOptionalValue(MSFStateProperties.NOT_CURED).isPresent()) {
                    if (!state1.getValue(MSFStateProperties.NOT_CORRUPTED)) {
                        level.setBlockAndUpdate(blockpos, state1.setValue(MSFStateProperties.NOT_CORRUPTED, true).setValue(MSFStateProperties.NOT_CURED, false));
                    }
                    if (!state1.getValue(MSFStateProperties.NOT_CURED)) {
                        level.setBlockAndUpdate(blockpos, state1.setValue(MSFStateProperties.NOT_CORRUPTED, true).setValue(MSFStateProperties.NOT_CURED, true));
                    }
                }
            }

            if (level.getBlockState(pos.above()).is(MSFBlocks.CORRUPTED_GRASS.get()))
                level.setBlock(pos.above(), Blocks.SHORT_GRASS.defaultBlockState(), 18);

            if (level.getBlockState(pos.above()).is(MSFBlocks.CORRUPTED_TALL_GRASS.get())) {
                level.setBlock(pos.above(), Blocks.TALL_GRASS.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 18);
                level.setBlock(pos.above(2), Blocks.TALL_GRASS.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 18);
            }

            var aabb = AABB.ofSize(pos.getCenter(), 4, 4, 4);
            boolean noCorruption = BlockPos.betweenClosedStream(aabb).allMatch(blockPos -> {
                if (level.getBlockState(blockPos).is(MSFBlocks.CORRUPTED_GRASS_BLOCK.get())) {
                    level.setBlockAndUpdate(
                            blockPos, blockstate.setValue(SNOWY, level.getBlockState(blockPos.above()).is(Blocks.SNOW))
                    );
                    return false;
                }
                return true;
            });

            if (noCorruption)
                level.setBlockAndUpdate(
                        pos, Blocks.GRASS_BLOCK.defaultBlockState().setValue(SNOWY, level.getBlockState(pos.above()).is(Blocks.SNOW))
                );
        }
    }

}
