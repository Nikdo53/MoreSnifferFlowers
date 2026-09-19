package net.abraxator.moresnifferflowers.datagen;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.datagen.advancement.MSFAdvancementGenerator;
import net.abraxator.moresnifferflowers.datagen.datamaps.MSFDataMapsProvider;
import net.abraxator.moresnifferflowers.datagen.loot.*;
import net.abraxator.moresnifferflowers.datagen.model.MSFModelGenerator;
import net.abraxator.moresnifferflowers.datagen.recipe.MSFRecipesProvider;
import net.abraxator.moresnifferflowers.datagen.tag.ModBannerPatternTagsProvider;
import net.abraxator.moresnifferflowers.datagen.tag.ModBiomeTagProvider;
import net.abraxator.moresnifferflowers.datagen.tag.ModBlockTagsProvider;
import net.abraxator.moresnifferflowers.datagen.tag.ModItemTagsProvider;
import net.abraxator.moresnifferflowers.init.MSFLoot;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID)
public class MSFDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event){
        event.createDatapackRegistryObjects(RegistryDataGenerator.BUILDER);

        var generator = event.getGenerator();
        var lookupProvider = event.getLookupProvider();
        var packOutput = generator.getPackOutput();

        //BLOCKMODELS
        generator.addProvider(true, new MSFModelGenerator(packOutput));

        //SOUNDS
        generator.addProvider(true, new MSFSoundProvider(packOutput));
        
        //DATAPACK REGISTRIES
      //  generator.addProvider(true, new RegistryDataGenerator(packOutput, lookupProvider));
        
        //DATA MAPS
        generator.addProvider(true, new MSFDataMapsProvider(packOutput, lookupProvider));
        
        //LOOT
        generator.addProvider(true, new LootTableProvider(
                packOutput,
                MSFLoot.LootTables.all(),
                List.of(
                        new LootTableProvider.SubProviderEntry(MSFBlockLoot::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(MSFArcheologyLoot::new, LootContextParamSets.ARCHAEOLOGY),
                        new LootTableProvider.SubProviderEntry(MSFChestLoot::new, LootContextParamSets.CHEST),
                        new LootTableProvider.SubProviderEntry(MSFEntityLoot::new, LootContextParamSets.ENTITY)
                ),
                lookupProvider
        ));

        //TAGS
        event.createBlockAndItemTags(ModBlockTagsProvider::new, ModItemTagsProvider::new);
        generator.addProvider(true, new ModBiomeTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModBannerPatternTagsProvider(packOutput, lookupProvider));

        //ADVANCEMENTS
        generator.addProvider(true, new AdvancementProvider(packOutput, lookupProvider, List.of(new MSFAdvancementGenerator())));

        //RECIPES
        generator.addProvider(true, new MSFRecipesProvider.Runner(packOutput, lookupProvider));
    }
}
