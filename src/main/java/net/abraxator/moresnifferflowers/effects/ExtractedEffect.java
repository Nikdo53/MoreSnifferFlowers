package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExtractedEffect extends MobEffect {
    public ExtractedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    //TODO: make it uncurable

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        List<MobEffectInstance> activeEffects = new ArrayList<>(livingEntity.getActiveEffects());
        activeEffects = activeEffects.stream().filter(mobEffectInstance -> !mobEffectInstance.getEffect().is(MSFTags.EffectTags.EXTRACTION_BLACKLIST)).toList();

        livingEntity.setData(MSFDataAttachments.EXTRACTED_TICKS_REMAINING, Objects.requireNonNull(livingEntity.getEffect(MSFEffects.EXTRACTED)).getDuration());
        if (activeEffects.size() <= 1){
            livingEntity.removeEffect(MSFEffects.EXTRACTED);
            livingEntity.removeData(MSFDataAttachments.EXTRACTED_TICKS_REMAINING);
        };

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
