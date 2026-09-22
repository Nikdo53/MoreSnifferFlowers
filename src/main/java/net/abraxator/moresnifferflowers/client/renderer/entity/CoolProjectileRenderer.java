package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.client.model.entity.CorruptedProjectileModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public abstract class CoolProjectileRenderer<T extends Entity> extends EntityRenderer<T, CoolProjectileRenderer.State> {
    private final ModelPart model;

    protected CoolProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = context.bakeLayer(CorruptedProjectileModel.CORRUPTED_PROJECTILE);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 180f));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        poseStack.translate(0, -0.5, 0);
        float scale = 0.6F;
        poseStack.scale(scale, scale, scale);
        submitNodeCollector.submitModelPart(
                this.model, poseStack, RenderTypes.entityCutout(this.getTextureLocation(state)), state.lightCoords, OverlayTexture.NO_OVERLAY, null
        );
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public void extractRenderState(T entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    protected abstract Identifier getTextureLocation(State state);


    public static class State extends EntityRenderState {
        public float xRot;
        public float yRot;
        public boolean isCorrupted = false;
    }
}
