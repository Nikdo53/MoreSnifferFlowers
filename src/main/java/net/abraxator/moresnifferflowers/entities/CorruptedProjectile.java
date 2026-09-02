package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.components.Corruptable;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class CorruptedProjectile extends ThrowableItemProjectile {
    public CorruptedProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public CorruptedProjectile(Level level, LivingEntity pShooter) {
        super(MSFEntityTypes.CORRUPTED_SLIME_BALL.get(), pShooter, level);
    }

    public CorruptedProjectile(Level level) {
        super(MSFEntityTypes.CORRUPTED_SLIME_BALL.get(), level);
    }

    @Override
    protected Item getDefaultItem() {
        return MSFItems.CORRUPTED_SLIME_BALL.get();
    }

    private ParticleOptions getParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        for (int i = 0; i < 16; i++) {
            this.level().broadcastEntityEvent(this, (byte) 3);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 3) {
            this.level().addParticle(new DustParticleOptions(Vec3.fromRGB24(0x36283D).toVector3f(), 1.0F), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if(entity instanceof LivingEntity livingEntity) {
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
          if (!level().isClientSide)
              livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
        }
        this.discard();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        var pos = result.getBlockPos();
        var posRelative = result.getBlockPos().relative(result.getDirection());
        var posRelativeBelow = result.getBlockPos().relative(result.getDirection()).below();

        var state = this.level().getBlockState(pos);
        var stateRelative = this.level().getBlockState(posRelative);
        var stateRelativeBelow = this.level().getBlockState(result.getBlockPos().relative(result.getDirection()).below());

        if (this.level().getBlockState(pos).is(MSFTags.BlockTags.NO_CORRUPTED_SLIME_COLLISION)) return;

        if(checkState(this.level().getBlockState(result.getBlockPos()))) {
            var layer = state.getValue(MSFStateProperties.LAYER);
            this.level().setBlockAndUpdate(
                    pos,
                    MSFBlocks.CORRUPTED_SLIME_LAYER.get().defaultBlockState().setValue(MSFStateProperties.LAYER, layer + 1));

        } else {
            transformBlock(this.level(), pos);

            if (checkState(this.level().getBlockState(result.getBlockPos().relative(result.getDirection())))) {
                var layerRelative = stateRelative.getValue(MSFStateProperties.LAYER);
                this.level().setBlockAndUpdate(
                        posRelative,
                        MSFBlocks.CORRUPTED_SLIME_LAYER.get().defaultBlockState().setValue(MSFStateProperties.LAYER, layerRelative + 1));
            }

            if (stateRelative.is(net.minecraft.world.level.block.Blocks.AIR) || stateRelative.is(net.minecraft.tags.BlockTags.FIRE) || (stateRelative.canBeReplaced() && !stateRelative.liquid())) {

                    if(result.getDirection() == Direction.UP && !state.is(net.minecraft.world.level.block.Blocks.AIR)) {
                        this.level().setBlockAndUpdate(
                                result.getBlockPos().relative(result.getDirection()),
                                MSFBlocks.CORRUPTED_SLIME_LAYER.get().defaultBlockState().setValue(MSFStateProperties.LAYER, 1));
                        this.discard();
                    } else {
                        CorruptedProjectile projectile = new CorruptedProjectile(this.level());
                        projectile.setPos(this.getX(),this.getY(), this.getZ());
                        projectile.setRot(this.getYRot(), Mth.PI / 90.0F);
                        this.level().addFreshEntity(projectile);
                    }

            } else {
                if(Corruptable.canBeCorrupted(stateRelative.getBlock(), random)){
                    transformBlock(this.level(), posRelative);
                }
            }
        }
        this.discard();
    }

    private boolean transformBlock(Level level, BlockPos blockPos) {
        var pos = BlockPos.findClosestMatch(blockPos, 1, 1, blockPos1 -> Corruptable.canBeCorrupted(level.getBlockState(blockPos1).getBlock(), random));
        var state = level.getBlockState(blockPos);

        if(Corruptable.canBeCorrupted(state.getBlock(), random)) {
            Optional<Block> optional = Corruptable.getCorruptedBlock(state.getBlock(), this.random);
            optional.ifPresent(block -> {
                if (level.getBlockState(blockPos).getBlock() instanceof net.abraxator.moresnifferflowers.blocks.Corruptable corruptable && level instanceof ServerLevel serverLevel) {
                    corruptable.onCorrupt(serverLevel, blockPos, level.getBlockState(blockPos), block);
                } else {
                    level.setBlockAndUpdate(blockPos, block.withPropertiesOf(state));

                    CorruptionCapability.onCorruptionSource(level, blockPos);

                }

                if (level.getNearestPlayer(this, 15) instanceof ServerPlayer serverPlayer) {
                    MSFAdvancementCritters.CORRUPTED_BLOCK.get().trigger(serverPlayer);
                }

                level.addParticle(
                        new DustParticleOptions(Vec3.fromRGB24(0x0443248).toVector3f(), 1.0F),
                        blockPos.getX() + level.random.nextDouble(), blockPos.getY() + level.random.nextDouble(), blockPos.getZ() + level.random.nextDouble(),
                        0.0D, 0.0D, 0.0D);
            });

            return true;
        }

        return false;
    }

    private static boolean checkState(BlockState state) {
        return state.is(MSFBlocks.CORRUPTED_SLIME_LAYER) && state.getValue(MSFStateProperties.LAYER) != 8;
    }
}
