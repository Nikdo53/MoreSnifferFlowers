package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.ModCauldronBlockEntity;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

public class ModCauldronRenderer extends MSFBERenderer<ModCauldronBlockEntity, ModCauldronRenderer.State> {
    private final Identifier ACID_TEXTURE = MoreSnifferFlowers.loc("block/acid_still");
    private final Identifier BONMEEL_TEXTURE = MoreSnifferFlowers.loc("block/bonmeel_still");

    public ModCauldronRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(ModCauldronRenderer.State renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        BlockState state = renderState.blockState;
        boolean isAcid = state.is(MSFBlocks.ACID_FILLED_CAULDRON.get());

        float y = switch (state.getValue(LayeredCauldronBlock.LEVEL)) {
            case 1 -> 0.55F;
            case 2 -> 0.75F;
            case 3 -> 0.93F;
            default -> 0f;
        };

        submitNodeCollector.submitMovingBlock(poseStack, renderState.originalCauldron);

        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.translate(-0.5D, -0.0D, 0.5D);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.solidMovingBlock(),
                (pose, vertexConsumer) -> renderFace(poseStack, vertexConsumer, 0.499f, -1 * y, renderState.lightCoords, isAcid));

        poseStack.popPose();
    }

    private void renderFace(PoseStack poseStack, VertexConsumer consumer, float size, float y, int light, boolean isAcid) {
        Identifier loc = isAcid ? ACID_TEXTURE : BONMEEL_TEXTURE;
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, loc));

        float x0 = -size;
        float x1 = size;
        float z0 = -size;
        float z1 = size;

        PoseStack.Pose last = poseStack.last();
        Matrix4f pose = last.pose();
        Matrix3f normal = last.normal().normal();

        consumer.addVertex(pose, x1, y, z0).setColor(1f, 1f, 1f, 1f).setUv(sprite.getU0(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(last, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x1, y, z1).setColor(1f, 1f, 1f, 1f).setUv(sprite.getU0(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(last, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x0, y, z1).setColor(1f, 1f, 1f, 1f).setUv(sprite.getU1(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(last, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x0, y, z0).setColor(1f, 1f, 1f, 1f).setUv(sprite.getU1(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(last, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ModCauldronRenderer.State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ModCauldronBlockEntity blockEntity, ModCauldronRenderer.State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        MovingBlockRenderState movingBlockRenderState = new MovingBlockRenderState();
        movingBlockRenderState.randomSeedPos = blockEntity.getBlockPos();
        movingBlockRenderState.blockPos = blockEntity.getBlockPos();
        movingBlockRenderState.blockState = blockEntity.getBlockState();
        movingBlockRenderState.biome = blockEntity.getLevel().getBiome(blockEntity.getBlockPos());
        movingBlockRenderState.cardinalLighting = Minecraft.getInstance().level.cardinalLighting();
        movingBlockRenderState.lightEngine = blockEntity.getLevel().getLightEngine();

        state.originalCauldron = movingBlockRenderState;
    }

    public static class State extends MSFBERenderState {
        public MovingBlockRenderState originalCauldron;
    }
}
