package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;

import net.abraxator.moresnifferflowers.client.model.entity.GluingGumModel;
import net.abraxator.moresnifferflowers.entities.GluingGumEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class GluingGumRenderer extends EntityRenderer<GluingGumEntity> {
    public static final Identifier GLUING_GUM_TEXTURE = MoreSnifferFlowers.loc("textures/entity/gluing_gum.png");
    private final GluingGumModel model;

    public GluingGumRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GluingGumModel(context.bakeLayer(GluingGumModel.GLUING_GUM));
    }

    @Override
    public Identifier getTextureLocation(GluingGumEntity entity) {
        return GLUING_GUM_TEXTURE;
    }

    @Override
    public void render(GluingGumEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.XN.rotationDegrees(180F));
        poseStack.translate(0.0f, -1.51f, 0.0f);
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(GLUING_GUM_TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}
