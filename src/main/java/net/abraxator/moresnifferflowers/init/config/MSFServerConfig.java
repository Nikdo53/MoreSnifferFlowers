package net.abraxator.moresnifferflowers.init.config;


import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

;

public class MSFServerConfig {
    public static final ModConfigSpec SERVER_CONFIG;
    public static final ModConfigSpec.DoubleValue CORRUPTION_SPREAD_SPEED;
    public static final ModConfigSpec.BooleanValue CORRUPTED_TREE_GROW_THROUGH;
    public static final ModConfigSpec.BooleanValue CORRUPTED_TREE_BONE_MEAL;
    public static final ModConfigSpec.BooleanValue CORRUPTED_BOBLING_GRIEFING;
    public static final ModConfigSpec.BooleanValue CORRUPTED_SLUDGE_GRIEFING;

    public static final ModConfigSpec.ConfigValue<String> REBREWING_LENGTH;
    public static final ModConfigSpec.ConfigValue<String> REBREWING_AMPLIFIER;
    public static final ModConfigSpec.ConfigValue<String> REBREWING_SPLASH;
    public static final ModConfigSpec.ConfigValue<String> REBREWING_LINGERING;

    public static final ModConfigSpec.BooleanValue SALTEMONE_GRIEFING;
    public static final ModConfigSpec.IntValue THROWABLES_COOLDOWN;
    public static final ModConfigSpec.BooleanValue TORCHFLOWER_CONVERSION;



    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("corruption");

        CORRUPTION_SPREAD_SPEED = builder
                .comment("Spread speed of corrupted grass blocks, 1 = Default, 0 = Disabled")
                .translation("moresnifferflowers.configuration.corruption_spread_speed")
                .defineInRange("Corruption Spread Speed", 1D, 0D, 5D);

        CORRUPTED_TREE_GROW_THROUGH = builder
                .comment("Should the corrupted tree be able to grow through and destroy blocks? Default = true")
                .translation("moresnifferflowers.configuration.corrupted_tree_grow_through")
                .define("Corrupted Tree Grow Trough", true);

        CORRUPTED_TREE_BONE_MEAL = builder
                .comment("Should corrupted sapling require bone meal to grow at all? Default = false")
                .translation("moresnifferflowers.configuration.corrupted_tree_bone_meal")
                .define("Corrupted Tree Bone Meal", false);

        CORRUPTED_BOBLING_GRIEFING = builder
                .comment("Should boblings spawn projectiles when hit and replace blocks when planting? Default = true")
                .translation("moresnifferflowers.configuration.corrupted_bobling_griefing")
                .define("Corrupted Bobling Griefing", true);

        CORRUPTED_SLUDGE_GRIEFING = builder
                .comment("Should sludges shoot projectiles when blocks get destroyed? Default = true")
                .translation("moresnifferflowers.configuration.corrupted_sludge_griefing")
                .define("Corrupted Sludge Griefing", true);


        builder.pop();

        builder.push("rebrewing");

        builder.comment("Change unlockedItems for rebrewing recipe, JEI needs rejoin");
        builder.translation("moresnifferflowers.configuration.rebrew_jei");

        REBREWING_LENGTH = builder
                .translation("moresnifferflowers.configuration.rebrew_length")
                .define("Rebrewing Length", itemToString(Items.REDSTONE), MSFServerConfig::validateItemName);

        REBREWING_AMPLIFIER = builder
                .translation("moresnifferflowers.configuration.rebrew_amplifier")
                .define("Rebrewing Amplifier", itemToString(Items.GLOWSTONE_DUST), MSFServerConfig::validateItemName);

        REBREWING_SPLASH = builder
                .translation("moresnifferflowers.configuration.rebrew_splash")
                .define("Rebrewing Splash", itemToString(Items.GUNPOWDER), MSFServerConfig::validateItemName);

        REBREWING_LINGERING = builder
                .translation("moresnifferflowers.configuration.rebrew_lingering")
                .define("Rebrewing Lingering", itemToString(Items.DRAGON_BREATH), MSFServerConfig::validateItemName);

        builder.pop();

        builder.push("misc");
        SALTEMONE_GRIEFING = builder
                .comment("Should Saltemone Bubbles drop loot, even when nobody popped them? Default = true")
                .translation("moresnifferflowers.configuration.saltemone_lingering")
                .define("Saltemone Griefing", true);

        THROWABLES_COOLDOWN = builder
                .comment("Cooldown for all throwable unlockedItems, which drop blocks. Input in ticks")
                .translation("moresnifferflowers.configuration.throwables_cooldown")
                .defineInRange("Throwables Cooldown", 0, 0, 10000);

        TORCHFLOWER_CONVERSION = builder
                .comment("Allows Torchflowers to covert to burning torchflower when in direct sunlight")
                .translation("moresnifferflowers.configuration.torchflower_conversion")
                .define("Torchflower Conversion", true);


        builder.pop();


        SERVER_CONFIG = builder.build();
    }

    private static @NotNull String itemToString(Item glowstoneDust) {
        return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(glowstoneDust)).toString();
    }

    public static Item itemFromLoc(String loc) {
        return BuiltInRegistries.ITEM.getValue(MoreSnifferFlowers.separatorLoc(loc));
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(Identifier.parse(itemName));
    }

}
