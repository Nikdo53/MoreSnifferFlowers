package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.init.config.MSFClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import net.neoforged.neoforge.client.model.quad.BakedColors;
import net.neoforged.neoforge.client.model.quad.BakedNormals;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockPatternRenderer {
    public static void renderAll(AddSectionGeometryEvent.@NotNull SectionRenderingContext context, Set<BlockPatternQuad> quads) {
        boolean isTransparent = MSFClientConfig.CLIENT_CONFIG.isLoaded() && MSFClientConfig.BLOCK_PATTERN_TRANSPARENCY.get();

        PoseStack poseStack = new PoseStack();

        for (BlockPatternQuad quad : quads) {
            BlockPos pos = quad.pos();
            poseStack.pushPose();
            poseStack.translate(SectionPos.sectionRelative(pos.getX()), SectionPos.sectionRelative(pos.getY()), SectionPos.sectionRelative(pos.getZ()));

            quad.render(poseStack, context.getOrCreateChunkBuffer(isTransparent ? ChunkSectionLayer.TRANSLUCENT : ChunkSectionLayer.CUTOUT), context);

            poseStack.popPose();
        }
    }

    public static boolean isInsideSection(BlockPos origin, BlockPos pos) {
        return SectionPos.of(origin).equals(SectionPos.of(pos));
    }

    @SuppressWarnings("deprecation")
    public static Set<BlockPatternQuad> cache(Level level, BlockPos origin) {
        Set<BlockPatternQuad> quads = new HashSet<>();

        Map<BlockPos, BlockPatternCapability.PatternData> patterns = BlockPatternCapability.getPatterns(level, origin);
        for (BlockPos pos : patterns.keySet()) {
            if (!isInsideSection(origin, pos) ) continue;

            BlockPatternCapability.PatternData data = patterns.get(pos);

            Identifier Identifier = MoreSnifferFlowers.loc("block/block_pattern/" + BlockPattern.fromId(data.patternId()).getSerializedName());
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(Identifier);
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;

            for (Direction dir : Direction.values()) {
                BlockPos relativePos = pos.relative(dir);
                BlockState relativeState = level.getBlockState(relativePos);

                boolean faceSturdy = state.isFaceSturdy(level, pos, dir);
                boolean notBlocked = !relativeState.isFaceSturdy(level, relativePos, dir.getOpposite());
                boolean noOcclusion = !relativeState.canOcclude();

                boolean canRenderFace = faceSturdy && (notBlocked || noOcclusion);
                if (!canRenderFace) continue;


                quads.add(new BlockPatternQuad(pos, dir, data.color(), sprite, MSFClientConfig.BLOCK_PATTERN_SMOOTH_LIGHTING.get(), data.direction(), data.isGlowing()));
            }
        }
        return quads;
    }


    public record BlockPatternQuad(BlockPos pos, Direction direction, int color, TextureAtlasSprite sprite, boolean smoothLighting, Direction rotation, boolean isGlowing) {
        private void render(PoseStack poseStack, VertexConsumer buffer, AddSectionGeometryEvent.SectionRenderingContext context) {
            poseStack.pushPose();
         //   poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
            translateToFace(poseStack, direction, pos);

            Vec3i n = direction.getUnitVec3i();
            float nx = n.getX(), ny = n.getY(), nz = n.getZ();

            int rgb = color;
            float r = ((rgb >> 16) & 0xFF) / 255f;
            float g = ((rgb >> 8) & 0xFF) / 255f;
            float b = (rgb & 0xFF) / 255f;

            Matrix4f pose = poseStack.last().pose();

            /*  if (direction == Direction.UP) {
                   r = 1F;
                   g = 1F;
                   b = 1F;
               }*/

            float u0 = sprite.getU1();
            float u1 = sprite.getU0();
            float u2 = sprite.getU0();
            float u3 = sprite.getU1();

            float v0 = sprite.getV1();
            float v1 = sprite.getV1();
            float v2 = sprite.getV0();
            float v3 = sprite.getV0();

            if (rotation == Direction.EAST) {
                u0 = sprite.getU0();
                u1 = sprite.getU0();
                u2 = sprite.getU1();
                u3 = sprite.getU1();
                v0 = sprite.getV0();
                v1 = sprite.getV1();
                v2 = sprite.getV1();
                v3 = sprite.getV0();
            }
            if (rotation == Direction.SOUTH) {
                u0 = sprite.getU0();
                u1 = sprite.getU1();
                u2 = sprite.getU1();
                u3 = sprite.getU0();
                v0 = sprite.getV0();
                v1 = sprite.getV0();
                v2 = sprite.getV1();
                v3 = sprite.getV1();
            }
            if (rotation == Direction.WEST) {
                u0 = sprite.getU1();
                u1 = sprite.getU1();
                u2 = sprite.getU0();
                u3 = sprite.getU0();
                v0 = sprite.getV1();
                v1 = sprite.getV0();
                v2 = sprite.getV0();
                v3 = sprite.getV1();
            }

            boolean translucencyEnabled = MSFClientConfig.BLOCK_PATTERN_TRANSPARENCY.get();
            BakedQuad quad = new BakedQuad(
                    new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0, 0, 0),
                    UVPair.pack(u1, v2), UVPair.pack(u2, v1), UVPair.pack(u3, v0), UVPair.pack(u0, v3),
                    direction,
                    new BakedQuad.MaterialInfo(
                            sprite,
                            translucencyEnabled ? ChunkSectionLayer.TRANSLUCENT : ChunkSectionLayer.CUTOUT,
                            translucencyEnabled ? RenderTypes.cutoutMovingBlock() : RenderTypes.translucentMovingBlock(),
                            -1, true, isGlowing ? 15 : 0, MSFClientConfig.BLOCK_PATTERN_SMOOTH_LIGHTING.getAsBoolean())
            , BakedNormals.UNSPECIFIED, BakedColors.of(ARGB.color(255, (int) (r * 255), (int) (g * 255), (int) (b * 255))));

            QuadInstance quadInstance = new QuadInstance();
            context.getBlockRenderer().lighter.prepareQuadAmbientOcclusion(Minecraft.getInstance().level, Blocks.DIAMOND_BLOCK.defaultBlockState(), pos, quad, quadInstance);

            buffer.putBakedQuad(poseStack.last(), quad, quadInstance);
/*
            buffer.addVertex(pose, 1, 0, 0).setColor(r * brightness0, g * brightness0, b * brightness0, 1f).setUv(u1, v2).setLight(lightmap[0]).setNormal(poseStack.last(), nx, ny, nz);
            buffer.addVertex(pose, 1, 0, 1).setColor(r * brightness1, g * brightness1, b * brightness1, 1f).setUv(u2, v1).setLight(lightmap[1]).setNormal(poseStack.last(), nx, ny, nz);
            buffer.addVertex(pose, 0, 0, 1).setColor(r * brightness2, g * brightness2, b * brightness2, 1f).setUv(u3, v0).setLight(lightmap[2]).setNormal(poseStack.last(), nx, ny, nz);
            buffer.addVertex(pose, 0, 0, 0).setColor(r * brightness3, g * brightness3, b * brightness3, 1f).setUv(u0, v3).setLight(lightmap[3]).setNormal(poseStack.last(), nx, ny, nz);
*/

            poseStack.popPose();
        }
    }

    private static void translateToFace(PoseStack stack, Direction face, BlockPos pos) {
        double configOffset = 0.001f;
        float distance = (float) (configOffset * (Math.abs((pos.getX() + pos.getY() + pos.getZ()) % 4) + 1));
        float scale = distance*2;
        switch (face) {
            case UP -> {
              //  stack.mulPose(Axis.XP.rotationDegrees(90));
              //  stack.translate(0, 0, -1 - distance);
                stack.translate(1, 1 + distance, 0);
                stack.mulPose(Axis.YP.rotationDegrees(180));
                stack.mulPose(Axis.XP.rotationDegrees(180));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance, 0, -distance);
            }
            case DOWN -> {
                stack.translate(1, 0 - distance, 1);
                stack.mulPose(Axis.YP.rotationDegrees(180));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance, 0, -distance);

            }
            case NORTH -> {
                stack.translate(0, 1, -0 - distance);
                stack.mulPose(Axis.XP.rotationDegrees(90));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance,0, -distance);

            }
            case SOUTH -> {
                stack.translate(1, 1, 1 + distance);
                stack.mulPose(Axis.XN.rotationDegrees(90));
                stack.mulPose(Axis.YP.rotationDegrees(180));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance,0, -distance);
            }
            case WEST -> {
                stack.translate(0 - distance, 1, 1);
                stack.mulPose(Axis.XP.rotationDegrees(90));
                stack.mulPose(Axis.ZP.rotationDegrees(-90));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance,0, -distance);

            }
            case EAST -> {
                stack.translate(1 + distance, 1, 0);
                stack.mulPose(Axis.XP.rotationDegrees(90));
                stack.mulPose(Axis.ZP.rotationDegrees(90));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance,0, -distance);

            }
        }
    }

    public static int getPackedLight(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightCoordsUtil.pack(blockLight, skyLight);
    }
}
