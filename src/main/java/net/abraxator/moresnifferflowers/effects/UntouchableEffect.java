package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.networking.toClient.SyncUntouchablePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class UntouchableEffect extends MobEffect {
    public UntouchableEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            player.getData(MSFDataAttachments.UNTOUCHABLE.get()).tick(player, amplifier);
        }
        return true;
    }

    @Override
    public void onMobHurt(ServerLevel level, LivingEntity livingEntity, int amplifier, DamageSource source, float damage) {
        if (livingEntity instanceof ServerPlayer player) {
            player.getData(MSFDataAttachments.UNTOUCHABLE.get()).onAttacked();
            PacketDistributor.sendToPlayer(player, new SyncUntouchablePacket());
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
