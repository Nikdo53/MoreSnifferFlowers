package net.abraxator.moresnifferflowers.datagen.tag;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends KeyTagProvider<Biome> {
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BIOME, lookupProvider, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(MSFTags.BiomeTags.HAS_DESSERT_SNIFFER_TEMPLE).addTag(Tags.Biomes.IS_DESERT);
        this.tag(MSFTags.BiomeTags.HAS_SNOW_SNIFFER_TEMPLE).addTag(Tags.Biomes.IS_SNOWY_PLAINS).addTag(Tags.Biomes.IS_SNOWY);

    }
}
