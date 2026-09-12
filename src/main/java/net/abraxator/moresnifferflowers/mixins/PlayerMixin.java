package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(FoodProperties.class)
public abstract class PlayerMixin {

    @Shadow
    public abstract int nutrition();

    @Shadow
    public abstract float saturation();

    @Inject(method = "onConsume", at = @At(value = "HEAD"))
    public void blandEffectInject(Level level, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo ci){
        Player player = (Player)(Object)this;

        if (player.hasEffect(MSFEffects.BLAND)) {
            int amplifier = Objects.requireNonNull(player.getEffect(MSFEffects.BLAND)).getAmplifier() + 1;
            float division =  1f + amplifier / 2f;
            float hungerChance = 0.2f + amplifier / 10f;

            if (!level.isClientSide() &&  level.getRandom().nextFloat() < hungerChance){
                player.addEffect( new MobEffectInstance(MobEffects.HUNGER, Math.round(10 * division) * 20, amplifier - 1));
            }

            //Original logic from FoodData cuz fabric SUCKS
            int foodValue = Math.round(nutrition() / division);
            float satValue = saturation() / division;
            player.getFoodData().eat( -(nutrition() - foodValue), -(saturation() - satValue));

        }
    }
}
