package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.client.model.block.GiantCropModels;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.nikdo53.tinymultiblocklib.components.PreviewMode;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GiantCropBlockEntityRenderer<T extends GiantCropBlockEntity> extends MSFBERenderer<T, GiantCropBlockEntityRenderer.State> {
	private final Map<Block, ModelPart> modelPartMap = new HashMap<>();


    public GiantCropBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
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
	public void extractRenderState(T blockEntity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.growProgress = blockEntity.clientGrowthTicks - 1;
		state.isPreview = blockEntity.getPreviewMode() != PreviewMode.PLACED;
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		BlockState blockState = state.getBlockState();
		if (blockState.getRenderShape() == RenderShape.INVISIBLE) return;

		String path = blockState.getBlock().getDescriptionId().replace("block." + MoreSnifferFlowers.MOD_ID + ".", "");
		SpriteId TEXTURE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/" + path));

		int length = 10;
		float overshoot = 0.04f;
		float smoothProgress = (float) Math.max(0, (state.growProgress + state.partialTicks) / length);

		float yCord = 0.5F;
		float yScale = 1;

		if (state.isPreview) yCord++;

		if(smoothProgress < 1 + overshoot) {
			yCord = smoothProgress * 1.5f - 1.0f;
			yScale = smoothProgress;
		}

		poseStack.pushPose();
		poseStack.translate(0.5, yCord, 0.5);
		poseStack.scale(1, yScale, 1);
		poseStack.mulPose(new Quaternionf().rotateX((float) (Math.PI)));

		submitNodeCollector.submitModelPart(modelPartMap.get(blockState.getBlock()),
				poseStack,
				TEXTURE.renderType(RenderTypes::entityCutoutCull),state.lightCoords, OverlayTexture.NO_OVERLAY, this.sprites.get(TEXTURE));

		poseStack.popPose();

	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public AABB getRenderBoundingBox(T blockEntity) {
		return new AABB(blockEntity.getCenter()).inflate(1.1);
	}

	public static class State extends MSFBERenderState {
		public double growProgress;
		public boolean isPreview;
    }
}