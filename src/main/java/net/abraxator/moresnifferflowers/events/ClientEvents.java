package net.abraxator.moresnifferflowers.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.entity.GluingGumModel;
import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.abraxator.moresnifferflowers.effects.GluedEffect;
import net.abraxator.moresnifferflowers.effects.IMSFPotionEffect;
import net.abraxator.moresnifferflowers.effects.SlipperyEffect;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.networking.toServer.DyespriaModePacket;
import net.abraxator.moresnifferflowers.networking.toServer.PatternspriaModePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Set;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onInputMouseScrolling(InputEvent.MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        if(player.isCrouching() && player.getMainHandItem().is(MSFItems.DYESPRIA.get())) {
            event.setCanceled(true);
            ClientPacketDistributor.sendToServer(new DyespriaModePacket((int) event.getScrollDeltaY()));
        }
        if(player.isCrouching() && player.getMainHandItem().is(MSFItems.PATTERNSPRIA.get())) {
            event.setCanceled(true);
            ClientPacketDistributor.sendToServer(new PatternspriaModePacket((int) event.getScrollDeltaY()));
        }
    }

    @SubscribeEvent
    public static void addSectionGeometry(AddSectionGeometryEvent event) {
        Set<BlockPatternRenderer.BlockPatternQuad> cache = BlockPatternRenderer.cache(event.getLevel(), event.getSectionOrigin());
        if (!cache.isEmpty()) {
            event.addRenderer(ctx -> BlockPatternRenderer.renderAll(ctx, cache));
        }
    }


    @SubscribeEvent
    public static void renderLiving(RenderLivingEvent.Post<?, ?, ?> event) {
        LivingEntityRenderState renderState = event.getRenderState();
        if (Boolean.TRUE.equals(renderState.getRenderData(GluedEffect.IS_GLUED_KEY))) {
            Minecraft minecraft = Minecraft.getInstance();
            PoseStack poseStack = event.getPoseStack();

            poseStack.pushPose();
            float yOff = 0;
            if (renderState instanceof AvatarRenderState player && player.isCrouching) {
                yOff += 0.13f;
            }
            poseStack.translate(0, yOff, 0);

            event.getSubmitNodeCollector().submitModelPart(GluingGumModel.createBodyLayer().bakeRoot(),
                    poseStack,
                    RenderTypes.entityCutout(MoreSnifferFlowers.loc("textures/entity/gluing_gum.png")),
                    renderState.lightCoords, renderState.outlineColor, null);

            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void renderPlayer(RenderPlayerEvent.Pre<?> event) {
        PoseStack pose = event.getPoseStack();
        if (Boolean.TRUE.equals(event.getRenderState().getRenderData(SlipperyEffect.IS_FALLEN_KEY))){
            pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
            pose.translate(0.0D, -0.5D, 0.0D);
        }
    }

    @SubscribeEvent
    public static void renderPlayer(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        for (MobEffectInstance activeEffect : player.getActiveEffects()) {
            if (activeEffect.getEffect().value() instanceof IMSFPotionEffect effect){
                effect.playerClientTick(player.level(), player, activeEffect.getAmplifier());
            }
        }
    }

}
