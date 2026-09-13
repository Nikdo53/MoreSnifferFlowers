package net.abraxator.moresnifferflowers.components;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.abraxator.moresnifferflowers.items.PatternspriaItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public enum BlockPattern implements StringRepresentable {
    PIPES("pipes", MoreSnifferFlowers.loc("block_pattern_pipes"),0, 0xFF79b055, DyeColor.LIME), //lime
    BRICKS("bricks", MoreSnifferFlowers.loc("block_pattern_bricks"),1, 0xFFae457c, DyeColor.MAGENTA), //magenta
    FOCUS("focus", MoreSnifferFlowers.loc("block_pattern_focus"),2, 0xFF6a3d87, DyeColor.PURPLE), //purple
    BUBBLES("bubbles", MoreSnifferFlowers.loc("block_pattern_bubbles"),3, 0xFF6dbdba, DyeColor.LIGHT_BLUE), //lightblue
    CLOUDS("clouds", MoreSnifferFlowers.loc("block_pattern_clouds"),4, 0xFFddddd0, DyeColor.WHITE), //white
    DEEPSLATE("deepslate", MoreSnifferFlowers.loc("block_pattern_deepslate"),5, 0xFF413f51, DyeColor.BLACK), //black
    DIAMOND("diamond", MoreSnifferFlowers.loc("block_pattern_diamond"),6, 0xFF4a9887, DyeColor.CYAN), //cyan
    EYE("eye", MoreSnifferFlowers.loc("block_pattern_eye"),7, 0xFF98a5a7, DyeColor.LIGHT_GRAY), //lightgray
    HEARTS("hearts", MoreSnifferFlowers.loc("block_pattern_hearts"),8, 0xFFa63e3b, DyeColor.RED), //red
    HONEYCOMB("honeycomb", MoreSnifferFlowers.loc("block_pattern_honeycomb"),9, 0xFFbe7b3a, DyeColor.ORANGE), //orange
    PAWS("paws", MoreSnifferFlowers.loc("block_pattern_paws"),10, 0xFF7d5840, DyeColor.BROWN), //brown
    PRISMARINE("prismarine", MoreSnifferFlowers.loc("block_pattern_prismarine"),11, 0xFF5351ad, DyeColor.BLUE), //blue
    SPROUTS("sprouts", MoreSnifferFlowers.loc("block_pattern_sprouts"),12, 0xFF4e8646, DyeColor.GREEN), //green
    STARS("stars", MoreSnifferFlowers.loc("block_pattern_stars"),13, 0xFFeed462, DyeColor.YELLOW), //yellow
    COVER("cover", MoreSnifferFlowers.loc("block_pattern_cover"),14, 0xFF736979, DyeColor.GRAY), //gray
    FLOWERS("flowers", MoreSnifferFlowers.loc("block_pattern_flowers"),15, 0xFFb45da6, DyeColor.PINK), //pink

    FLOWER_CHARGE("flower_charge", MoreSnifferFlowers.vanillaLoc("flower_banner_pattern"),16, 0xFFffebb6, null),
    GLOBE("globe", MoreSnifferFlowers.vanillaLoc("globe_banner_pattern"),17, 0xFFffebb6, null),
    SNOUT("snout", MoreSnifferFlowers.vanillaLoc("piglin_banner_pattern"),18, 0xFFffebb6, null),
    CREEPER_CHARGE("creeper_charge", MoreSnifferFlowers.vanillaLoc("creeper_banner_pattern"),19, 0xFFffebb6, null),
    SKULL_CHARGE("skull_charge", MoreSnifferFlowers.vanillaLoc("skull_banner_pattern"),20, 0xFFffebb6, null),
    THING("thing", MoreSnifferFlowers.vanillaLoc("mojang_banner_pattern"),21, 0xFFffebb6, null),
    AMBUSH("ambush", MoreSnifferFlowers.loc("ambush_banner_pattern"),22, 0xFFffebb6, null),
    EVIL("evil", MoreSnifferFlowers.loc("evil_banner_pattern"),23, 0xFFffebb6, null),

    EMPTY("empty", null, -1, 0xFFddddd0, null)

    ;

    private final String name;
    private final Identifier item;
    private final int id;
    private final int color;
    private final DyeColor dyeColor;

    BlockPattern(String name, Identifier item, int id, int color, DyeColor dyeColor){
        this.name = name;
        this.item = item;
        this.id = id;
        this.color = color;
        this.dyeColor = dyeColor;
    }

    public static BlockPattern fromId(int index) {
        return Arrays.stream(values()).filter(blockPattern -> blockPattern.id == index).findFirst().orElse(EMPTY);
    }

    public static BlockPattern fromItem(Item item){
        return Arrays.stream(values()).filter(blockPattern -> Objects.equals(blockPattern.item, BuiltInRegistries.ITEM.getKey(item))).findFirst().orElse(EMPTY);
    }

    public static BlockPattern fromDyeColor(DyeColor dyeColor) {
        return Arrays.stream(values()).filter(blockPattern -> blockPattern.dyeColor == dyeColor).findFirst().orElse(EMPTY);
    }

    public static BlockPattern fromState(BlockState state) {
        return state.getValue(MSFStateProperties.BLOCK_PATTERN);
    }

    public static BlockPattern fromPatternspria(ItemStack stack) {
        if (stack.has(MSFDataComponents.PATTERN_ID)) {
            int patternId = stack.getOrDefault(MSFDataComponents.PATTERN_ID, 0);
            return fromId(patternId);
        }
        return EMPTY;
    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public Item getItem(){
        return BuiltInRegistries.ITEM.getValue(item);
    }

    public boolean isBanner(){
        return id > 15 && id < 24;
    }

    public boolean isSamePattern(ItemStack patternspria) {
        int patternId = patternspria.getOrDefault(MSFDataComponents.PATTERN_ID, 0);
        return patternId == this.id;
    }

    public ItemStack getItemStack(ItemStack patternspria) {
        int amount = patternspria.getOrDefault(MSFDataComponents.AMOUNT, 1);
        return getItem().getDefaultInstance().copyWithCount(amount);
    }

    public static ItemStack getItemStackFromPatternspria(ItemStack patternspria){
        return fromPatternspria(patternspria).getItemStack(patternspria);
    }

    public static void setPatternToHolderStack(ItemStack itemStack, ItemStack patternToInsert, int amount) {
        setPatternToHolderStack(itemStack, patternToInsert, amount, PatternspriaItem.getPatternspriaUses(itemStack));
    }

    public static void setPatternToHolderStack(ItemStack itemStack, ItemStack patternToInsert, int amount, int uses) {
        if (fromItem(patternToInsert.getItem()) == EMPTY) {
            removePatternFromStack(itemStack);
            return;
        }
        int patternId = Objects.requireNonNull(fromItem(patternToInsert.getItem())).getId();
        itemStack.set(MSFDataComponents.AMOUNT, amount);
        itemStack.set(MSFDataComponents.PATTERN_ID, patternId);
        itemStack.set(MSFDataComponents.USES, uses);
    }

    public static void removePatternFromStack(ItemStack itemStack) {
        itemStack.set(MSFDataComponents.AMOUNT, 0);
        itemStack.set(MSFDataComponents.PATTERN_ID, -1);
        itemStack.set(MSFDataComponents.USES, 0);
    }

    public static boolean isEmpty(BlockState state) {
        return state.getValue(MSFStateProperties.BLOCK_PATTERN) == EMPTY;
    }
}