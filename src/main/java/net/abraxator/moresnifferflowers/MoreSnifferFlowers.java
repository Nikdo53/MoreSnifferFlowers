package net.abraxator.moresnifferflowers;

import com.mojang.logging.LogUtils;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
import net.abraxator.moresnifferflowers.networking.MSFNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(MoreSnifferFlowers.MOD_ID)
public class MoreSnifferFlowers {
    public static final String MOD_ID = "moresnifferflowers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MoreSnifferFlowers(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::commonSetup);
        container.registerConfig(ModConfig.Type.SERVER, MSFServerConfig.SERVER_CONFIG);

        MSFItems.ITEMS.register(modEventBus);
        MSFBlocks.BLOCKS.register(modEventBus);
        MSFSounds.SOUNDS.register(modEventBus);
        MSFEffects.EFFECTS.register(modEventBus);
        MSFCreativeTabs.TABS.register(modEventBus);
        MSFParticles.PARTICLES.register(modEventBus);
        MSFMenuTypes.MENU_TYPES.register(modEventBus);
        MSFEntityTypes.ENTITIES.register(modEventBus);
        MSFWorldGen.Features.FEATURES.register(modEventBus);
        MSFRecipes.Types.RECIPE_TYPES.register(modEventBus);
        MSFWood.TrunkPlacerTypes.TRUNKS.register(modEventBus);
        MSFAdvancementCritters.TRIGGERS.register(modEventBus);
        MSFBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        MSFDataComponents.DATA_COMPONENTS.register(modEventBus);
        MSFLoot.LootConditions.CONDITIONS.register(modEventBus);
        MSFDataAttachments.ATTACHMENT_TYPES.register(modEventBus);
        MSFWood.TreeDecoratorTypes.DECORATORS.register(modEventBus);
        MSFRecipes.Serializer.RECIPE_SERIALIZERS.register(modEventBus);

        MSFNetworking.register(modEventBus, 1);
    }

   private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(MSFBlocks.DYESPRIA_PLANT.getId(), MSFBlocks.POTTED_DYESPRIA);
            pot.addPlant(MSFBlocks.CORRUPTED_SAPLING.getId(), MSFBlocks.POTTED_CORRUPTED_SAPLING);
            pot.addPlant(MSFBlocks.VIVICUS_SAPLING.getId(), MSFBlocks.POTTED_VIVICUS_SAPLING);

            FireBlock fireBlock = (FireBlock) Blocks.FIRE;
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_LOG.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_WOOD.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.STRIPPED_CORRUPTED_LOG.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.STRIPPED_CORRUPTED_WOOD.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_PLANKS.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_STAIRS.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_SLAB.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_FENCE.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_FENCE_GATE.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_DOOR.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_TRAPDOOR.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_PRESSURE_PLATE.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_BUTTON.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_LEAVES.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.CORRUPTED_SAPLING.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_LOG.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_WOOD.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.STRIPPED_VIVICUS_LOG.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.STRIPPED_VIVICUS_WOOD.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_PLANKS.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_STAIRS.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_SLAB.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_FENCE.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_FENCE_GATE.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_DOOR.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_TRAPDOOR.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_PRESSURE_PLATE.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_BUTTON.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_LEAVES.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_SAPLING.get(), 5, 20);
            fireBlock.setFlammable(MSFBlocks.VIVICUS_LEAVES_SPROUT.get(), 5, 20);

            MSFCauldronInteractions.bootstrap();
        });
    }

    public static boolean hasFarmersDelight(){
        return ModList.get().isLoaded("farmersdelight");
    }

    public static Identifier farmersDelightLoc(String path) {
        return Identifier.fromNamespaceAndPath("farmersdelight", path);
    }

    public static Identifier loc(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public void clientConfig(ModContainer container){
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static Identifier vanillaLoc(String path) {
        return Identifier.withDefaultNamespace(path);
    }

    public static Identifier separatorLoc(String path) {
        return Identifier.bySeparator(path, ':');
    }

    public static String sLoc(String path) {
        return loc(path).toString();
    }
}
