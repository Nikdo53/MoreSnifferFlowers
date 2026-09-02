package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.nutrition.NutritionType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;

import java.util.*;

public record NutritionCapability(Set<Item> unlockedItems, Set<Integer> unlockedEffects) {
    public static final Codec<Set<Item>> ITEM_SET_CODEC =
            BuiltInRegistries.ITEM.byNameCodec()
                    .listOf()
                    .xmap(HashSet::new, ArrayList::new);

    public static final Codec<Set<Integer>> INT_SET_CODEC = Codec.INT.listOf().xmap(HashSet::new, ArrayList::new);

    public static final Codec<NutritionCapability> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ITEM_SET_CODEC.fieldOf("items").forGetter(NutritionCapability::unlockedItems),
                    INT_SET_CODEC.fieldOf("effects").forGetter(NutritionCapability::unlockedEffects))
            .apply(instance, NutritionCapability::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NutritionCapability> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.collection(HashSet::new)), NutritionCapability::unlockedItems,
            ByteBufCodecs.INT.apply(ByteBufCodecs.collection(HashSet::new)), NutritionCapability::unlockedEffects,
            NutritionCapability::new
    );


    public static Holder<MobEffect> effectFromId(int id){
        if (id % 2 == 0){
           return NutritionType.getEffect(NutritionType.byId(id / 2), false);
        }
        return NutritionType.getEffect(NutritionType.byId(Mth.floor(id / 2f)), true);
    }

    public static int idFromNutrition(NutritionType type, boolean isPositive){
        int id = type.ordinal() * 2;
        if (isPositive) id++;
        return id;
    }

    public static final Map<Integer, Identifier> ICON_FROM_ID = Map.of(
            0, MoreSnifferFlowers.loc("textures/mob_effect/slippery.png"),
            1, MoreSnifferFlowers.loc("textures/mob_effect/untouchable.png"),
            2, MoreSnifferFlowers.loc("textures/mob_effect/salty.png"),
            3, MoreSnifferFlowers.loc("textures/mob_effect/combo_meal.png"),
            4, MoreSnifferFlowers.loc("textures/mob_effect/pants_on_fire.png"),
            5, MoreSnifferFlowers.loc("textures/mob_effect/hardened_mouth.png"),
            6, MoreSnifferFlowers.loc("textures/mob_effect/sticky.png"),
            7, MoreSnifferFlowers.loc("textures/mob_effect/gluing_touch.png"),
            8, MoreSnifferFlowers.loc("textures/mob_effect/bland.png"),
            9, MoreSnifferFlowers.loc("textures/mob_effect/well_balanced.png")
    );
}
