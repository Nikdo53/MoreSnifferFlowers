package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.CropressorBlockEntity;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class CropressorBlockEntityRenderer implements BlockEntityRenderer<CropressorBlockEntity> {
    public CropressorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CropressorBlockEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = blockEntity.getBlockState();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Direction direction = blockEntity.getBlockState().getValue(CropressorBlockBase.FACING).getOpposite();

        var progress = blockEntity.progress;
        if( progress > 0) {
            double scale = 100D;
            double d = progress / scale;
            Vec3 factor = switch (direction) {
                case NORTH -> new Vec3(0.5, 0, (1 - d));
                case EAST -> new Vec3(d, 0, 0.55);
                case SOUTH -> new Vec3(0.5, 0, d);
                default -> new Vec3((1 - d), 0, 0.55);
            };

            pose.pushPose();
            pose.translate(factor.x, 0.35, factor.z);
            pose.scale(0.4F, 0.4F, 0.4F);
            itemRenderer.renderStatic(blockEntity.result, ItemDisplayContext.FIXED, light, overlay, pose, buffer, blockEntity.getLevel(), ((int) blockEntity.getBlockPos().asLong()));
            pose.popPose();
        }

        //Progress Bar
        pose.pushPose();

        switch (direction){
            case NORTH -> {
                pose.mulPose(Axis.XP.rotationDegrees(90F));
                pose.mulPose(Axis.ZP.rotationDegrees(90F));
                pose.translate(1.5, -2.001, -0.5);

            }
            case EAST -> {
                pose.mulPose(Axis.XP.rotationDegrees(90F));
                pose.mulPose(Axis.ZP.rotationDegrees(180F));

                pose.translate(0.5, -2.001, -0.5);
            }

            case SOUTH -> {
                pose.mulPose(Axis.YP.rotationDegrees(90F));
                pose.mulPose(Axis.XP.rotationDegrees(90F));
                pose.translate(0.5, -1.001, -0.5);

            }

            case WEST -> {
                pose.mulPose(Axis.XP.rotationDegrees(90F));
                pose.translate(1.5, -1.001, -0.5);
            }
        }

        float[] rgb = MSFColorHandler.hexToRGB(blockEntity.getColor());
        renderFace(pose.last().pose(), pose.last(), buffer.getBuffer(RenderType.cutoutMipped()), rgb[0], rgb[1], rgb[2], light, blockEntity.barLength);
        pose.popPose();

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
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(resourceLocation);

        consumer.addVertex(pose, x1, y, z0).setColor(red, green, blue, 1f).setUv(sprite.getU0(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x1, y, z1).setColor(red, green, blue, 1f).setUv(sprite.getU0(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x0, y, z1).setColor(red, green, blue, 1f).setUv(sprite.getU1(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x0, y, z0).setColor(red, green, blue, 1f).setUv(sprite.getU1(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal, 0.0F, 1.0F, 0.0F);
    }
}
