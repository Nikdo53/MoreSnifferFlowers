package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.entity.SaltBubbleModel;
import net.abraxator.moresnifferflowers.entities.SaltBubbleProjectile;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class SaltBubbleRenderer extends EntityRenderer<SaltBubbleProjectile, SaltBubbleRenderer.State> {
    private final SaltBubbleModel model;
    public static final Identifier TEXTURE_SALT = MoreSnifferFlowers.loc("textures/entity/salt_bubble.png");
    public static final Identifier TEXTURE_SOUR = MoreSnifferFlowers.loc("textures/entity/sour_bubble.png");


    public SaltBubbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SaltBubbleModel(context.bakeLayer(SaltBubbleModel.SALT_BUBBLE));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(SaltBubbleProjectile entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.isCorrupted = entity.isCorrupted();
        state.state = entity.getState();
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        poseStack.translate(0, -1.0, 0);

        float randomOffset = (float) (state.x +state.y + state.z) * 10;
        float time = (state.ageInTicks + randomOffset) / 20f;

        float scaleAmount = state.state == 1 ? 0.4F : 0.3F;

        float scale = 1.1f + scaleAmount * Mth.sin(time / 2 * Mth.TWO_PI);
        poseStack.translate(0, -scale + 1.1, 0);


        if (state.state == 1){
            poseStack.translate(0, 0.2f * Mth.sin(time / 4 * Mth.TWO_PI), 0);
        }


        if (state.state == 2){
            scale *= 2;
        }

        poseStack.scale(scale, scale, scale);

        submitNodeCollector.submitModel(model, state, poseStack, RenderTypes.entityCutoutCull(this.getTextureLocation(state)), state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public Identifier getTextureLocation(State entity) {
        return entity.isCorrupted ? TEXTURE_SOUR : TEXTURE_SALT;
    }



    public static class State extends EntityRenderState {
        public float xRot;
        public float yRot;
        public int state;
        public boolean isCorrupted = false;
    }
}
