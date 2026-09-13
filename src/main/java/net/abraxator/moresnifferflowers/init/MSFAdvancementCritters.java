package net.abraxator.moresnifferflowers.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

public interface MSFAdvancementCritters {
    DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, MoreSnifferFlowers.MOD_ID);

    Supplier<SimpleAdvancementTrigger> USED_DYESPRIA = TRIGGERS.register("used_dyespria", SimpleAdvancementTrigger::new);
    Supplier<SimpleAdvancementTrigger> USED_BONMEEL = TRIGGERS.register("used_bonmeel", SimpleAdvancementTrigger::new);
    Supplier<SimpleAdvancementTrigger> PLACED_DYESPRIA_PLANT = TRIGGERS.register("placed_dyespria_plant", SimpleAdvancementTrigger::new);
    Supplier<SimpleAdvancementTrigger> BOBLING_ATTACK = TRIGGERS.register("bobling_attack", SimpleAdvancementTrigger::new);
    Supplier<SimpleAdvancementTrigger> DYE_BOAT = TRIGGERS.register("dye_boat", SimpleAdvancementTrigger::new);
    Supplier<SimpleAdvancementTrigger> USED_CURE = TRIGGERS.register("used_cure", SimpleAdvancementTrigger::new);
    Supplier<SimpleAdvancementTrigger> CORRUPTED_BLOCK = TRIGGERS.register("corrupted_block", SimpleAdvancementTrigger::new);

    class SimpleAdvancementTrigger extends SimpleCriterionTrigger<SimpleAdvancementTrigger.TriggerInstance> {

        @Override
        public Codec<TriggerInstance> codec() {
            return TriggerInstance.CODEC;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, (instance) -> true);
        }

        public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {

            public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
                    .apply(instance, TriggerInstance::new));


            public static Criterion<?> usedDyespria() {
                return USED_DYESPRIA.get().createCriterion(new TriggerInstance(Optional.empty()));
            }

            public static Criterion<?> usedBonmeel() {
                return USED_BONMEEL.get().createCriterion(new TriggerInstance(Optional.empty()));
            }

            public static Criterion<?> placedDyespriaPlant() {
                return PLACED_DYESPRIA_PLANT.get().createCriterion(new TriggerInstance(Optional.empty()));
            }

            public static Criterion<?> boblingAttack() {
                return BOBLING_ATTACK.get().createCriterion(new TriggerInstance(Optional.empty()));
            }

            public static Criterion<?> dyeBoat() {
                return DYE_BOAT.get().createCriterion(new TriggerInstance(Optional.empty()));
            }

            public static Criterion<?> usedCure() {
                return USED_CURE.get().createCriterion(new TriggerInstance(Optional.empty()));
            }

            public static Criterion<?> corruptedBlock() {
                return CORRUPTED_BLOCK.get().createCriterion(new TriggerInstance(Optional.empty()));
            }
        }
    }
}

