package net.abraxator.moresnifferflowers.mixins;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AgeableMob.class)
public class AgeableMobMixin extends PathfinderMob {
    protected AgeableMobMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "aiStep", at = @At(value = "STORE"), name = "age")
    public int moresniffeflowers$aiStep(int x) {
        if(this.getType() == EntityType.SNIFFER) {
            if (x < 0) {
                return x + 1;
            } else if (x > 0) {
                return x;
            }
        } 
        
        return x;
    }
}
