package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.client.gui.slot.HardenedMouthSlot;
import net.abraxator.moresnifferflowers.components.BetterNonNullList;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class HardenedMouthEffect extends MobEffect implements IMSFPotionEffect {
    public HardenedMouthEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        if (!(livingEntity instanceof Player player)) return false;
        if (player.level().isClientSide() || !player.hasEffect(MSFEffects.HARDENED_MOUTH)) return true;

        BetterNonNullList<ItemStack> data = player.getData(MSFDataAttachments.HARDENED_MOUTH_SLOTS.get());
        int cooldown = player.getData(MSFDataAttachments.HARDENED_MOUTH_COOLDOWN.get());
        ItemStack input = data.get(0);
        ItemStack output = data.get(1);


        Optional<ItemStack> smeltingResult = getSmeltingResult(serverLevel, input);
        if (smeltingResult.isEmpty() || (!smeltingResult.get().is(output.getItem()) && !output.isEmpty())){
            if (cooldown < getMaxCooldown(amplifier))
                setMaxCooldown(player, amplifier);
            return true;
        }

        if (cooldown > 0) {
            cooldown--;
            player.setData(MSFDataAttachments.HARDENED_MOUTH_COOLDOWN.get(), cooldown);
            player.setData(MSFDataAttachments.HARDENED_MOUTH_SLOTS.get(), data);
            return true;
        }

        smeltingResult.ifPresent(result -> {

            if (output.isEmpty() || (ItemStack.isSameItemSameComponents(output, result) && output.getCount() < output.getMaxStackSize())) {
                input.shrink(1);

                if (output.isEmpty()) {
                    player.setData(MSFDataAttachments.HARDENED_MOUTH_SLOTS.get(), data);
                    data.set(1, result.copy());
                } else {
                    output.grow(1);
                    data.set(1, output);
                }

                data.set(0, input);

            }
        });


        player.setData(MSFDataAttachments.HARDENED_MOUTH_COOLDOWN.get(), getMaxCooldown(amplifier));
        player.setData(MSFDataAttachments.HARDENED_MOUTH_SLOTS.get(), data);

        return true;
    }

    @Override
    public void onEffectEnd(LivingEntity entity, MobEffectInstance instance) {
        if (!(entity instanceof Player player)) return;
        player.getData(MSFDataAttachments.HARDENED_MOUTH_SLOTS.get()).forEach(itemStack -> {
            if (HardenedMouthSlot.moveToPlayerInventory(player.inventoryMenu, itemStack)) return;
            if (itemStack.isEmpty()) return;
            player.drop(itemStack, true);
        });

        player.removeData(MSFDataAttachments.HARDENED_MOUTH_SLOTS);
        player.removeData(MSFDataAttachments.HARDENED_MOUTH_COOLDOWN);

    }

    public void setMaxCooldown(Player player, int amplifier) {
        player.setData(MSFDataAttachments.HARDENED_MOUTH_COOLDOWN.get(), getMaxCooldown(amplifier));
    }

    public static int getMaxCooldown(int amplifier) {
        return Math.max(1, 80 - amplifier * 10);
    }

    public Optional<ItemStack> getSmeltingResult(ServerLevel level, ItemStack input) {
        return level.recipeAccess()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), level)
                .map(recipe -> recipe.value().assemble(new SingleRecipeInput(input)));
    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
