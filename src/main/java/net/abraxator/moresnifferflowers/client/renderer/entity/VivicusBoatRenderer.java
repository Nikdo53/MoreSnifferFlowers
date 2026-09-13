package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.boat.VivicusBoatEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.joml.Quaternionf;

public class VivicusBoatRenderer extends BoatRenderer {
    public static final ContextKey<Integer> BOAT_COLOR_KEY = new ContextKey<>(MoreSnifferFlowers.loc("boat_color_key"));

    public static final ModelLayerLocation CORRUPTED_BOAT_LAYER = new ModelLayerLocation(MoreSnifferFlowers.loc("boat/corrupted"), "main");
    public static final ModelLayerLocation CORRUPTED_CHEST_BOAT_LAYER = new ModelLayerLocation(MoreSnifferFlowers.loc("chest_boat/corrupted"), "main");
    public static final ModelLayerLocation VIVICUS_BOAT_LAYER = new ModelLayerLocation(MoreSnifferFlowers.loc("boat/vivicus"), "main");
    public static final ModelLayerLocation VIVICUS_CHEST_BOAT_LAYER = new ModelLayerLocation(MoreSnifferFlowers.loc("chest_boat/vivicus"), "main");

    public VivicusBoatRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
        super(context, modelId);
    }

    @Override
    public void extractRenderState(AbstractBoat entity, BoatRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        if (entity instanceof VivicusBoatEntity vivicusBoat) {
            state.setRenderData(BOAT_COLOR_KEY, vivicusBoat.colorValues().get(vivicusBoat.getColor()));
        }
    }

    @Override
    public void submit(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yRot));
        float hurt = state.hurtTime;
        if (hurt > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurt) * hurt * state.damageTime / 10.0F * state.hurtDir));
        }

        if (!state.isUnderWater && !Mth.equal(state.bubbleAngle, 0.0F)) {
            poseStack.mulPose(new Quaternionf().setAngleAxis(state.bubbleAngle * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        submitNodeCollector.submitModel(this.model(), state, poseStack, this.model().renderType(this.texture), state.lightCoords, OverlayTexture.NO_OVERLAY, state.getRenderData(BOAT_COLOR_KEY), null, state.outlineColor, null);
        this.submitTypeAdditions(state, poseStack, submitNodeCollector, state.lightCoords);
        poseStack.popPose();

        if (state.leashStates != null) {
            for (EntityRenderState.LeashState leashState : state.leashStates) {
                submitNodeCollector.submitLeash(poseStack, leashState);
            }
        }

        this.submitNameDisplay(state, poseStack, submitNodeCollector, camera);
    }
}
