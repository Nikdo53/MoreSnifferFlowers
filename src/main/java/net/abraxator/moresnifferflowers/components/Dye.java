package net.abraxator.moresnifferflowers.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public record Dye(DyeColor color, int amount) {
    public static final Codec<Dye> CODEC = RecordCodecBuilder.create(
            dyeInstance -> dyeInstance.group(
                    Codec.INT.fieldOf("color").forGetter(Dye::colorId),
                    Codec.INT.fieldOf("amount").forGetter(Dye::amount)
            ).apply(dyeInstance, (colorId, amount) -> new Dye(colorFromId(colorId), amount))
    );

    public static Function<DyeColor, Item> DYE_FOR_ITEM = Util.memoize(dye -> BuiltInRegistries.ITEM
            .stream().filter(item -> item.components().has(DataComponents.DYE) && item.components().get(DataComponents.DYE).equals(dye)).findFirst()
            .orElseThrow(() -> new IllegalStateException("No item for dye " + dye)));

    public static final Dye EMPTY = new Dye(DyeColor.WHITE, 0);
    
    public boolean isEmpty() {
        return Dye.this.amount <= 0;
    }

    public static Dye getDyeFromDyeStack(ItemStack dyeStack) {
        return new Dye((dyeStack.get(DataComponents.DYE)), dyeStack.getCount());
    }
    
    public static Dye getDyeFromDyespria(ItemStack dyespria) {
        return dyespria.getOrDefault(MSFDataComponents.DYE, EMPTY);
    }
    
    public static ItemStack stackFromDye(Dye dye) {
        return dye.isEmpty() ? ItemStack.EMPTY : new ItemStack(DYE_FOR_ITEM.apply(dye.color), dye.amount);
    }

    public ItemStack toStack(){
        return new ItemStack(DYE_FOR_ITEM.apply(color), amount);
    }
    
    public static boolean dyeCheck(Dye dye, ItemStack dyeToInsert) {
        return dye.color.equals(dyeToInsert.get(DataComponents.DYE));
    }
    
    public static int colorForDye(Colorable colorable, DyeColor dyeColor) {
        return colorable.colorValues().getOrDefault(dyeColor, -1);
    }

    public static void setDyeToDyeHolderStack(ItemStack dyespria, ItemStack dyeToInsert, int amount) {
        setDyeToDyeHolderStack(dyespria, dyeToInsert, amount, DyespriaItem.getDyespriaUses(dyespria));
    }
    
    public static void setDyeToDyeHolderStack(ItemStack dyespria, ItemStack dyeToInsert, int amount, int uses) {
        var dyeColor = dyeToInsert.getItem() instanceof DyeItem ? (dyeToInsert.get(DataComponents.DYE)): DyeColor.WHITE;
        dyespria.set(MSFDataComponents.DYE, new Dye(dyeColor, amount));
        dyespria.set(MSFDataComponents.COLOR, uses);
    }
    
    public static void setDyeColorToStack(ItemStack stack, DyeColor color, int amount) {
        stack.set(MSFDataComponents.DYE, new Dye(color, amount));
    }
    
    public static DyeColor colorFromId(int id) {
        return DyeColor.byId(id);
    }
    
    public int colorId() {
        return color.getId();
    }
}