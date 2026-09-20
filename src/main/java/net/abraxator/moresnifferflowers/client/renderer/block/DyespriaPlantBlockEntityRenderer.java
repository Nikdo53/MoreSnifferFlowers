package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.DyespriaPlantBlockEntity;
import net.abraxator.moresnifferflowers.client.model.block.DyespriaModel;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class DyespriaPlantBlockEntityRenderer extends MSFBERenderer<DyespriaPlantBlockEntity, DyespriaPlantBlockEntityRenderer.State> {
    private final ModelPart modelPart;

    public DyespriaPlantBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        modelPart = context.bakeLayer(DyespriaModel.DYESPRIA);
    }

    public static boolean isRotated(BlockPos pos){
        long total = pos.getX() + pos.getY() + pos.getZ();
        return total % 2 == 0;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(DyespriaPlantBlockEntity blockEntity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        if (!blockEntity.dye.equals(Dye.EMPTY)){
            ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemStackRenderState, blockEntity.dye.toStack(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 53);

            state.itemStackRenderState = itemStackRenderState;
        }
        state.dye = blockEntity.dye;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        BlockState blockState = state.getBlockState();
        var isGrown = blockState.getValue(MSFStateProperties.AGE_3) >= 3;
        Dye dye = state.dye;
        var hasDye = !dye.isEmpty();

        if (isGrown){
            boolean isModdedDye = Colorable.isModdedDye(dye.color());
            boolean hasInvalidDye = dye.isEmpty() || isModdedDye;

            String colorName = hasInvalidDye ? "white" : dye.color().getName();
            SpriteId TEXTURE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/dyespria/dyespria_top_" + colorName));

            poseStack.pushPose();

            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.translate(0.5D, -1.5D, -0.5D);

            if (isRotated(state.blockPos))
                poseStack.mulPose(Axis.YP.rotationDegrees(45));

            int color = isModdedDye ? dye.color().getTextColor() : 0xFFFFFFFF;

            submitNodeCollector.submitModelPart(
                    modelPart, poseStack, TEXTURE.renderType(RenderTypes::entityCutout), state.lightCoords, OverlayTexture.NO_OVERLAY, null, color, null);

            poseStack.popPose();
        }

        if(isGrown && hasDye && !blockState.getValue(MSFStateProperties.SHEARED)) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.9375, 0.5);
            poseStack.mulPose(entityRenderer.camera.rotation());
            poseStack.scale(0.35F, 0.35F, 0.35F);
            state.itemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }


    public static class State extends MSFBERenderState {
        @Nullable ItemStackRenderState itemStackRenderState = null;
        Dye dye;
    }
}
