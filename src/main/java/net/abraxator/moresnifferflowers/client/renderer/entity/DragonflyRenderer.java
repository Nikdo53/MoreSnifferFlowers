package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.entity.DragonflyModel;
import net.abraxator.moresnifferflowers.entities.DragonflyProjectile;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class DragonflyRenderer extends EntityRenderer<DragonflyProjectile, DragonflyRenderer.State> {
    public static final Identifier TEXTURE = MoreSnifferFlowers.loc("textures/entity/dragonfly.png");
    private final DragonflyModel model;

    public DragonflyRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DragonflyModel(context.bakeLayer(DragonflyModel.DRAGONFLY));
    }

    @Override
    public DragonflyRenderer.State createRenderState() {
        return new DragonflyRenderer.State();
    }

    @Override
    public void extractRenderState(DragonflyProjectile entity, DragonflyRenderer.State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
    }

    @Override
    public void submit(DragonflyRenderer.State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 180F));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        poseStack.translate(0, -1, 0.5);
        model.animate(state.partialTick);

        submitNodeCollector.submitModel(model, state, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public static class State extends EntityRenderState {
        public float xRot;
        public float yRot;
    }
}
