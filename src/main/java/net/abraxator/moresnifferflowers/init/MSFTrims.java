package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface MSFTrims {

    interface Materials {
        Set<Consumer<BootstrapContext<TrimMaterial>>> MATERIALS = new HashSet<>();

        ResourceKey<TrimMaterial> AMBER = register("amber", MSFItems.AMBER_SHARD, 0xdf910b, ItemModelIndex.GOLD);
        ResourceKey<TrimMaterial> GARNET = register("garnet",MSFItems.GARNET_SHARD, 0x8d182b, ItemModelIndex.REDSTONE);
        ResourceKey<TrimMaterial> NETHER_WART = register("nether_wart", MSFItems.CROPRESSED_NETHERWART, 0x831c20, ItemModelIndex.REDSTONE);
        ResourceKey<TrimMaterial> POTATO = register("potato", MSFItems.CROPRESSED_POTATO, 0xd9aa51, ItemModelIndex.GOLD);
        ResourceKey<TrimMaterial> WHEAT = register("wheat", MSFItems.CROPRESSED_WHEAT, 0xcdb159, ItemModelIndex.GOLD);
        ResourceKey<TrimMaterial> BEETROOT = register("beetroot", MSFItems.CROPRESSED_BEETROOT, 0xa4272c, ItemModelIndex.REDSTONE);
        ResourceKey<TrimMaterial> CARROT = register("carrot", MSFItems.CROPRESSED_CARROT, 0xe67022, ItemModelIndex.COPPER);

        static void bootstrap(BootstrapContext<TrimMaterial> context) {
            MATERIALS.forEach(c -> c.accept(context));
        }
        
        private static ResourceKey<TrimMaterial> register(String name, Holder<Item> ingredient, int color, ItemModelIndex itemModelIndex, Map<Holder<ArmorMaterial>, String> overrideArmorMaterials) {
            ResourceKey<TrimMaterial> key = key(name);
            TrimMaterial trimmaterial = new TrimMaterial(name, ingredient, itemModelIndex.index, overrideArmorMaterials, Component.translatable(Util.makeDescriptionId("trim_material", key.location())).withStyle(Style.EMPTY.withColor(color)));
            MATERIALS.add(c -> c.register(key, trimmaterial));
            return key;
        }

        private static ResourceKey<TrimMaterial> register(String name, Holder<Item> ingredient, int color, ItemModelIndex itemModelIndex) {
            return register(name, ingredient, color, itemModelIndex, Map.of());
        }

        enum ItemModelIndex{
            QUARTZ(0.1f), IRON(0.2f), NETHERITE(0.3f), REDSTONE(0.4f), COPPER(0.5f),
            GOLD(0.6f), EMERALD(0.7f), DIAMOND(0.8f), LAPIS(0.8f), AMETHYST(1.0f);

            final float index;
            ItemModelIndex(float index) {
                this.index = index;
            }
        }

        private static @NotNull ResourceKey<TrimMaterial> key(String name) {
            return ResourceKey.create(Registries.TRIM_MATERIAL, MoreSnifferFlowers.loc(name));
        }
    }

    interface Patterns {
        Set<Consumer<BootstrapContext<TrimPattern>>> PATTERNS = new HashSet<>();

         ResourceKey<TrimPattern> AROMA = register("aroma", MSFItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE);
         ResourceKey<TrimPattern> CARNAGE = register("carnage", MSFItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE);
         ResourceKey<TrimPattern> NETHER_WART = register("nether_wart", MSFItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE);
         ResourceKey<TrimPattern> TATER = register("tater", MSFItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE);
         ResourceKey<TrimPattern> CAROTENE = register("carotene", MSFItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE);
         ResourceKey<TrimPattern> GRAIN = register("grain", MSFItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE);
         ResourceKey<TrimPattern> BEAT = register("beat", MSFItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE);

        static void bootstrap(BootstrapContext<TrimPattern> context) {
            PATTERNS.forEach(c -> c.accept(context));
        }

        static ResourceKey<TrimPattern> register(String name, Holder<Item> templateItem) {
            ResourceKey<TrimPattern> trimPatternKey = key(name);
            TrimPattern trimpattern = new TrimPattern(
                    trimPatternKey.identifier(),
                    Component.translatable(Util.makeDescriptionId("trim_pattern", trimPatternKey.identifier())),
                    false
            );
            PATTERNS.add(c -> c.register(trimPatternKey, trimpattern));
            return trimPatternKey;
        }

        private static @NotNull ResourceKey<TrimPattern> key(String name) {
            return ResourceKey.create(Registries.TRIM_PATTERN, MoreSnifferFlowers.loc(name));
        }
    }
}
