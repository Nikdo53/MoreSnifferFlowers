package net.abraxator.moresnifferflowers.effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IMSFPotionEffect {
    default void onEffectEnd(LivingEntity player, MobEffectInstance instance) {
    }

    default void playerClientTick(Level level, Player player, int amplifier) {

    }
}
