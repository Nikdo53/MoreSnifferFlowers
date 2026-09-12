package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.init.MSFEntityTypes;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public class DragonflyProjectile extends ThrowableItemProjectile {
    public DragonflyProjectile(EntityType<? extends DragonflyProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public DragonflyProjectile(Level level, Player player, ItemStack itemStack) {
        super(MSFEntityTypes.DRAGONFLY.get(), player, level, itemStack);
        this.setOwner(player);
    }
    
    public DragonflyProjectile(Level level) {
        super(MSFEntityTypes.DRAGONFLY.get(), level);
    }

    @Override
    protected Item getDefaultItem() {
        return MSFItems.DRAGONFLY.get();
    }

    @Override
    protected void onHit(HitResult result) {
        if(level() instanceof ServerLevel serverLevel) {
            var particle = new ItemParticleOption(ParticleTypes.ITEM, MSFItems.DRAGONFLY.get());
            serverLevel.sendParticles(particle, getX(), getY(), getZ(), 10, Mth.nextDouble(random, 0, 0.3), Mth.nextDouble(random, 0, 0.3), Mth.nextDouble(random, 0, 0.3), 0);
        }
        super.onHit(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(result.getEntity() instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 140, 2));
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0.3F);
        }
        
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        discard();
    }
}
