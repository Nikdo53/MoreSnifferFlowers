package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.CropressorBlockEntity;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

public class CropressorBlockEntityRenderer extends MSFBERenderer<CropressorBlockEntity, CropressorBlockEntityRenderer.State> {
    public CropressorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        BlockState blockState = state.getBlockState();
        Direction direction = blockState.getValue(CropressorBlockBase.FACING).getOpposite();

        var progress = state.progress;
        if(progress > 0) {
            double scale = 100D;
            double d = progress / scale;
            Vec3 factor = switch (direction) {
                case NORTH -> new Vec3(0.5, 0, (1 - d));
                case EAST -> new Vec3(d, 0, 0.55);
                case SOUTH -> new Vec3(0.5, 0, d);
                default -> new Vec3((1 - d), 0, 0.55);
            };

            poseStack.pushPose();
            poseStack.translate(factor.x, 0.35, factor.z);
            poseStack.scale(0.4F, 0.4F, 0.4F);

            state.result.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, -1);
            poseStack.popPose();
        }

        //Progress Bar
        poseStack.pushPose();

        switch (direction){
            case NORTH -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(90F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(90F));
                poseStack.translate(1.5, -2.001, -0.5);

            }
            case EAST -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(90F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(180F));

                poseStack.translate(0.5, -2.001, -0.5);
            }

            case SOUTH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                poseStack.mulPose(Axis.XP.rotationDegrees(90F));
                poseStack.translate(0.5, -1.001, -0.5);

            }

            case WEST -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(90F));
                poseStack.translate(1.5, -1.001, -0.5);
            }
        }

        float[] rgb = MSFColorHandler.hexToRGB(state.color);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.cutoutMovingBlock(),
                ((pose, buffer) -> renderFace(pose.pose(), poseStack.last(), buffer, rgb[0], rgb[1], rgb[2], state.lightCoords, state.barLength)));
        poseStack.popPose();

    }

    private void renderFace(Matrix4f pose, PoseStack.Pose normal, VertexConsumer consumer, float red, float green, float blue, int light, int barLength) {

        float y = 1f;
        float size = 1F;
        float halfSize = size / 2.0F;

        float x0 = -halfSize;
        float x1 = halfSize;
        float z0 = -halfSize;
        float z1 = halfSize;

        String name = "cropressor_bar" + barLength;
        Identifier resourceLocation = MoreSnifferFlowers.loc("block/" + name);
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().get(new SpriteId(AtlasIds.BLOCKS,resourceLocation));

        consumer.addVertex(pose, x1, y, z0).setColor(red, green, blue, 1f).setUv(sprite.getU0(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x1, y, z1).setColor(red, green, blue, 1f).setUv(sprite.getU0(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x0, y, z1).setColor(red, green, blue, 1f).setUv(sprite.getU1(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x0, y, z0).setColor(red, green, blue, 1f).setUv(sprite.getU1(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(CropressorBlockEntity blockEntity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        state.progress = blockEntity.progress;
        ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(itemStackRenderState, blockEntity.result, ItemDisplayContext.FIXED, blockEntity.level(), null, 53);

        state.result = itemStackRenderState;
        state.barLength = blockEntity.barLength;
        state.color = blockEntity.getColor();

        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    }

    public static class State extends MSFBERenderState {
        public float progress;
        public ItemStackRenderState result;
        public int barLength;
        public int color;
    }
}
