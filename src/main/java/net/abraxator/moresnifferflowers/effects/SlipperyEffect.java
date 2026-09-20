package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SlipperyEffect extends MobEffect implements IMSFPotionEffect {
    public static final ContextKey<Boolean> IS_FALLEN_KEY = new ContextKey<>(MoreSnifferFlowers.loc("is_fallen_key"));

    public SlipperyEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        livingEntity.getData(MSFDataAttachments.SLIPPERY.get()).tick(livingEntity, amplifier);
        return true;
    }

    @Override
    public void playerClientTick(Level level, Player player, int amplifier) {
        player.getData(MSFDataAttachments.SLIPPERY.get()).tick(player, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
