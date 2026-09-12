package net.abraxator.moresnifferflowers.datagen.advancement;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.EffectsChangedTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class MSFAdvancementGenerator implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
        HolderLookup<EntityType<?>> entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE);
        HolderLookup<Item> items = registries.lookupOrThrow(Registries.ITEM);
        HolderLookup<Block> blocks = registries.lookupOrThrow(Registries.BLOCK);

        var root = Advancement.Builder.advancement()
                .display(
                        net.minecraft.world.item.Items.SNIFFER_EGG,
                        Component.translatable("advancements.more_sniffer_flowers.any_seed"),
                        Component.translatable("advancements.more_sniffer_flowers.any_seed.desc"),
                        MoreSnifferFlowers.loc("textures/gui/grass_block_bg.png"),
                        AdvancementType.TASK,
                        true,
                        false,
                        false)
                .addCriterion("has_advancement", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SNIFFER_EGG))
                .save(consumer, MoreSnifferFlowers.loc("root").toString());

        var dyespria_plant = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        MSFItems.DYESPRIA.get(),
                        Component.translatable("advancements.more_sniffer_flowers.dyespria_plant"),
                        Component.translatable("advancements.more_sniffer_flowers.dyespria_plant.desc"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("planted_dyespria_plant", MSFAdvancementCritters.SimpleAdvancementTrigger.TriggerInstance.placedDyespriaPlant())
                .save(consumer, MoreSnifferFlowers.loc("dyespria_plant").toString());

        Advancement.Builder.advancement()
                .parent(dyespria_plant)
                .display(
                        MSFBlocks.CAULORFLOWER.get(),
                        Component.translatable("advancements.more_sniffer_flowers.dyespria"),
                        Component.translatable("advancements.more_sniffer_flowers.dyespria.desc"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("used_dyespria", MSFAdvancementCritters.SimpleAdvancementTrigger.TriggerInstance.usedDyespria())
                .save(consumer, MoreSnifferFlowers.loc("dyespria").toString());

        var bonmeel = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        MSFItems.JAR_OF_BONMEEL.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.bonmeel", "Let It Grow!"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.bonmeel.desc", "Enlarge your crops using the magic of Bonmeel"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("used_bonmeel", MSFAdvancementCritters.SimpleAdvancementTrigger.TriggerInstance.usedBonmeel())
                .save(consumer, MoreSnifferFlowers.loc("bonmeel").toString());

       var cropressing = Advancement.Builder.advancement()
                .parent(bonmeel)
                .display(
                        MSFItems.CROPRESSED_BEETROOT.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.cropressor", "Compressing with extra steps"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.cropressor.desc", "Cropress any crop"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_cropressed_crop", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, MSFTags.ItemTags.CROPRESSED_CROPS).build()))
                .save(consumer, MoreSnifferFlowers.loc("cropressor").toString());

       var cauldron = Advancement.Builder.advancement()
                .parent(cropressing)
                .display(
                        MSFBlocks.BEROOT_CAULDRON.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.beroot_cauldron", "Glooby, and never Soupy"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.beroot_cauldron.desc", "Craft the Beroot Cauldron"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_beroot_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(MSFBlocks.BEROOT_CAULDRON.get()))
                .save(consumer, MoreSnifferFlowers.loc("beroot_cauldron").toString());

        ItemStack rootedSoup = MSFItems.ROOTED_SOUP.get().getDefaultInstance();
        rootedSoup.set(MSFDataComponents.COLOR, 0xFFBC51);

        Advancement.Builder.advancement()
                .parent(cauldron)
                .display(
                        ItemStackTemplate.fromNonEmptyStack(rootedSoup),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.positive_soup", "Michelin Star Chef"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.positive_soup.desc", "Have 4 positive soup effects at the same time"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("has_positive_soup_effects", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MSFEffects.GLUING_TOUCH).and(MSFEffects.UNTOUCHABLE).and(MSFEffects.COMBO_MEAL).and(MSFEffects.HARDENED_MOUTH)))
                .rewards(AdvancementRewards.Builder.experience(100))
                .save(consumer, MoreSnifferFlowers.loc("positive_soup").toString());

        Advancement.Builder.advancement()
                .parent(cauldron)
                .display(
                        net.minecraft.world.item.Items.POISONOUS_POTATO,
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.negative_soup", "Pasta Burner"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.negative_soup.desc", "Have 4 negative soup effects at the same time"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("has_negative_soup_effects", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MSFEffects.STICKY).and(MSFEffects.SLIPPERY).and(MSFEffects.SALTY).and(MSFEffects.PANTS_ON_FIRE)))
                .rewards(AdvancementRewards.Builder.experience(53))
                .save(consumer, MoreSnifferFlowers.loc("negative_soup").toString());




        Advancement.Builder.advancement()
                .parent(cropressing)
                .display(
                        MSFItems.REBREWING_STAND.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.rebrew", "Local Rebrewery"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.rebrew.desc", "Rebrew an Extracted Potion"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_rebrewed_potion", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, MSFTags.ItemTags.REBREWED_POTIONS).build()))
                .save(consumer, MoreSnifferFlowers.loc("rebrew").toString());

        var bobling = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        MSFItems.CORRUPTED_BOBLING_CORE.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.bobling", "Fight back!"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.bobling.desc", "Fight back against the trees"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("bobling_attacked", MSFAdvancementCritters.SimpleAdvancementTrigger.TriggerInstance.boblingAttack())
                .save(consumer, MoreSnifferFlowers.loc("bobling").toString());

        Advancement.Builder.advancement()
                .parent(bobling)
                .display(
                        MSFItems.CORRUPTED_SLIME_BALL.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.corruption", "Evil Blocks"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.corruption.desc", "Corrupt blocks around you, making them evil"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_corrupted_slime_ball", MSFAdvancementCritters.SimpleAdvancementTrigger.TriggerInstance.corruptedBlock())
                .save(consumer, MoreSnifferFlowers.loc("corruption").toString());

        Advancement.Builder.advancement()
                .parent(bobling)
                .display(
                        MSFItems.VIVICUS_ANTIDOTE.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.cure", "A bobling of Kindness"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.cure.desc", "Cure a vivicus sapling to get regular (kind) boblings"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("used_antidote", MSFAdvancementCritters.SimpleAdvancementTrigger.TriggerInstance.usedCure())
                .save(consumer, MoreSnifferFlowers.loc("cure").toString());

        var ambush = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        MSFItems.AMBUSH_SEEDS.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.ambush", "Ambushed by great loot"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.ambush.desc", "Break an amber block to get whats inside (the \"great\" part not guaranteed)"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_ambush", InventoryChangeTrigger.TriggerInstance.hasItems(MSFItems.AMBER_SHARD.get()))
                .save(consumer, MoreSnifferFlowers.loc("ambush").toString());

        Advancement.Builder.advancement()
                .parent(ambush)
                .display(
                        MSFItems.GARBUSH_SEEDS.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.garbush", "Garbushed by garbush loot"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.garbush.desc", "Break a Garnet block, like amber but more violent"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_garbush", InventoryChangeTrigger.TriggerInstance.hasItems(MSFItems.GARNET_SHARD.get()))
                .save(consumer, MoreSnifferFlowers.loc("garbush").toString());

        Advancement.Builder.advancement()
                .parent(dyespria_plant)
                .display(
                        MSFItems.VIVICUS_BOAT.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.dye_boat", "Whatever colors your boat"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.dye_boat.desc", "Dye the vivicus boat any color, pretty unlikely to happen during actual gameplay"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("dye_boat", MSFAdvancementCritters.SimpleAdvancementTrigger.TriggerInstance.dyeBoat())
                .save(consumer, MoreSnifferFlowers.loc("dye_boat").toString());
    }

    private String id(String name) {
        return "%s:%s".formatted(MoreSnifferFlowers.MOD_ID, name);
    }

}
