package net.abraxator.moresnifferflowers.datagen;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.datagen.advancement.MSFAdvancementGenerator;
import net.abraxator.moresnifferflowers.datagen.datamaps.MSFDataMapsProvider;
import net.abraxator.moresnifferflowers.datagen.loot.MSFLootGenerator;
import net.abraxator.moresnifferflowers.datagen.model.MSFModelGenerator;
import net.abraxator.moresnifferflowers.datagen.recipe.MSFRecipesProvider;
import net.abraxator.moresnifferflowers.datagen.tag.*;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID)
public class MSFDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event){
        var generator = event.getGenerator();
        var lookupProvider = event.getLookupProvider();
        var packOutput = generator.getPackOutput();
        var datapackProvider = new RegistryDataGenerator(packOutput, event.getLookupProvider());
        var registryProvider = datapackProvider.getRegistryProvider();

        //BLOCKMODELS
        generator.addProvider(true, new MSFModelGenerator(packOutput));

        //SOUNDS
        generator.addProvider(true, new MSFSoundProvider(packOutput));
        
        //DATAPACK REGISTRIES
        generator.addProvider(true, new RegistryDataGenerator(packOutput, lookupProvider));
        
        //DATA MAPS
        generator.addProvider(true, new MSFDataMapsProvider(packOutput, lookupProvider));
        
        //LOOT
        generator.addProvider(true, MSFLootGenerator.create(packOutput, lookupProvider));

        //TAGS
        event.createBlockAndItemTags(ModBlockTagsProvider::new, ModItemTagsProvider::new);
        generator.addProvider(true, new ModBiomeTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModBannerPatternTagsProvider(packOutput, registryProvider));

        //ADVANCEMENTS
        generator.addProvider(true, new AdvancementProvider(packOutput, lookupProvider, List.of(new MSFAdvancementGenerator())));

        //RECIPES
        generator.addProvider(true, new MSFRecipesProvider.Runner(packOutput, lookupProvider));
    }
}
