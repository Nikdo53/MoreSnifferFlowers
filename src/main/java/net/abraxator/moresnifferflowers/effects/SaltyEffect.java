package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class SaltyEffect extends MobEffect {
    public SaltyEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        Level level = livingEntity.level();
        if (livingEntity instanceof Player player){
            double reach = 3;
            LivingEntity entity = (LivingEntity) getClosestEntity(level, player, reach);

            if (entity != null){
                player.attack(entity);

                entity.addEffect(new MobEffectInstance(MSFEffects.SALTY, livingEntity.getEffect(MSFEffects.SALTY).getDuration() / 2 , amplifier));

            }


        }
        else if (livingEntity instanceof Mob mob && (livingEntity instanceof Enemy || livingEntity instanceof NeutralMob)){
            double reach = 1.5d;

            LivingEntity entity = (LivingEntity) getClosestEntity(level, mob, reach);

            if (entity != null && mob.getSensing().hasLineOfSight(entity)){

                mob.doHurtTarget(serverLevel, entity);

                entity.addEffect(new MobEffectInstance(MSFEffects.SALTY, (int) (livingEntity.getEffect(MSFEffects.SALTY).getDuration() / 1.5), amplifier));
            }

        }
        else livingEntity.removeEffect(MSFEffects.SALTY);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % (1200 / (amplifier + 1)) == 0;
    }

    public static @Nullable Entity getClosestEntity(Level level, LivingEntity mid, double range) {
        Vec3 start = new Vec3(mid.getEyePosition().x - range, mid.getEyePosition().y - range, mid.getEyePosition().z - range);
        Vec3 end = new Vec3(mid.getEyePosition().x + range, mid.getEyePosition().y + range, mid.getEyePosition().z + range);

        List<Entity> list = new java.util.ArrayList<>(level.getEntities(mid, new AABB(start, end)).stream()
                .filter(entity -> entity instanceof LivingEntity && ((LivingEntity) entity).canBeSeenAsEnemy())
                .toList());

        if (list.isEmpty()){
            return null;
        }

        list.sort(Comparator.comparing(entity -> {
            Vec3 pos1 = mid.position();
            Vec3 pos2 = entity.position();
            return pos1.distanceTo(pos2);
        }));

        return list.get(0);

    }
}
