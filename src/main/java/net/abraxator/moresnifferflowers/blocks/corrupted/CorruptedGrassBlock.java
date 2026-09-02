package net.abraxator.moresnifferflowers.blocks.corrupted;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
import net.abraxator.moresnifferflowers.networking.toClient.CorruptionParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.references.BlockIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.lighting.LightEngine;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public class CorruptedGrassBlock extends GrassBlock implements BonemealableBlock {
    public static final MapCodec<GrassBlock> CODEC = simpleCodec(GrassBlock::new);

    @Override
    public MapCodec<GrassBlock> codec() {
        return CODEC;
    }

    public CorruptedGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MSFStateProperties.CROWDED, false));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_256229_, BlockPos p_256432_, BlockState p_255677_) {
        return p_256229_.getBlockState(p_256432_.above()).isAir();
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if (d0 < 0.1 && !entity.isSteppingCarefully()) {
            double d1 = 0.8;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0, d1));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MSFStateProperties.CROWDED);
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

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(blockpos).is(FluidTags.WATER);
    }

/* //TODO: be?
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        LevelChunk chunk = level.getChunkAt(pos);

        CorruptionCapability cap = chunk.getData(MSFDataAttachments.CHUNK_CORRUPTION);
        if (cap.count > 0) cap.count--;
    }
*/

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            level.setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState());
        } else {
            if (!level.isAreaLoaded(pos, 3)) return;
            if (state.getValue(MSFStateProperties.CROWDED)) return;
            if (level.getMaxLocalRawBrightness(pos.above()) <=6 && random.nextDouble() < 0.2D *  MSFServerConfig.CORRUPTION_SPREAD_SPEED.get()) {
                BlockState blockstate = this.defaultBlockState();
                boolean spreadSuccess = false;

                for (int i = 0; i < 4 && !spreadSuccess; i++) {
                    spreadSuccess = spread(level, pos, random, blockstate);
                }

                placeCorruptedLeaves(level, pos, random);
                placeTallGrass(level, pos);
            }
        }
    }

    private static void placeCorruptedLeaves(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos blockPos1 = pos.above(random.nextInt(6));
        BlockState state2 = level.getBlockState(blockPos1);

        if (state2.getOptionalValue(MSFStateProperties.NOT_CORRUPTED).isPresent() && state2.getValue(MSFStateProperties.NOT_CORRUPTED)){
            level.setBlock(blockPos1, state2.setValue(MSFStateProperties.NOT_CORRUPTED, false), 3);
        }
    }

    private static boolean spread(ServerLevel level, BlockPos pos, RandomSource random, BlockState blockstate) {
        BlockPos pos1 = pos.offset(random.nextIntBetweenInclusive(-2,2), random.nextIntBetweenInclusive(-2,2), random.nextIntBetweenInclusive(-2,2));
        BlockState state1 = level.getBlockState(pos1);

        boolean canSpread = state1.is(BlockTags.DIRT) && canPropagate(blockstate, level, pos1) && !state1.is(MSFBlocks.CURED_GRASS_BLOCK.get()) && !state1.is(MSFBlocks.CORRUPTED_GRASS_BLOCK.get());

        if (canSpread) {

            if (level.isClientSide()) return false;

            if (checkChunks(level, pos, random, blockstate, pos1)) return false;

            level.setBlockAndUpdate(pos1, blockstate.setValue(SNOWY, level.getBlockState(pos1.above()).is(Blocks.SNOW)));

            BlockPos posAbove = pos1.above();
            if (random.nextFloat() < 0.10F && level.getBlockState(posAbove).isAir()){
                level.setBlock(posAbove, MSFBlocks.CORRUPTED_WART.get().defaultBlockState(), 3);
            }

            return true;
        }

        return false;
    }

    private static boolean checkChunks(ServerLevel level, BlockPos pos, RandomSource random, BlockState blockstate, BlockPos pos1) {
        LevelChunk chunkNew = level.getChunkAt(pos1);
        LevelChunk chunkOriginal = level.getChunkAt(pos);

        CorruptionCapability capNew = CorruptionCapability.get(chunkNew);
        CorruptionCapability capOriginal = CorruptionCapability.get(chunkOriginal);

        boolean differentChunk = CorruptionCapability.areDifferentChunks(level, pos1, pos);

        boolean isSourceOriginal = capOriginal.isSource;
        boolean isSourceNew = capNew.isSource;
        boolean isNeighborNew = capNew.isNeighbor;

        int count = capNew.count;
        int resistance = capNew.resistance;

        if (differentChunk) {
           if (isSourceOriginal) {
               chunkNew.getData(MSFDataAttachments.CHUNK_CORRUPTION).isNeighbor = true;

           } else if (isNeighborNew) {
               return true;
           }

        }

        if (!isNeighborNew && !isSourceNew) {
            setCrowded(level, pos, blockstate, resistance, chunkOriginal);
            return true;
        }

        if (isNeighborNew && !isSourceNew) {
            int maxResistance = CorruptionCapability.MAX_RESISTANCE;
            int maxCorruption = CorruptionCapability.MAX_CORRUPTION;

            double chance = 1 - (double) count / Math.max(maxCorruption - resistance*(maxCorruption / maxResistance), 1);

            if (chance <= 0.01) {
                setCrowded(level, pos, blockstate, resistance, chunkOriginal);
                return true;
            }

            if (random.nextDouble() > chance) {
                return true;
            }
        }

        capNew.count++;
        return false;
    }

    private static void setCrowded(ServerLevel level, BlockPos pos, BlockState blockstate, int resistance, LevelChunk chunkOriginal) {
        level.setBlock(pos, blockstate.setValue(MSFStateProperties.CROWDED, true), 3);

        boolean isPositive = resistance > 0;
        PacketDistributor.sendToAllPlayers(new CorruptionParticlePacket(pos, isPositive, false));

        if (isPositive){
            CorruptionCapability.sendFlowerParticles(chunkOriginal);
        }
    }

    private static void placeTallGrass(ServerLevel level, BlockPos pos) {
        if (level.getBlockState(pos.above()).is(Blocks.SHORT_GRASS))
            level.setBlock(pos.above(), MSFBlocks.CORRUPTED_GRASS.get().defaultBlockState(), 18);

        if (level.getBlockState(pos.above()).is(Blocks.TALL_GRASS)) {
            level.setBlock(pos.above(), MSFBlocks.CORRUPTED_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 18);
            level.setBlock(pos.above(2), MSFBlocks.CORRUPTED_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 18);
        }
    }
}
