package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;

public interface MSFTags {
     interface ItemTags {
          TagKey<Item> AROMA_TRIM_TEMPLATE_INGREDIENT = tag("aroma_trim_template_ingredient");
          TagKey<Item> CROP_SMITHING_TEMPLATES = tag("crop_smithing_templates");
          TagKey<Item> CROPRESSABLE = tag("cropressable");
          TagKey<Item> CROPRESSOR_PIECES = tag("cropressor_pieces");
          TagKey<Item> CROPRESSED_CROPS = tag("cropressed_crops");
          TagKey<Item> REBREWED_POTIONS = tag("rebrewed_potions");
          TagKey<Item> VIVICUS_LOGS = tag("vivicus_logs");
          TagKey<Item> CORRUPTED_LOGS = tag("corrupted_logs");
          TagKey<Item> BLOCK_PATTERNS = tag("block_patterns");
          TagKey<Item> COLORABLE = tag("colorable");

          TagKey<Item> MSF_SNIFFER_LOOT = tag("msf_sniffer_loot");

         private static TagKey<Item> tag(String name){
             return createTyped(Registries.ITEM, name);
         }
     }

     interface BlockTags {
          TagKey<Block> BONMEELABLE = tag("bonmeelable");
          TagKey<Block> GIANT_CROP_REPLACEABLE = tag("giant_crop_replaceable");
          TagKey<Block> GIANT_CROPS = tag("giant_crops");
          TagKey<Block> WATERLOGGABLE = tag("waterloggable");
          TagKey<Block> NO_SHADING = tag("no_shading");

          TagKey<Block> VIVICUS_BLOCKS = tag("vivicus_blocks");
          TagKey<Block> CORRUPTED_BLOCKS = tag("corrupted_blocks");
          TagKey<Block> CORRUPTED_SLUDGE = tag("corrupted_sludge");
          TagKey<Block> VIVICUS_TREE_REPLACABLE = tag("vivicus_tree_replacable");
          TagKey<Block> CORRUPTION_TRANSFORMABLES = tag("corruption_transformables");
          TagKey<Block> DYED = tag("dyed");
          TagKey<Block> NO_CORRUPTED_SLIME_COLLISION = tag("no_corrupted_slime_collision");
          TagKey<Block> STICKABLE = tag("stickable");
          TagKey<Block> CORRUPTION_SHIELDING = tag("corruption_shielding");

          TagKey<Block> CARRYON_BLACKLIST = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("carryon", "block_blacklist"));;

         private static TagKey<Block> tag(String name){
             return createTyped(Registries.BLOCK, name);
         }
     }

     interface BannerTags {
          TagKey<BannerPattern> AMBUSH_BANNER_PATTERN = createTyped(Registries.BANNER_PATTERN, "pattern_item/ambush");
          TagKey<BannerPattern> EVIL_BANNER_PATTERN = createTyped(Registries.BANNER_PATTERN, "pattern_item/evil");

    }
    
     interface BiomeTags {
         TagKey<Biome> HAS_DESSERT_SNIFFER_TEMPLE = createTyped(Registries.BIOME, "has_structure/dessert_sniffer_temple_biomes");
         TagKey<Biome> HAS_SNOW_SNIFFER_TEMPLE = createTyped(Registries.BIOME, "has_structure/snow_sniffer_temple_biomes");

     }
    
     interface EffectTags {
          TagKey<MobEffect> EXTRACTION_BLACKLIST = createTyped(Registries.MOB_EFFECT, "extraction_blacklist");
    }

    private static <T> TagKey<T> createTyped(ResourceKey<Registry<T>> registry, String name){
        return TagKey.create(registry, MoreSnifferFlowers.loc(name));
    }

}