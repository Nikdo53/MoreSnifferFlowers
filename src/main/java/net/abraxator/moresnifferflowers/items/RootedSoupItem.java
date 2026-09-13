package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.capability.NutritionCapability;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.components.RootedSoup;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.abraxator.moresnifferflowers.components.nutrition.NutritionType;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class RootedSoupItem extends Item {
    public RootedSoupItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return InteractionResult.CONSUME;
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        Player player = (Player) livingEntity;
        FoodData foodData = player.getFoodData();
        RootedSoup soup = stack.get(MSFDataComponents.ROOTED_SOUP);
        if (soup == null) return stack;

        int food = soup.food();
        float sat = soup.saturation();

        List<RootedSoup.RootedEffect> rootedEffects = stack.get(MSFDataComponents.ROOTED_EFFECTS);
        List<MobEffectInstance> effects = new ArrayList<>();

        NutritionCapability data = player.getData(MSFDataAttachments.NUTRITION);
        if (rootedEffects != null) {
            for (RootedSoup.RootedEffect effect : rootedEffects) {

                int id = effect.id();
                int dur = effect.length();
                int amp = effect.amplifier();
                boolean positive = effect.isPositive();

                data.unlockedEffects().add(NutritionCapability.idFromNutrition(NutritionType.byId(id), positive));

                Holder<MobEffect> mobEffect = NutritionType.getEffect(NutritionType.byId(id), positive);
                if (mobEffect != null) {
                    effects.add(new MobEffectInstance(mobEffect, dur, amp));
                }
            }
        }

        foodData.eat(food, sat);
        if (!level.isClientSide()) {
            for (MobEffectInstance effect : effects) {
                player.addEffect(effect);
            }
        }

        int uses = stack.getOrDefault(MSFDataComponents.USES, 1) - 1;

        if(uses <= 0) {
            return Items.BOWL.getDefaultInstance();
        }

        // Cookbook unlocking
        List<ItemStack> ingredients = stack.getOrDefault(MSFDataComponents.ROOTED_INGREDIENTS, new ArrayList<>());

        for (ItemStack ingredient : ingredients) {
            data.unlockedItems().add(ingredient.getItem());
        }


        player.setData(MSFDataAttachments.NUTRITION, data);
        stack.set(MSFDataComponents.USES, uses);
        return stack;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.EAT;
    }


    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrDefault(MSFDataComponents.USES, 0) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int input = stack.getOrDefault(MSFDataComponents.USES, 0);
        RootedSoup soup = stack.get(MSFDataComponents.ROOTED_SOUP);

        if (soup == null) return 0;

        int maxInput= soup.maxUses();

        return MSFColorHandler.barColorHelper(input, maxInput);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int input = stack.getOrDefault(MSFDataComponents.USES, 0);
        RootedSoup soup = stack.get(MSFDataComponents.ROOTED_SOUP);

        if (soup == null) return 0;

        int maxInput = soup.maxUses();

        return Math.round((float) input / maxInput * 13.0F);
    }
}
