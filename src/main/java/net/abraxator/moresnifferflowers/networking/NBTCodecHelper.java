package net.abraxator.moresnifferflowers.networking;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class NBTCodecHelper {

    public static <T> void encode(Codec<T> codec, T data, ValueOutput output, String name){
        if (data == null){
            return;
        }

        output.store(name, codec, data);
    }

    public static <T> T decode(Codec<T> codec, ValueInput input, String name, Supplier<T> orElse){
        return input.read(name, codec).orElse(orElse.get());
    }

    public static  <T> @Nullable T decode(Codec<T> codec, ValueInput input, String name){
        return input.read(name, codec).orElse(null);
    }

}
