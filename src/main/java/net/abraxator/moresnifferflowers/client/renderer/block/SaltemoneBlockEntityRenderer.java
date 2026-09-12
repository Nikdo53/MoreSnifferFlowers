package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.SaltemoneBlockEntity;

import net.abraxator.moresnifferflowers.client.model.block.SaltemoneModel;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import org.jetbrains.annotations.NotNull;

public class SaltemoneBlockEntityRenderer<T extends SaltemoneBlockEntity> extends MSFBERenderer<T, SaltemoneBlockEntityRenderer.State> {
    private final ModelPart body;
    private final ModelPart top;
    private static final SpriteId SALTEMONE_TEXTURE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/saltemone"));
    private static final SpriteId SOURLEMON_TEXTURE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/sourlemon"));

    public SaltemoneBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.body = context.bakeLayer(SaltemoneModel.SALTEMONE);
        this.top = context.bakeLayer(SaltemoneModel.SALTEMONE_TOP);
    }

    @Override
    public SaltemoneBlockEntityRenderer.State createRenderState() {
        return new State();
    }

    @Override
    public void submit(SaltemoneBlockEntityRenderer.State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        BlockState blockState = state.blockState;
        if(IMultiBlock.isCenter(blockState) && blockState.getValue(MSFStateProperties.AGE_2) >= 2) {
            SpriteId material = blockState.is(MSFBlocks.SOURLEMONE.get()) ? SOURLEMON_TEXTURE : SALTEMONE_TEXTURE;
            RenderType renderType = material.renderType(RenderTypes::entityCutout);

            poseStack.pushPose();
            Direction direction = blockState.getValue(HorizontalDirectionalBlock.FACING);
            poseStack.mulPose(direction.getCounterClockWise().getRotation());
            poseStack.mulPose(Axis.XN.rotationDegrees(-90));
            poseStack.translate(0, -1.4, 0);

            switch (direction) {
                case EAST -> poseStack.translate(-1, 0, 1);
                case WEST -> poseStack.translate(0, 0, 0);
                case SOUTH -> poseStack.translate(-1, 0, 0);
                case NORTH -> poseStack.translate(0, 0, 1);
            }

            submitNodeCollector.submitModelPart(body, poseStack, renderType, state.lightCoords, overlay(), sprites.get(material));

            float time = (getLevel().getGameTime() + state.partialTicks) / 20f;
            float scale = 1.0f + 0.3f * Mth.sin(time / 2 * Mth.TWO_PI);
            poseStack.scale(scale, scale / 1.5f + 0.4f, scale);
            poseStack.translate(0, -scale + 2.32, 0);

            submitNodeCollector.submitModelPart(top, poseStack, renderType, state.lightCoords, overlay(), sprites.get(material));
            poseStack.popPose();
        }
    }

    public int getViewDistance() {
        return 256;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(T blockEntity) {
        return new AABB(blockEntity.getCenter()).inflate(1);
    }

    public static class State extends MSFBERenderState {

    }

}
