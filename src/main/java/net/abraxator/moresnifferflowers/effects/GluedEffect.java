package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GluedEffect extends MobEffect {
    public GluedEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static final ContextKey<Boolean> IS_GLUED_KEY = new ContextKey<>(MoreSnifferFlowers.loc("is_glued_key"));

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        double y = Math.min(livingEntity.getDeltaMovement().y, 0);
        livingEntity.setDeltaMovement(new Vec3(0, y ,0));
        livingEntity.setJumping(false);

        if (livingEntity.level().getGameTime() % 41 == 0) {
            setAndSync(livingEntity, true, false);
        }
        return true;
    }

    public static void setAndSync(LivingEntity entity, boolean isGlued, boolean playSound) {
        Level level = entity.level();
        if (playSound) playSound(level, entity);

        entity.setData(MSFDataAttachments.IS_GLUED, isGlued);
    }

    public static void playSound(Level level, Entity entity){
        level.playSound(null, entity.getOnPos(), SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.PLAYERS, 5.0F, 0.02F + level.getRandom().nextFloat() * 0.01F);
    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

}
