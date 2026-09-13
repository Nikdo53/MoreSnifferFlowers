package net.abraxator.moresnifferflowers.client.color.item;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public record DyespriaTint() implements ItemTintSource {
    public static final DyespriaTint INSTANCE = new DyespriaTint();
    public static final MapCodec<DyespriaTint> CODEC = MapCodec.unit(() -> DyespriaTint.INSTANCE);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        Dye dye = Dye.getDyeFromDyespria(itemStack);
        if(dye.isEmpty()) {
            return -1;
        } else {
            return Dye.colorForDye(((DyespriaItem) itemStack.getItem()), dye.color());
        }
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }

    @Override
    public @NotNull String toString() {
        return "DyespriaTint";
    }
}
