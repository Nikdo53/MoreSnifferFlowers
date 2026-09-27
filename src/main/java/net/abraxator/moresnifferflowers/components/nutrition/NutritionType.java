package net.abraxator.moresnifferflowers.components.nutrition;

import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.minecraft.core.Holder;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;
import java.util.function.IntFunction;

public enum NutritionType implements StringRepresentable {
    SOUR("sour", 0xffe6a005, 0.5f),
    SALTY("salty", 0xff8bb8c3, 0.4f),
    SPICY("spicy", 0xffbb4330, 0.3f),
    SWEET("sweet", 0xffe67896, 0.2f),
    NEUTRAL("neutral", 0xff8c661e, 0.1f),;


    public final String name;
    public final int color;
    public final float priority;

    private static final IntFunction<NutritionType> BY_ID = ByIdMap.continuous(NutritionType::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<NutritionType> CODEC = StringRepresentable.fromValues(NutritionType::values);
    
    NutritionType(String name, int color, float priority) {
        this.name = name;
        this.color = color;
        this.priority = priority;
    }
    
    public static NutritionType byId(int id) {
        return BY_ID.apply(id);
    }
    
    public static Holder<MobEffect> getEffect(NutritionType nutritionType, boolean positive) {
        Map<NutritionType, Holder<MobEffect>> map = positive ? POSITIVE_SOUP_EFFECT_MAP : NEGATIVE_SOUP_EFFECT_MAP;

        return map.get(nutritionType);
    }

    public static final Map<NutritionType, Holder<MobEffect>> NEGATIVE_SOUP_EFFECT_MAP = Map.of(
            SOUR, MSFEffects.SLIPPERY,
            SALTY, MSFEffects.SALTY,
            SPICY, MSFEffects.PANTS_ON_FIRE,
            SWEET, MSFEffects.STICKY,
            NEUTRAL, MSFEffects.BLAND
    );

    public static final Map<NutritionType, Holder<MobEffect>> POSITIVE_SOUP_EFFECT_MAP = Map.of(
            SOUR, MSFEffects.UNTOUCHABLE,
            SALTY,  MSFEffects.COMBO_MEAL,
            SPICY,  MSFEffects.HARDENED_MOUTH,
            SWEET,  MSFEffects.GLUING_TOUCH,
            NEUTRAL, MSFEffects.WELL_BALANCED
    );

    @Override
    public String getSerializedName() {
        return name;
    }
}
