package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SlipperyEffect extends MobEffect {
    public SlipperyEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        livingEntity.getData(MSFDataAttachments.SLIPPERY.get()).tick(livingEntity, amplifier);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
