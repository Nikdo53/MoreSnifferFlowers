package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.abraxator.moresnifferflowers.client.model.block.BerootCauldronModel;
import net.abraxator.moresnifferflowers.components.BetterNonNullList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

public class BerootCauldronRenderer<T extends BerootCauldronBlockEntity> extends MSFBERenderer<T, BerootCauldronRenderer.State> {
    final SpriteId CAULDRON_TEXTURE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/beroot_cauldron"));
    final SpriteId SPOON_TEXTURE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/beroot_spoon"));

    private final ModelPart cauldron;
    private final ModelPart spoon;

    public BerootCauldronRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.cauldron = context.bakeLayer(BerootCauldronModel.BEROOT_CAULDRON);
        this.spoon = context.bakeLayer(BerootCauldronModel.BEROOT_SPOON);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        final RenderType cauldronRenderType = CAULDRON_TEXTURE.renderType(RenderTypes::entityCutout);
        final RenderType spoonRenderType = SPOON_TEXTURE.renderType(RenderTypes::entityCutout);

        final Direction direction = state.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        if(IMultiBlock.isCenter(state.getBlockState())) {
            //CAULDRON
            poseStack.pushPose();
            poseStack.translate(1, 1.5, 0);
            poseStack.mulPose(Axis.XN.rotationDegrees(-180));
            rotate(poseStack, direction, false);
            submitNodeCollector.submitModelPart(cauldron, poseStack, cauldronRenderType, state.lightCoords, overlay(), sprites.get(CAULDRON_TEXTURE));
            poseStack.popPose();

            //SOUP
            poseStack.pushPose();
            poseStack.translate(1, 0.5, 0);
            poseStack.mulPose(Axis.XN.rotationDegrees(-180));
            rotate(poseStack, direction, false);
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix4f = pose.pose();
            Matrix3f matrix3f = pose.normal();
            int soupCount = state.soupCount;
            float size = 1.5F;
            float halfSize = size / 2.0F;
            float minX = -halfSize;
            float maxX = halfSize;
            float minZ = -halfSize;
            float maxZ = halfSize;
            float r = (float) (state.color.x / 255);
            float g = (float) (state.color.y / 255);
            float b = (float) (state.color.z / 255);
            float y = -((float) 1 / 6 * soupCount);
            float soupScale =0.332f;

            if (soupCount > 0) {
                poseStack.scale(1 + soupScale, 1, 1 + soupScale);
                poseStack.translate(-soupScale * 0.565, 0, soupScale * 0.565);
                submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.cutoutMovingBlock(), (p,  consumer) ->
                        renderFace(pose,consumer, r,g,b, 1f, minX, maxX, y, minZ, maxZ, state.lightCoords, state.isCrafted));
            }
            poseStack.popPose();

            //SPOON
            {
                float rot = -state.spoonRotation;
                poseStack.pushPose();
                poseStack.translate(1, 1.5, 0);
                poseStack.mulPose(Axis.XN.rotationDegrees(-180));
                rotate(poseStack, direction, false);
                poseStack.mulPose((new Quaternionf()).rotationY((float) (rot * (Math.PI / 180))));

                submitNodeCollector.submitModelPart(spoon, poseStack, spoonRenderType, state.lightCoords, overlay(), sprites.get(SPOON_TEXTURE));
                poseStack.popPose();
            }
            
            //ITEMS
            for (int i = 0; i < state.ingredients.getValidSize(); i++) {
                poseStack.pushPose();
                rotate(poseStack, direction, true);
                ItemStack itemStack = state.ingredients.get(i);
                float rot = (float) (state.itemsRotation * ((i + 1) * 0.1));
                //poseStack.translate(i / 0.2 + 0.1, absoluteY, i / 0.2 + 0.1);
                float a = (float) (i * 0.05);
                poseStack.translate(0.8 + a, (float) -y + 0.51F, 0.0 - a);
                poseStack.mulPose(new Quaternionf().rotationY((float) (rot * (Math.PI / 180))));
                poseStack.translate(0.25, 0, 0.25);
                poseStack.mulPose(new Quaternionf().rotationY((float) ((rot * 0.2) * (Math.PI / 180))));
                //poseStack.translate(randomSource.nextFloat(), 0, randomSource.nextFloat());
                poseStack.scale(0.5F, 0.5F, 0.5F);

                ItemStackRenderState itemState = new ItemStackRenderState();
                this.itemModelResolver
                        .updateForTopItem(itemState, itemStack, ItemDisplayContext.FIXED, level, null, 53);

                itemState.submit(poseStack, submitNodeCollector, state.lightCoords, overlay(), 0);
                poseStack.popPose();
            }
        }
    }

    private void rotate(PoseStack poseStack, Direction direction, Boolean isItems){
        if (!isItems) {
            switch (direction) {
                case EAST -> poseStack.translate(0, 0, -1);
                case WEST -> poseStack.translate(-1, 0, 0);
                case SOUTH -> poseStack.translate(-1, 0, -1);
            }
        } else {
            switch (direction) {
                case EAST -> poseStack.translate(0, 0, 1);
                case WEST -> poseStack.translate(-1, 0, 0);
                case SOUTH -> poseStack.translate(-1, 0, 1);
            }
            poseStack.translate(0, 0, 0.1);
        }
        if (!isItems) poseStack.mulPose(direction.getRotation());
        if (!isItems) poseStack.mulPose(Axis.XN.rotationDegrees(90));
    }

    private void renderFace(PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, float alpha, float x0, float x1, float y, float z0, float z1, int light, boolean isCrafted) {

        String name = isCrafted ? "beroot_soup1" : "beroot_soup";
        Identifier Identifier = MoreSnifferFlowers.loc("block/" + name);
        TextureAtlasSprite sprite = sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, Identifier));

        consumer.addVertex(pose, x1, y, z0).setColor(red, green, blue, alpha).setUv(sprite.getU0(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x1, y, z1).setColor(red, green, blue, alpha).setUv(sprite.getU0(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x0, y, z1).setColor(red, green, blue, alpha).setUv(sprite.getU1(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose, x0, y, z0).setColor(red, green, blue, alpha).setUv(sprite.getU1(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(1);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(T blockEntity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.soupCount = blockEntity.soupCount;
        state.spoonRotation = blockEntity.getSpoonRotation(partialTicks);
        state.itemsRotation = blockEntity.getItemsRotation(partialTicks);
        state.ingredients = blockEntity.ingredients;
        state.isCrafted = blockEntity.isCrafted;
        state.color = blockEntity.color();
    }

    public static class State extends MSFBERenderState {
        public int soupCount;
        public float spoonRotation;
        public float itemsRotation;
        public BetterNonNullList<ItemStack> ingredients;
        public boolean isCrafted;
        public Vec3 color;
    }
}
