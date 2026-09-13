package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;

import net.abraxator.moresnifferflowers.client.model.block.BondripiaModel;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import org.jetbrains.annotations.NotNull;

public class BondripiaBlockEntityRenderer<T extends BondripiaBlockEntity> implements BlockEntityRenderer<T> {
    private ModelPart model;
    private static final Material BONDRIPIA_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/bondripia"));
    private static final Material ACIDRIPIA_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/acidripia"));

    public BondripiaBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.model = context.bakeLayer(BondripiaModel.BONDRIPIA);
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if(IMultiBlock.isCenter(blockEntity.getBlockState()) && blockEntity.getBlockState().getValue(MSFStateProperties.AGE_2) >= 2) {
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.XP.rotationDegrees(180));

            Material material = blockEntity.getBlockState().is(MSFBlocks.ACIDRIPIA.get()) ? ACIDRIPIA_TEXTURE : BONDRIPIA_TEXTURE;

            VertexConsumer consumer = material.buffer(buffer, RenderType::entityCutout);

            model.render(poseStack, consumer, packedLight, packedOverlay);
        }
    }

    public int getViewDistance() {
        return 256;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(T blockEntity) {
        return new AABB(blockEntity.getCenter()).inflate(1);
    }
}
