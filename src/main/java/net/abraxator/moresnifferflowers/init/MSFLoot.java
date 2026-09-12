package net.abraxator.moresnifferflowers.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashSet;
import java.util.Set;

public interface MSFLoot {
    interface LootConditions {
        DeferredRegister<LootItemConditionType> CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MoreSnifferFlowers.MOD_ID);

        DeferredHolder<LootItemConditionType, LootItemConditionType> BOBLING_TYPE = CONDITIONS.register("bobling_type", () -> new LootItemConditionType(BoblingTypeCondition.CODEC));


        record BoblingTypeCondition(boolean cured) implements LootItemCondition {
            public static final MapCodec<BoblingTypeCondition> CODEC =
                    RecordCodecBuilder.mapCodec(instance ->
                            instance.group(
                                    Codec.BOOL.fieldOf("inverse").forGetter(o -> o.cured))
                            .apply(instance, BoblingTypeCondition::new));

            @Override
            public LootItemConditionType getType() {
                return BOBLING_TYPE.get();
            }

            @Override
            public boolean test(LootContext lootContext) {
                return lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof BoblingEntity bobling && bobling.isCured() == cured;
            }

            public static Builder builder(boolean cured) {
                return () -> new BoblingTypeCondition(cured);
            }
        }


    }

    interface LootTables {
        Set<ResourceKey<LootTable>> RESOURCES = new HashSet<>();

        ResourceKey<LootTable> SNOW_SNIFFER_TEMPLE = register("snow_sniffer_temple");
        ResourceKey<LootTable> DESSERT_SNIFFER_TEMPLE = register("dessert_sniffer_temple");
        ResourceKey<LootTable> SWAMP_SNIFFER_TEMPLE = register("swamp_sniffer_temple");
        ResourceKey<LootTable> SWAMP_SNIFFER_TEMPLE_CHEST = register("swamp_sniffer_temple_chest");
        ResourceKey<LootTable> SNIFFER_EGG = register("sniffer_egg");

        private static ResourceKey<LootTable> register(String name) {
            var ret = ResourceKey.create(Registries.LOOT_TABLE, MoreSnifferFlowers.loc(name));
            RESOURCES.add(ret);
            return ret;
        }

         static Set<ResourceKey<LootTable>> all() {
            return RESOURCES;
        }
    }
}
