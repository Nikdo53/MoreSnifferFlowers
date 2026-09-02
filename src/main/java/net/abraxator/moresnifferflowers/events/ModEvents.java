package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFDataMaps;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.MSFEntityTypes;
import net.abraxator.moresnifferflowers.init.config.MSFClientConfig;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(MSFEntityTypes.BOBLING.get(), BoblingEntity.createAttributes().build());
    }


    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(MSFEntityTypes.BOBLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void onRegisterDataMapTypes(RegisterDataMapTypesEvent event) {
        MSFDataMaps.init();
        for (DataMapType<?, ?> dataMap : MSFDataMaps.DATA_MAPS) {
            event.register(dataMap);
        }
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event){
        if (MSFServerConfig.SERVER_CONFIG.isLoaded()) {
            List<Identifier> locations = new ArrayList<>();

            locations.add(MoreSnifferFlowers.separatorLoc(MSFServerConfig.REBREWING_AMPLIFIER.get()));
            locations.add(MoreSnifferFlowers.separatorLoc(MSFServerConfig.REBREWING_LENGTH.get()));
            locations.add(MoreSnifferFlowers.separatorLoc(MSFServerConfig.REBREWING_SPLASH.get()));
            locations.add(MoreSnifferFlowers.separatorLoc(MSFServerConfig.REBREWING_LINGERING.get()));

            for (Identifier location : locations) {
                if (!BuiltInRegistries.ITEM.containsKey(location)) {
                    MoreSnifferFlowers.LOGGER.error("Error in Rebrewing Server Config, couldn't find item: " + location);
                }

            }
        }

        if (MSFClientConfig.CLIENT_CONFIG.isLoaded()){
            int hardenedMouthX = MSFClientConfig.HARDENED_MOUTH_X.get();
            if (hardenedMouthX > -5 && hardenedMouthX < 132){
                MoreSnifferFlowers.LOGGER.error("Error in Hardened Mouth Client Config, the following X value would overlap vanilla slots " + hardenedMouthX + " ... Resetting to default value");

                MSFClientConfig.HARDENED_MOUTH_X.set(MSFClientConfig.HARDENED_MOUTH_X.getDefault());
                MSFClientConfig.HARDENED_MOUTH_X.save();
            }
        }
    }
}
