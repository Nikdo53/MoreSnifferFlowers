package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


public class VivicusSignRenderer extends StandingSignRenderer {
    public VivicusSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void renderSignWithText(SignBlockEntity signEntity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, BlockState state, SignBlock signBlock, WoodType woodType, Model model) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.75F * this.getSignModelRenderScale(), 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-signBlock.getYRotationDegrees(state)));
        if (!(state.getBlock() instanceof StandingSignBlock)) {
            poseStack.translate(0.0F, -0.3125F, -0.4375F);
        }
        renderVivicusSign(poseStack, buffer, packedLight, packedOverlay, woodType, model, state);
        this.renderSignText(
                signEntity.getBlockPos(),
                signEntity.getFrontText(),
                poseStack,
                buffer,
                packedLight,
                signEntity.getTextLineHeight(),
                signEntity.getMaxTextLineWidth(),
                true
        );
        this.renderSignText(
                signEntity.getBlockPos(),
                signEntity.getBackText(),
                poseStack,
                buffer,
                packedLight,
                signEntity.getTextLineHeight(),
                signEntity.getMaxTextLineWidth(),
                false
        );
        poseStack.popPose();
    }
    
    private void renderVivicusSign(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, WoodType woodType, Model model, BlockState state) {
        poseStack.pushPose();
        float f = this.getSignModelRenderScale();
        poseStack.scale(f, -f, -f);
        Material material = Sheets.getSignMaterial(woodType);
        VertexConsumer vertexconsumer = material.buffer(buffer, model::renderType);
        var color = -1;
        if(state.getBlock() instanceof ColorableVivicusBlock colorableVivicusBlock) {
            var dyeColor = state.getValue(MSFStateProperties.COLOR);
            color = colorableVivicusBlock.colorValues().get(dyeColor);
        }
        this.renderSignModel(poseStack, packedLight, packedOverlay, model, vertexconsumer, color);
        poseStack.popPose();
    }

    void renderSignModel(PoseStack poseStack, int packedLight, int packedOverlay, Model model, VertexConsumer vertexConsumer, int color) {
        SignRenderer.SignModel signrenderer$signmodel = (SignRenderer.SignModel)model;
        signrenderer$signmodel.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
