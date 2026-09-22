package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BottleOfExtractionItem extends Item {
    public BottleOfExtractionItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        if (livingEntity instanceof Player player && !level.isClientSide()) {

            if (livingEntity instanceof ServerPlayer serverplayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
                serverplayer.awardStat(Stats.ITEM_USED.get(this));
            }

            if ((player.hasEffect(MSFEffects.EXTRACTED) || player.getData(MSFDataAttachments.EXTRACTED_TICKS_REMAINING) > 10) && !player.isCreative()) {
                doCheaterEasterEgg(level, player);
                ItemStack itemStack = new ItemStack(Items.POISONOUS_POTATO);
                itemStack.set(DataComponents.CUSTOM_NAME, Component.literal("Cheater Potato"));
                return itemStack;
            }

            List<MobEffectInstance> activeEffects = new ArrayList<>(player.getActiveEffects());
            activeEffects = activeEffects.stream().filter(mobEffectInstance -> !mobEffectInstance.getEffect().is(MSFTags.MSFEffectTags.EXTRACTION_BLACKLIST)).toList();

            stack = initPotion(activeEffects);

            activeEffects.forEach(effectInstance -> player.removeEffect(effectInstance.getEffect()));
        }
        return stack;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (!canExtract(level, player)) {
            return InteractionResult.PASS;
        } else {
            player.startUsingItem(usedHand);
            return InteractionResult.CONSUME;
        }
    }

    private ItemStack initPotion(List<MobEffectInstance> activeEffects) {
        var stack = MSFItems.EXTRACTED_BOTTLE.get().getDefaultInstance();
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(PotionContents.getColorOptional(activeEffects).getAsInt()), new ArrayList<>(activeEffects), Optional.empty()));
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.DRINK;
    }

    private boolean canExtract(Level level, Player player) {
        List<MobEffectInstance> activeEffects = new ArrayList<>(player.getActiveEffects());
        activeEffects = activeEffects.stream().filter(mobEffectInstance -> !mobEffectInstance.getEffect().is(MSFTags.MSFEffectTags.EXTRACTION_BLACKLIST)).toList();

        return !level.isClientSide() && !activeEffects.isEmpty() && !player.hasEffect(MSFEffects.EXTRACTED);
    }

    private static void doCheaterEasterEgg(Level level, Player player) {
        player.removeAllEffects();
        player.setData(MSFDataAttachments.EXTRACTED_TICKS_REMAINING, 0);
        player.setAbsorptionAmount(0);
        player.setHealth(0.1F);
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2));
        player.setSwimming(true);
        player.setJumping(true);
        player.setXRot(0F);
        player.setYHeadRot(0f);
        level.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 1.0F);
        level.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 0.8F);
        level.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 1.3F);
        level.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 0.8F);
        level.playSound(null, player, SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.4F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatableWithFallback("tooltip.bottle_of_extraction.usage", "Drink to extract all effects into single potion").withStyle(ChatFormatting.GOLD));
    }
}
