package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.client.model.block.GiantCropModels;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.components.PreviewMode;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GiantCropBlockEntityRenderer<T extends GiantCropBlockEntity> implements BlockEntityRenderer<T> {
	private final Map<Block, ModelPart> modelPartMap = new HashMap<>();


    public GiantCropBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart carrot = context.bakeLayer(GiantCropModels.GIANT_CARROT).getChild("root");
		this.modelPartMap.put(MSFBlocks.GIANT_CARROT.get(), carrot);
        ModelPart potato = context.bakeLayer(GiantCropModels.GIANT_POTATO).getChild("root");
		this.modelPartMap.put(MSFBlocks.GIANT_POTATO.get(), potato);
        ModelPart netherwart = context.bakeLayer(GiantCropModels.GIANT_NETHERWART).getChild("root");
		this.modelPartMap.put(MSFBlocks.GIANT_NETHERWART.get(), netherwart);
        ModelPart beetroot = context.bakeLayer(GiantCropModels.GIANT_BEETROOT).getChild("root");
		this.modelPartMap.put(MSFBlocks.GIANT_BEETROOT.get(), beetroot);
        ModelPart wheat = context.bakeLayer(GiantCropModels.GIANT_WHEAT).getChild("root");
		this.modelPartMap.put(MSFBlocks.GIANT_WHEAT.get(), wheat);

        ModelPart onion = context.bakeLayer(GiantCropModels.GIANT_ONION).getChild("root");
        this.modelPartMap.put(MSFBlocks.GIANT_ONION.get(), onion);
        ModelPart tomato = context.bakeLayer(GiantCropModels.GIANT_TOMATO).getChild("root");
        this.modelPartMap.put(MSFBlocks.GIANT_TOMATO.get(), tomato);
        ModelPart cabbage = context.bakeLayer(GiantCropModels.GIANT_CABBAGE).getChild("root");
        this.modelPartMap.put(MSFBlocks.GIANT_CABBAGE.get(), cabbage);
        ModelPart rice = context.bakeLayer(GiantCropModels.GIANT_RICE).getChild("root");
        this.modelPartMap.put(MSFBlocks.GIANT_RICE.get(), rice);

    }

	@Override
	public void render(GiantCropBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		BlockState blockState = blockEntity.getBlockState();
		String path = blockState.getBlock().getDescriptionId().replace("block." + MoreSnifferFlowers.MOD_ID + ".", "");
		Material TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/" + path));

		PreviewMode previewMode = blockEntity.getPreviewMode();
		Function<Identifier, RenderType> renderType = RenderType::entityCutout;
		VertexConsumer vertexConsumer = TEXTURE.buffer(buffer, renderType);

		double growProgress = previewMode == PreviewMode.PLACED ? blockEntity.growProgress : 1;
		float coolPartialTick = (growProgress < 1 && blockState.is(MSFTags.BlockTags.GIANT_CROPS) && IMultiBlock.isCenter(blockState)) ? partialTick : 0;
		float coolGrowProgress = blockEntity.getLevel().getGameTime() - blockEntity.staticGameTime;

		if(growProgress > 0 && blockState.is(MSFTags.BlockTags.GIANT_CROPS) && IMultiBlock.isCenter(blockState)) {
			float yCord = 0.5F;
			float yScale = 1;

			if (previewMode != PreviewMode.PLACED) yCord++;

			if(growProgress < 1) {
				yCord = (coolGrowProgress + coolPartialTick) / 4 - 2;
				yScale = Mth.lerp((coolGrowProgress + coolPartialTick) / 10, 0, 1);
			}

			poseStack.pushPose();
			poseStack.translate(0.5, yCord, 0.5);
			poseStack.scale(1, yScale, 1);
			poseStack.mulPose(new Quaternionf().rotateX((float) (Math.PI)));

            if (blockState.is(MSFTags.BlockTags.NO_SHADING)) {
                vertexConsumer = new VertexConsumerWrapper(vertexConsumer) {
                    @Override
                    public VertexConsumer setNormal(float x, float y, float z) {
                        return super.setNormal(1, 1, 1);
                    }
                };
            }

            modelPartMap.get(blockState.getBlock()).render(poseStack, vertexConsumer, packedLight, packedOverlay);

			poseStack.popPose();
		}
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public AABB getRenderBoundingBox(T blockEntity) {
		return new AABB(blockEntity.getCenter()).inflate(1.1);
	}
}