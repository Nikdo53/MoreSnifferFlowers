package net.abraxator.moresnifferflowers.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;

public class BannerPatternItem extends Item {
    public BannerPatternItem(TagKey<BannerPattern> patternTag, Properties properties) {
        super(properties
                .stacksTo(1)
                .delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(BannerPatternTags.PATTERN_ITEM_GLOBE)));
    }
}
