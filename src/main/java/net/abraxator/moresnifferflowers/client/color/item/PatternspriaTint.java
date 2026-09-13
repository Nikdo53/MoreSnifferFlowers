package net.abraxator.moresnifferflowers.client.color.item;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public record PatternspriaTint() implements ItemTintSource {
    public static final PatternspriaTint INSTANCE = new PatternspriaTint();
    public static final MapCodec<PatternspriaTint> CODEC = MapCodec.unit(() -> PatternspriaTint.INSTANCE);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        BlockPattern pattern = BlockPattern.fromPatternspria(itemStack);
        if(pattern == BlockPattern.EMPTY) return -1;
        return MSFColorHandler.alphaFixer(itemStack.getOrDefault(MSFDataComponents.COLOR.get(), pattern.getColor()));
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }

    @Override
    public @NotNull String toString() {
        return "PatternspriaTint";
    }
}
