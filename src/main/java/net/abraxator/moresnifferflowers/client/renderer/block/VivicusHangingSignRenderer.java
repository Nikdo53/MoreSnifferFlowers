package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.state.HangingSignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jspecify.annotations.Nullable;


public class VivicusHangingSignRenderer extends HangingSignRenderer {
    private final SpriteGetter sprites;
    BlockState blockState; // funny passthrough 2 for the color

    public VivicusHangingSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.sprites = context.sprites();
    }

    @Override
    public void submit(HangingSignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.blockState = state.blockState;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    protected void submitSign(PoseStack poseStack, int lightCoords, WoodType type, Model.Simple signModel, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, SubmitNodeCollector submitNodeCollector) {
        SpriteId sprite = this.getSignSprite(type);
        int color = -1;
        if (blockState.getBlock() instanceof ColorableVivicusBlock colorableBlock) {
            color = colorableBlock.colorValues().get(blockState.getValue(MSFStateProperties.COLOR));
        }
        submitNodeCollector.submitModel(signModel, Unit.INSTANCE, poseStack, lightCoords, OverlayTexture.NO_OVERLAY, color, sprite, this.sprites, 0, breakProgress);

        this.blockState = null;
    }


}
