package net.abraxator.moresnifferflowers;

import net.abraxator.moresnifferflowers.client.gui.screen.ClientDyespriaTooltip;
import net.abraxator.moresnifferflowers.client.gui.screen.DyespriaTooltip;
import net.abraxator.moresnifferflowers.client.gui.screen.GluedOverlay;
import net.abraxator.moresnifferflowers.client.gui.screen.RebrewingStandScreen;

import net.abraxator.moresnifferflowers.client.model.block.*;
import net.abraxator.moresnifferflowers.client.model.entity.*;
import net.abraxator.moresnifferflowers.client.particle.*;
import net.abraxator.moresnifferflowers.client.renderer.block.*;
import net.abraxator.moresnifferflowers.client.renderer.entity.*;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.init.config.MSFClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(value = MoreSnifferFlowers.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, value = Dist.CLIENT)
public class MoreSnifferFlowersClient {
    public MoreSnifferFlowersClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.registerConfig(ModConfig.Type.CLIENT, MSFClientConfig.CLIENT_CONFIG);
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Sheets.addWoodType(MSFWood.WoodTypes.CORRUPTED);
            Sheets.addWoodType(MSFWood.WoodTypes.VIVICUS);
        });
    }

    @SubscribeEvent
    public static void onRegisterMenuScreenEvent(RegisterMenuScreensEvent event) {
        event.register(MSFMenuTypes.REBREWING_STAND.get(), RebrewingStandScreen::new);
    }

    @SubscribeEvent
    public static void onEntityRenderersRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        //CENTER
        event.registerLayerDefinition(BoblingModel.BOBLING, BoblingModel::createBodyLayer);
        event.registerLayerDefinition(DragonflyModel.DRAGONFLY, DragonflyModel::createBodyLayer);
        event.registerLayerDefinition(CorruptedProjectileModel.CORRUPTED_PROJECTILE, CorruptedProjectileModel::createBodyLayer);
        event.registerLayerDefinition(VivicusBoatRenderer.CORRUPTED_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(VivicusBoatRenderer.CORRUPTED_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
        event.registerLayerDefinition(VivicusBoatRenderer.VIVICUS_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(VivicusBoatRenderer.VIVICUS_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
        event.registerLayerDefinition(SaltBubbleModel.SALT_BUBBLE, SaltBubbleModel::createBodyLayer);
        event.registerLayerDefinition(GluingGumModel.GLUING_GUM, GluingGumModel::createBodyLayer);

        //BLOCK
        event.registerLayerDefinition(GiantCropModels.GIANT_CARROT, GiantCropModels::createGiantCarrotLayer);
        event.registerLayerDefinition(GiantCropModels.GIANT_POTATO, GiantCropModels::createGiantPotatoLayer);
        event.registerLayerDefinition(GiantCropModels.GIANT_NETHERWART, GiantCropModels::createNetherwartLayer);
        event.registerLayerDefinition(GiantCropModels.GIANT_BEETROOT, GiantCropModels::createBeetrootLayer);
        event.registerLayerDefinition(GiantCropModels.GIANT_WHEAT, GiantCropModels::createWheatLayer);
        event.registerLayerDefinition(GiantCropModels.GIANT_ONION, GiantCropModels::createOnionLayer);
        event.registerLayerDefinition(GiantCropModels.GIANT_TOMATO, GiantCropModels::createTomatoLayer);
        event.registerLayerDefinition(GiantCropModels.GIANT_CABBAGE, GiantCropModels::createCabbageLayer);
        event.registerLayerDefinition(GiantCropModels.GIANT_RICE, GiantCropModels::createRiceLayer);

        event.registerLayerDefinition(BondripiaModel.BONDRIPIA, BondripiaModel::createBodyLayer);
        event.registerLayerDefinition(BerootCauldronModel.BEROOT_CAULDRON, BerootCauldronModel::createCauldronLayer);
        event.registerLayerDefinition(BerootCauldronModel.BEROOT_SPOON, BerootCauldronModel::createSpoonLayer);
        event.registerLayerDefinition(SaltemoneModel.SALTEMONE, SaltemoneModel::createBodyLayer);
        event.registerLayerDefinition(SaltemoneModel.SALTEMONE_TOP, SaltemoneModel::createTopLayer);
        event.registerLayerDefinition(DyespriaModel.DYESPRIA, DyespriaModel::createBodyLayer);

        event.registerLayerDefinition(SimpleModels.SIMPLE_CUBE, SimpleModels::simpleCube);
        event.registerLayerDefinition(SimpleModels.INVERTED_CUBE, SimpleModels::invertedCube);


    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MSFEntityTypes.BOBLING.get(), BoblingRenderer::new);
        event.registerEntityRenderer(MSFEntityTypes.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(MSFEntityTypes.CORRUPTED_SLIME_BALL.get(), CorruptedProjectileRenderer::new);
        event.registerEntityRenderer(MSFEntityTypes.CORRUPTED_BOAT.get(), context -> new VivicusBoatRenderer(context, false));
        event.registerEntityRenderer(MSFEntityTypes.CORRUPTED_CHEST_BOAT.get(), context -> new VivicusBoatRenderer(context, true));
        event.registerEntityRenderer(MSFEntityTypes.VIVICUS_BOAT.get(), context -> new VivicusBoatRenderer(context, false));
        event.registerEntityRenderer(MSFEntityTypes.VIVICUS_CHEST_BOAT.get(), context -> new VivicusBoatRenderer(context, true));
        event.registerEntityRenderer(MSFEntityTypes.JAR_OF_ACID.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(MSFEntityTypes.SALT_BUBBLE.get(), SaltBubbleRenderer::new);
        event.registerEntityRenderer(MSFEntityTypes.SALT_PROJECTILE.get(), SaltProjectileRenderer::new);
        event.registerEntityRenderer(MSFEntityTypes.GLUING_GUM_ENTITY.get(), GluingGumRenderer::new);

    }

    @SubscribeEvent
    public static void blockRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MSFBlockEntities.XBUSH.get(), AmbushBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.GIANT_CROP.get(), GiantCropBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.CROPRESSOR.get(), CropressorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.DYESPRIA_PLANT.get(), DyespriaPlantBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.MOD_SIGN.get(), StandingSignRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.VIVICUS_SIGN.get(), VivicusSignRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.VIVICUS_HANGING_SIGN.get(), VivicusHangingSignRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.BONDRIPIA.get(), BondripiaBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.BEROOT_CAULDRON.get(), BerootCauldronRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.SALTEMONE.get(), SaltemoneBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MSFBlockEntities.MOD_CAULDRON.get(), ModCauldronRenderer::new);

    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MSFParticles.AMBUSH.get(), AmbushParticle.Provider::new);
        event.registerSpriteSet(MSFParticles.GARBUSH.get(), AmbushParticle.Provider::new);
        event.registerSpriteSet(MSFParticles.GIANT_CROP.get(), GiantCropParticle.Provider::new);
        event.registerSpriteSet(MSFParticles.BONDRIPIA_DRIP.get(), BondripiaParticle.BondripiaDripProvider::new);
        event.registerSpriteSet(MSFParticles.BONDRIPIA_FALL.get(), BondripiaParticle.BondripiaFallProvider::new);
        event.registerSpriteSet(MSFParticles.BONDRIPIA_LAND.get(), BondripiaParticle.BondripiaLandProvider::new);

        event.registerSpriteSet(MSFParticles.ACIDRIPIA_DRIP.get(), BondripiaParticle.AcidripiaDripProvider::new);
        event.registerSpriteSet(MSFParticles.ACIDRIPIA_FALL.get(), BondripiaParticle.AcidripiaFallProvider::new);
        event.registerSpriteSet(MSFParticles.ACIDRIPIA_LAND.get(), BondripiaParticle.AcidripiaLandProvider::new);

        event.registerSpriteSet(MSFParticles.TORCHFLAME.get(), TorchflameParticle.Provider::new);
        event.registerSpriteSet(MSFParticles.BUBBLE.get(), ModBubbleParticle.Provider::new);

    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerBelowAll(MoreSnifferFlowers.loc("glued"), new GluedOverlay());
    }

    @SubscribeEvent
    public static void onRegisterTooltips(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(DyespriaTooltip.class, ClientDyespriaTooltip::new);
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        event.addPackFinders(MoreSnifferFlowers.loc("more_sniffer_flowers_boring"),
                PackType.CLIENT_RESOURCES,
                Component.literal("More Sniffer Flowers Boring"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP
        );

    }

    public static boolean isBoringLoaded() {
        return Minecraft.getInstance().getResourceManager().listPacks().anyMatch(packResources -> packResources.packId().equals("more_sniffer_flowers_boring"));
    }

}
