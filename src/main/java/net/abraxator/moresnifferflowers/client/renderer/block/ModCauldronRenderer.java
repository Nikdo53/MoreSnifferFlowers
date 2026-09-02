package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.ModCauldronBlockEntity;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ModCauldronRenderer implements BlockEntityRenderer<ModCauldronBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;
    private final Identifier ACID_TEXTURE = MoreSnifferFlowers.loc("block/acid_still");
    private final Identifier BONMEEL_TEXTURE = MoreSnifferFlowers.loc("block/bonmeel_still");

    public ModCauldronRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(ModCauldronBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        boolean isAcid = state.is(MSFBlocks.ACID_FILLED_CAULDRON.get());

        float y = switch (state.getValue(LayeredCauldronBlock.LEVEL)) {
            case 1 -> 0.55F;
            case 2 -> 0.75F;
            case 3 -> 0.93F;
            default -> 0f;
        };

        blockRenderer.renderSingleBlock(blockEntity.originalCauldron, poseStack, buffer, packedLight, packedOverlay);

        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.translate(-0.5D, -0.0D, 0.5D);
        renderFace(poseStack, buffer.getBuffer(RenderType.solid()), 0.499f, -1 * y, packedLight, isAcid);

        poseStack.popPose();
    }

    private void renderFace(PoseStack poseStack, VertexConsumer consumer, float size, float y, int light, boolean isAcid) {
        Identifier Identifier = isAcid ? ACID_TEXTURE : BONMEEL_TEXTURE;
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(Identifier);

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

}
