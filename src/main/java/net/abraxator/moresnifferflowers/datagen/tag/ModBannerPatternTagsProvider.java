package net.abraxator.moresnifferflowers.datagen.tag;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBannerPatterns;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.concurrent.CompletableFuture;

public class ModBannerPatternTagsProvider extends KeyTagProvider<BannerPattern> {
    public ModBannerPatternTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BANNER_PATTERN, lookupProvider, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(MSFTags.MSFBannerTags.AMBUSH_BANNER_PATTERN).add(MSFBannerPatterns.AMBUSH);
        tag(MSFTags.MSFBannerTags.EVIL_BANNER_PATTERN).add(MSFBannerPatterns.EVIL);
    }

}
