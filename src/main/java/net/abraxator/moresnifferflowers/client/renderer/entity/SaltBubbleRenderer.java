package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;

import net.abraxator.moresnifferflowers.client.model.entity.SaltBubbleModel;
import net.abraxator.moresnifferflowers.entities.SaltBubbleProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class SaltBubbleRenderer extends EntityRenderer<SaltBubbleProjectile> {
    private final SaltBubbleModel model;
    public static final Identifier TEXTURE_SALT = MoreSnifferFlowers.loc("textures/entity/salt_bubble.png");
    public static final Identifier TEXTURE_SOUR = MoreSnifferFlowers.loc("textures/entity/sour_bubble.png");


    public SaltBubbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SaltBubbleModel(context.bakeLayer(SaltBubbleModel.SALT_BUBBLE));
    }

    @Override
    public void render(SaltBubbleProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(CoolProjectileRenderer.projectileCameraCheck(entity, this.entityRenderDispatcher)) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot())));
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
            poseStack.translate(0, -1.0, 0);

            float scale = 1F;
            float randomOffset = (float) (entity.position().x +entity.position().y + entity.position().z) * 10;
            float time = (entity.tickCount + randomOffset + partialTick) / 20f;

            float scaleAmount = entity.getState() == 1 ? 0.4F : 0.3F;

            scale = 1.1f + scaleAmount * Mth.sin(time / 2 * Mth.TWO_PI);
            poseStack.translate(0, -scale + 1.1, 0);


            if (entity.getState() == 1){
                poseStack.translate(0, 0.2f * Mth.sin(time / 4 * Mth.TWO_PI), 0);
            }

            if (entity.getState() == 2){
                scale *= 2;
            }

            poseStack.scale(scale, scale, scale);


            this.model.renderToBuffer(
                    poseStack,
                    buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity))),
                    packedLight,
                    OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
            super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        }
    }

    @Override
    public Identifier getTextureLocation(SaltBubbleProjectile entity) {
        return entity.isCorrupted() ? TEXTURE_SOUR : TEXTURE_SALT;
    }
}
