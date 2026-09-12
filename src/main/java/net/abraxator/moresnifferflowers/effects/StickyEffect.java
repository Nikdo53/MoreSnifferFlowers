package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class StickyEffect extends MobEffect {
    public StickyEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        Level level = livingEntity.level();
        double pullRange = 8.0D;
        List<ItemEntity> nearbyItems = level.getEntitiesOfClass(ItemEntity.class, livingEntity.getBoundingBox().inflate(pullRange), item -> !item.hasPickUpDelay());
        for (ItemEntity item : nearbyItems) {
            pullItemTowards(livingEntity, item);
        }

        if (!level.isClientSide() && livingEntity instanceof Player player) {

            BlockPos pos = livingEntity.getOnPos();
            BlockState state = level.getBlockState(pos);
            Vec3 vec3 = pos.getCenter();

            boolean isRunning = player.isSprinting();

            int delay = 600 - (amplifier * 50);
            if (delay < 1) delay = 1;

            if (isRunning && level.getGameTime() % delay == 0 && state.is(MSFTags.BlockTags.STICKABLE)) {
                level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 0.5F + level.getRandom().nextFloat() * 0.8F);
                ItemEntity itemEntity = new ItemEntity(level, vec3.x, vec3.y + 0.6F, vec3.z, state.getBlock().asItem().getDefaultInstance());
                level.addFreshEntity(itemEntity);
                if (level.getRandom().nextFloat() < 0.3f)
                    livingEntity.addEffect(new MobEffectInstance(MSFEffects.GLUED, 40 * amplifier, 0));
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    // ChatGPT code
    private static void pullItemTowards(LivingEntity livingEntity, ItemEntity item) {
        Vec3 livingEntityPos = livingEntity.position().add(0, 1, 0); // Aim for livingEntity's chest, not feet
        Vec3 itemPos = item.position();
        Vec3 direction = livingEntityPos.subtract(itemPos);


        // Don't do anything if very close already
        if (direction.lengthSqr() < 0.5) {
            return;
        }

        direction = direction.normalize().scale(0.05); // SMALL pull per tick

        Vec3 currentVelocity = item.getDeltaMovement();

        // Slightly steer the velocity toward the player
        Vec3 newVelocity = currentVelocity.add(direction).scale(0.95); // Dampen a bit

        item.setDeltaMovement(newVelocity);
    }
}
