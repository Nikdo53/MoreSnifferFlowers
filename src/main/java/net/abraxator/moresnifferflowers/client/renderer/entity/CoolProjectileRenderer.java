package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.abraxator.moresnifferflowers.client.model.entity.CorruptedProjectileModel;
import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public abstract class CoolProjectileRenderer<T extends Entity> extends EntityRenderer<T> {
    private final CorruptedProjectileModel model;

    protected CoolProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CorruptedProjectileModel(context.bakeLayer(CorruptedProjectileModel.CORRUPTED_PROJECTILE));
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(projectileCameraCheck(entity, this.entityRenderDispatcher)) {
            projectileRendering(entity, partialTick, poseStack, buffer, packedLight, this.model, getTextureLocation(entity));
            super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        }
    }

    public static void projectileRendering(Entity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Model model, Identifier location) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 180F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
        poseStack.translate(0, -0.5, 0);
        float scale = 0.6F;
        poseStack.scale(scale, scale, scale);
        model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(model.renderType(location)),
                packedLight,
                OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    public static boolean projectileCameraCheck(Entity entity, EntityRenderDispatcher renderer ) {
        return entity.tickCount >= 2 || !(renderer.camera.getEntity().distanceToSqr(entity) < 12.25);
    }
}
