package net.abraxator.moresnifferflowers.init;

import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.*;
import net.abraxator.moresnifferflowers.components.BetterNonNullList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface MSFDataAttachments {
    DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MoreSnifferFlowers.MOD_ID);

    Supplier<AttachmentType<BlockPatternCapability>> BLOCK_PATTERNS = register("block_patterns", () -> new BlockPatternCapability(new HashMap<>()), BlockPatternCapability.CODEC, BlockPatternCapability.STREAM_CODEC);

    Supplier<AttachmentType<ComboMealCapability>> COMBO_MEAL = register("combo_meal", () -> new ComboMealCapability(1f, 0), ComboMealCapability.CODEC, ComboMealCapability.STREAM_CODEC);

    Supplier<AttachmentType<CorruptionCapability>> CHUNK_CORRUPTION = register("corruption", CorruptionCapability::new, CorruptionCapability.CODEC);

    Supplier<AttachmentType<Boolean>> IS_GLUED = register("glued", () -> false, Codec.BOOL);

    Supplier<AttachmentType<BetterNonNullList<ItemStack>>> HARDENED_MOUTH_SLOTS = register("hardened_mouth_slots",
            () -> BetterNonNullList.withSize(2, ItemStack.EMPTY),
            BetterNonNullList.codecOf(ItemStack.OPTIONAL_CODEC),
            BetterNonNullList.streamCodecOf(ItemStack.OPTIONAL_STREAM_CODEC)
    );

    Supplier<AttachmentType<Integer>> HARDENED_MOUTH_COOLDOWN = register("hardened_mouth_cooldown", () -> 0, Codec.INT, ByteBufCodecs.VAR_INT);

    Supplier<AttachmentType<SlipperyCapability>> SLIPPERY = register("slippery", SlipperyCapability::new, SlipperyCapability.CODEC);

    Supplier<AttachmentType<UntouchableCapability>> UNTOUCHABLE = register("untouchable", UntouchableCapability::new, UntouchableCapability.CODEC);

    Supplier<AttachmentType<NutritionCapability>> NUTRITION = register("nutrition",
            () -> new NutritionCapability(new HashSet<>(), new HashSet<>()),
            NutritionCapability.CODEC,
            NutritionCapability.STREAM_CODEC,
            AttachmentType.Builder::copyOnDeath
    );

    Supplier<AttachmentType<Integer>> EXTRACTED_TICKS_REMAINING = register("extracted_ticks_remaining", () -> 0, Codec.INT);

    static <T> AttachmentType.Builder<T> builder(String name, Supplier<T> supplier, @Nullable Codec<T> codec, @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        AttachmentType.Builder<T> builder = AttachmentType.builder(supplier);
        if (codec != null)
            builder.serialize(codec.fieldOf(name));
        if (streamCodec != null)
            builder.sync(streamCodec);
        return builder;
    }

    static <T>DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String name, Supplier<T> supplier, @Nullable Codec<T> codec,
                                                                            @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, UnaryOperator<AttachmentType.Builder<T>> builderConsumer) {
        return ATTACHMENT_TYPES.register(name, () -> builderConsumer.apply(builder(name, supplier, codec, streamCodec)).build());
    }

    static <T>DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String name, Supplier<T> supplier, @Nullable Codec<T> codec,
                                                                            @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return ATTACHMENT_TYPES.register(name, () -> builder(name, supplier, codec, streamCodec).build());
    }

    static <T>DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String name, Supplier<T> supplier, @Nullable Codec<T> codec) {
        return ATTACHMENT_TYPES.register(name, () -> builder(name, supplier, codec, null).build());
    }
}
