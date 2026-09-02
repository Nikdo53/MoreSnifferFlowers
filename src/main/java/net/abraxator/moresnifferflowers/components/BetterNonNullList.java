package net.abraxator.moresnifferflowers.components;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.networking.NBTCodecHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class BetterNonNullList<E> extends AbstractList<E> {
    protected final List<E> list;
    protected final E defaultValue;

    public static <E> Codec<BetterNonNullList<E>> codecOf(Codec<E> entryCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
                entryCodec.listOf().fieldOf("list").forGetter(l ->l.list),
                entryCodec.fieldOf("defaultValue").forGetter(l->l.defaultValue)
        ).apply(instance, BetterNonNullList::new));
    }

    public static <E> StreamCodec<RegistryFriendlyByteBuf, BetterNonNullList<E>> streamCodecOf(StreamCodec<RegistryFriendlyByteBuf, E> entryCodec) {
        return StreamCodec.composite(
                entryCodec.apply(ByteBufCodecs.list()), l ->l.list,
                entryCodec, l->l.defaultValue,
                BetterNonNullList::new
        );
    }

    public void copyTo(BetterNonNullList<E> list) {
        for (int i = 0; i < this.list.size(); i++) {
            list.set(i, this.list.get(i));
        }
    }


    public void writeToTag(Codec<E> codec, ValueOutput tag, String key) {
        NBTCodecHelper.encode(codecOf(codec), this, tag, key);
    }

    public static <E> void readFromTag(BetterNonNullList<E> listToWriteTo, Codec<E> codec, ValueInput tag, String key) {
        BetterNonNullList<E> decode = NBTCodecHelper.decode(codecOf(codec), tag, key);
        if (decode != null) {
            decode.copyTo(listToWriteTo);
        }
    }

    protected BetterNonNullList(List<E> list, E defaultValue) {
        this.list = list;
        this.defaultValue = defaultValue;

    }

    @SuppressWarnings("unchecked")
    public static <E> BetterNonNullList<E> withSize(int size, E defaultValue) {
        if (defaultValue == null) throw new IllegalArgumentException("defaultValue must not be null");
        Object[] aobject = new Object[size];
        Arrays.fill(aobject, defaultValue);
        return new BetterNonNullList<>(Arrays.asList((E[])aobject), defaultValue);
    }

    @SafeVarargs
    public static <E> BetterNonNullList<E> of(E defaultValue, E... elements) {
        return new BetterNonNullList<>(Arrays.asList(elements), defaultValue);
    }

    public int getValidSize() {
        int size = 0;
        for (E entry : list) {
            if (!isDefault(entry)) {
                size++;
            }
        }
        return size;
    }

    public int getFirstEmptySlot(){
        for (int i = 0; i < list.size(); i++) {
            E entry = list.get(i);
            if (isDefault(entry)) return i;
        }
        throw new RuntimeException("No empty Slot found");
    }

    public E getLastValid(){
        for (int i = list.size() -1 ; i >= 0; i--) {
            E entry = list.get(i);
            if (!isDefault(entry)) {
                return entry;

            }
        }

        throw new RuntimeException("List is Default");
    }

    public boolean isDefault(E o) {
        if (o == null) return defaultValue == null;
        return o.equals(defaultValue);
    }

    public boolean isFullyDefault() {
        for (E entry : list) {
            if (!isDefault(entry))
                return false;
        }
        return true;
    }

    public boolean isFull(){
        return getValidSize() >= list.size();
    }

    public Stream<E> validStream() {
        return list.stream().filter(e -> !isDefault(e));
    }


    public @NotNull E setDefault(int index) {
        E ret = this.get(index);
        list.set(index, defaultValue);
        return ret;
    }

    @Nonnull
    @Override
    public E get(int index) {
        return this.list.get(index);
    }

    @Override
    public E set(int index, E value) {
        if (value == null) throw new NullPointerException();
        return this.list.set(index, value);
    }

    @Override
    public void add(int index, E value) {
        if (value == null) throw new NullPointerException();
        this.list.add(index, value);
    }

    @Override
    public E remove(int index) {
        return this.list.remove(index);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public void clear() {
        if (this.defaultValue == null) {
            super.clear();
        } else {
            Collections.fill(this, this.defaultValue);
        }
    }
}
