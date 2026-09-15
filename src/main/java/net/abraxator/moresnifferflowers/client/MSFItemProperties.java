package net.abraxator.moresnifferflowers.client;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.Dye;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import org.jspecify.annotations.Nullable;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, value = Dist.CLIENT)
public class MSFItemProperties {
    @SubscribeEvent
    public static void register(RegisterConditionalItemModelPropertyEvent event) {
        event.register(MoreSnifferFlowers.loc("dyespria"), Dyespria.MAP_CODEC);
        event.register(MoreSnifferFlowers.loc("patternspria"), Patternspria.MAP_CODEC);
    }

    public record Dyespria() implements ConditionalItemModelProperty{
        public static final MapCodec<Dyespria> MAP_CODEC = MapCodec.unit(new Dyespria());

        @Override
        public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
            return !Dye.getDyeFromDyespria(stack).isEmpty();
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

    public record Patternspria() implements ConditionalItemModelProperty{
        public static final MapCodec<Dyespria> MAP_CODEC = MapCodec.unit(new Dyespria());

        @Override
        public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
            return BlockPattern.fromPatternspria(stack) != BlockPattern.EMPTY;
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

}
