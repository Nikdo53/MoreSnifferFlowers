package net.abraxator.moresnifferflowers.client.color.item;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.awt.*;

public record VivicusItemTint() implements ItemTintSource {
    public static final VivicusItemTint INSTANCE = new VivicusItemTint();
    public static final MapCodec<VivicusItemTint> CODEC = MapCodec.unit(() -> VivicusItemTint.INSTANCE);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        if (itemStack.has(MSFDataComponents.COLOR)) {
            int color = itemStack.getOrDefault(MSFDataComponents.COLOR, 0xffffffff);
            int colorId = itemStack.getOrDefault(MSFDataComponents.COLOR_ID, 0);

            if (Colorable.isModdedDye(DyeColor.byId(colorId))) {
                float[] colorHSB = MSFColorHandler.getColorHSB(color);

                colorHSB[1] = colorHSB[1] / 1.5f;
                colorHSB[2] = colorHSB[2] * 1.6f;

                if (colorHSB[2] > 1) colorHSB[2] = 1f;

                return MSFColorHandler.alphaFixer(Color.HSBtoRGB(colorHSB[0], colorHSB[1], colorHSB[2]));
            }

            return MSFColorHandler.alphaFixer(color);
        }
        return -1;

    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }

    @Override
    public @NotNull String toString() {
        return "VivicusTint";
    }
}
