package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.client.model.block.BondripiaModel;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.phys.AABB;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import org.jetbrains.annotations.NotNull;

public class BondripiaBlockEntityRenderer<T extends BondripiaBlockEntity> extends MSFBERenderer<T, MSFBERenderState> {
    private ModelPart model;
    private static final SpriteId BONDRIPIA_TEXTURE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/bondripia"));
    private static final SpriteId ACIDRIPIA_TEXTURE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/acidripia"));

    public BondripiaBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = context.bakeLayer(BondripiaModel.BONDRIPIA);
    }

    @Override
    public MSFBERenderState createRenderState() {
        return new MSFBERenderState();
    }

    @Override
    public void submit(MSFBERenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if(IMultiBlock.isCenter(state.getBlockState()) && state.getBlockState().getValue(MSFStateProperties.AGE_2) >= 2) {
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.XP.rotationDegrees(180));

            SpriteId material = state.getBlockState().is(MSFBlocks.ACIDRIPIA.get()) ? ACIDRIPIA_TEXTURE : BONDRIPIA_TEXTURE;

            submitNodeCollector.submitModelPart(model, poseStack, material.renderType(RenderTypes::entityCutout), state.lightCoords, OverlayTexture.NO_OVERLAY, sprites.get(material), -1, state.breakProgress);
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
