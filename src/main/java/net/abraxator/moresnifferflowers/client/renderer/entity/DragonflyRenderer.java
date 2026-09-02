package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;

import net.abraxator.moresnifferflowers.client.model.entity.DragonflyModel;
import net.abraxator.moresnifferflowers.entities.DragonflyProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class DragonflyRenderer extends EntityRenderer<DragonflyProjectile> {
    public static final Identifier TEXTURE = MoreSnifferFlowers.loc("textures/entity/dragonfly.png");
    private final DragonflyModel model;

    public DragonflyRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DragonflyModel(context.bakeLayer(DragonflyModel.DRAGONFLY));
    }

    @Override
    public void render(DragonflyProjectile entity, float pEntityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 180F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
        poseStack.translate(0, -1, 0.5);
        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity))),
                packedLight,
                OverlayTexture.NO_OVERLAY);
        model.animate(partialTick);
        poseStack.popPose();
        super.render(entity, pEntityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public Identifier getTextureLocation(DragonflyProjectile dragonflyProjectile) {
        return TEXTURE;
    }
}
