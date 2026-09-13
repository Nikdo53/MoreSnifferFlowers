package net.abraxator.moresnifferflowers.client.color.item;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public record RootedSoupTint() implements ItemTintSource {
    public static final RootedSoupTint INSTANCE = new RootedSoupTint();
    public static final MapCodec<RootedSoupTint> CODEC = MapCodec.unit(() -> RootedSoupTint.INSTANCE);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return MSFColorHandler.alphaFixer(itemStack.getOrDefault(MSFDataComponents.COLOR.get(), 0xFFa4272c));}

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }

    @Override
    public @NotNull String toString() {
        return "RootedSoupTint";
    }
}
