package net.abraxator.moresnifferflowers.blocks.corrupted;

import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
  
public class CorruptedSlimeLayerBlock extends SnowLayerBlock {
    public CorruptedSlimeLayerBlock(Properties p_56585_) {
        super(p_56585_);
    }
    
    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, Math.max((state.getValue(LAYERS) - 3) * 2, 0), 16);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean pIsMoving) {
        if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY()) {
            spawnProjectile(state, level, pos);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
        //return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY() && level instanceof LevelAccessor levelAccessor) {
            spawnProjectile(state, levelAccessor, pos);
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);

    }

    private static void spawnProjectile(BlockState state, LevelAccessor level, BlockPos pos) {
        for (int i = 0; i < state.getValue(LAYERS); i++) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            CorruptedProjectile projectile = new CorruptedProjectile((Level) level);
            projectile.setPos(pos.below().getCenter());
            projectile.setXRot(Mth.PI / 90.0F);
            level.addFreshEntity(projectile);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double pFallDistance) {
        entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
        showParticles(entity, 10);

        if (entity.causeFallDamage(pFallDistance, 0.2F, level.damageSources().fall())) {
            entity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if (d0 < 0.1 && !entity.isSteppingCarefully()) {
            double d1 = (double) 1 / (state.getValue(LAYERS)+1) + d0 * 0.2;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0, d1));
        }
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    @SuppressWarnings("deprecation")
    public static boolean isFree(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int layers = state.getValue(LAYERS);
        if(layers == 1) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(LAYERS, layers - 1), 3);
        }
    }
    
    private void showParticles(Entity entity, int pParticleCount) {
        if (entity.level().isClientSide()) {
            for (int i = 0; i < pParticleCount; i++) {
                entity.level()
                        .addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.defaultBlockState()), entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return (adjacentBlockState.is(this) && (adjacentBlockState.getValue(SnowLayerBlock.LAYERS)>=(state.getValue(SnowLayerBlock.LAYERS)) || side.getAxis().equals(Direction.Axis.Y))) || super.skipRendering(state, adjacentBlockState, side);
    }
}
