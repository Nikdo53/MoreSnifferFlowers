package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.blockentities.XbushBlockEntity;
import net.abraxator.moresnifferflowers.blocks.xbush.AbstractXBushBlockUpper;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class AmbushBlockEntityRenderer extends MSFBERenderer<XbushBlockEntity, AmbushBlockEntityRenderer.State> {

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(XbushBlockEntity blockEntity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.growProgress = blockEntity.growProgress;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if(state.getBlockState().getBlock() instanceof AbstractXBushBlockUpper bushBlockUpper) {
            BlockState blockState = bushBlockUpper.getDropBlock().defaultBlockState();
            poseStack.pushPose();
            float progress = Math.min(state.growProgress, 1);
            float translate = 0.5f -(progress  * 0.5f);
            poseStack.translate(translate, translate, translate);
            poseStack.scale(progress, progress, progress);
            submitNodeCollector.submitMovingBlock(poseStack, getMovingBlockRenderState(state.blockPos, blockState));
            poseStack.popPose();
        }
    }

    public AmbushBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public static class State extends MSFBERenderState {
        public float growProgress;
    }
}