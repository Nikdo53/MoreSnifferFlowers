package net.abraxator.moresnifferflowers.datagen.loot;

import net.abraxator.moresnifferflowers.init.MSFEntityTypes;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFLoot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.Stream;

public class MSFEntityLoot extends EntityLootSubProvider {
    public MSFEntityLoot(HolderLookup.Provider provider) {
        super(FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    public void generate() {
        add(MSFEntityTypes.BOBLING.get(), LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(MSFItems.CORRUPTED_BOBLING_CORE)
                                .when(LootItemRandomChanceCondition.randomChance(0.8F))
                                .when(MSFLoot.LootConditions.BoblingTypeCondition.builder(false))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.1F, 0.05F))
                        )
                        .add(LootItem.lootTableItem(MSFItems.BOBLING_CORE)
                                .when(LootItemRandomChanceCondition.randomChance(0.4F))
                                .when(MSFLoot.LootConditions.BoblingTypeCondition.builder(true))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.15F, 0.1F))
                        )
        ));

    }


    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return MSFEntityTypes.ENTITIES.getEntries().stream().filter(type -> type.get().getCategory() != MobCategory.MISC).map(DeferredHolder::value);
    }
}
