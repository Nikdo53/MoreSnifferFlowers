package net.abraxator.moresnifferflowers.compat.jei.rebrewing;

import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JeiRebrewingRecipe(ItemStack extractedPotion, ItemStack rebrewedPotion, ItemStack ingredient) {
    public static List<JeiRebrewingRecipe> createRecipes() {
        List<JeiRebrewingRecipe> ret = new ArrayList<>();
        List<ItemStack> ingredients = List.of(
                MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_AMPLIFIER.get()).getDefaultInstance(),
                MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_LENGTH.get()).getDefaultInstance(),
                MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_SPLASH.get()).getDefaultInstance(),
                MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_LINGERING.get()).getDefaultInstance()
        );

        for (ItemStack item : ingredients) {
            BuiltInRegistries.MOB_EFFECT.stream().forEach(effect -> {
                int duration = item.is(MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_LENGTH.get())) ? 12000 : 6000;
                int amplifier = item.is(MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_AMPLIFIER.get())) ? 2 : 1;

                List<MobEffectInstance> extractedEffect = List.of(new MobEffectInstance(Holder.direct(effect), 1200, 0));
                List<MobEffectInstance> rebrewedEffect = List.of(new MobEffectInstance(Holder.direct(effect), 1200 + duration, amplifier));
                PotionContents extractedPotionContents = new PotionContents(Optional.of(Potions.WATER), Optional.of(PotionContents.getColorOptional(extractedEffect).getAsInt()), extractedEffect, Optional.empty());
                PotionContents rebrewedPotionContents = new PotionContents(Optional.of(Potions.WATER), Optional.of(PotionContents.getColorOptional(rebrewedEffect).getAsInt()), rebrewedEffect, Optional.empty());

                ItemStack extractedPotion = MSFItems.EXTRACTED_BOTTLE.get().getDefaultInstance();
                var rebrewedPotion = item.is(MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_SPLASH.get())) ? MSFItems.REBREWED_SPLASH_POTION.get().getDefaultInstance() :
                        item.is(MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_LINGERING.get())) ? MSFItems.REBREWED_LINGERING_POTION.get().getDefaultInstance() :
                                MSFItems.REBREWED_POTION.get().getDefaultInstance();

                extractedPotion.set(DataComponents.POTION_CONTENTS, extractedPotionContents);
                rebrewedPotion.set(DataComponents.POTION_CONTENTS, rebrewedPotionContents);

                ret.add(new JeiRebrewingRecipe(extractedPotion, rebrewedPotion, item));
            });
        }
        return ret;
    }
}
